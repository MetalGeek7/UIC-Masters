#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/uio.h>
#include <sys/socket.h>
#include <sys/select.h>
#include <netinet/in.h>
#include "hw6.h"

#define ALPHA .75
#define min(a, b) (((a) < (b)) ? (a) : (b))

enum user{
  UNKNOWN = 0, Sender = 1, Receiver = 2
}user = UNKNOWN;
 
struct sockaddr_in otherhost_addr;
struct timeval timeout;

int sequence_number;
int exp_sequence_number = 0;
int rtt_count;
int close_retry_attempts = 0;
unsigned int rtt;
unsigned int deviation;
unsigned int ack_time;

int timeval_to_msec(struct timeval *t) { 
	return t->tv_sec*1000+t->tv_usec/1000;
}

void msec_to_timeval(int millis, struct timeval *out_timeval) {
	out_timeval->tv_sec = millis/1000;
	out_timeval->tv_usec = (millis%1000)*1000;
}

int current_msec() {
	struct timeval t;
	gettimeofday(&t,0);
	return timeval_to_msec(&t);
}

int rel_connect(int socket,struct sockaddr_in *toaddr,int addrsize) {
  //connect(socket,(struct sockaddr*)toaddr,addrsize);                     //connect does not do anything here
  
  otherhost_addr = *toaddr;
  //user = UNKNOWN;
  //rtt = 120;                    //Initial RTT
  //rtt_count = 0;
  return 0;
}

int rel_rtt(int socket) {
  return rtt;
}

/*
void send_ack(int sock, int seq_no)
{
  char packet[1400];
  struct hw6_hdr* hdr = (struct hw6_hdr*)packet;
  hdr->sequence_number = htonl(sequence_number);
  hdr->ack_number = htonl(seq_no);                    //received everything up until byte no. (seq_no-1) and waiting for bytes seq_no and forward 
  send(sock, packet, sizeof(struct hw6_hdr), 0);
  sequence_number++;
}
*/

/*
int receive_ack(int sock, int timeout)
{
  int status = 1;
  char buff[1400];
  struct hw6_hdr* hdr = (struct hw6_hdr*)buff;
  
  struct timeval tv;
  msec_to_timeval(timeout, &tv);
  setsockopt(sock, SOL_SOCKET, SO_RCVTIMEO, (struct timeval*)&tv, sizeof(struct timeval));    //Manipulating options at the  sockets API level
  
  status = recv(sock, buff, MAX_PACKET, 0);
  if(status < 0)    //Timeout occured and ACK not received
    return -2;
  
  int ack_no = ntohl(hdr->ack_number);
  if(ack_no>=0)
    return ack_no;
  
  return -1;
}
*/


void rel_send(int sock, void *buf, int len)
{
  int close_packet = 0;
  if(len == 0)
    close_packet = 1;

  // make the packet = header + buf
  char packet[1400];
  struct hw6_hdr *hdr = (struct hw6_hdr*)packet;
  hdr->sequence_number = htonl(sequence_number);
  memcpy(hdr+1,buf,len); //hdr+1 is where the payload starts

  int rexmit = 0;
  fprintf(stderr,"\rPacket %d with rtt %d timeout %d ms \n",sequence_number, rtt, timeval_to_msec(&timeout));
  sendto(sock, packet, sizeof(struct hw6_hdr)+len, 0,(struct sockaddr*)&otherhost_addr,sizeof(otherhost_addr));
  //send(sock, packet, sizeof(struct hw6_hdr)+len, 0);

  while(1)
  {
    if(close_packet)
    {
      close_retry_attempts++;
      
    }
    
    int sent_time = current_msec();              //start of RTT
    
    fd_set readset;
    FD_ZERO(&readset);
    FD_SET(sock,&readset); 
    struct timeval t = timeout;
    
    int ready = select(FD_SETSIZE,&readset,0,0,&t);
    if(ready==0){
      msec_to_timeval(min(5000,2*timeval_to_msec(&timeout)), &timeout);
      //fprintf(stderr,"We timed out...hence retransmit.....\n");
      fprintf(stderr,"\rPacket %d with rtt %d timeout %d ms \n",sequence_number, rtt, timeval_to_msec(&timeout));
      sendto(sock, packet, sizeof(struct hw6_hdr)+len, 0,(struct sockaddr*)&otherhost_addr,sizeof(otherhost_addr));
      //send(sock, packet, sizeof(struct hw6_hdr)+len, 0);
      rexmit = 1;
    }
    else if(ready==-1)
      perror("Error while select \n");
    /* Listen for acknowledgement from "OTHER_HOST" after sending */
    else{
      char incoming[1400];
      struct sockaddr_in from_addr;
      unsigned int addrsize = sizeof(from_addr);
      int recv_count=recvfrom(sock,incoming,1400,0,(struct sockaddr*)&from_addr,&addrsize);
      if(recv_count<0) {
	perror("Error while receiving packet.");
	return;
      }
      
      /* ACK received is for the latest packet....Alls Well......update RTT for this packet.*/
      struct hw6_hdr *hdr = (struct hw6_hdr*)incoming;
      if(ntohl(hdr->ack_number) == sequence_number) {
	if(!rexmit){
	  //update RTT here
	  ack_time = current_msec();
	  
	  deviation = 0.75*deviation + 0.25*(abs((ack_time - sent_time) - rtt));
	  rtt = (float)ALPHA * rtt + (float)(1 - ALPHA) * (ack_time - sent_time);
	  msec_to_timeval(rtt+4*deviation,&timeout);
	}
	  
	sequence_number++;
	break;
      }
      
      /* End of the stream code goes over here */
      /*      else if(ntohl(hdr->ack_number) < sequence_number){
	fprintf(stderr, "Closing gracefully at the Senders side \n");
	break;
	}*/

      
    }
    //send(sock, packet, sizeof(struct hw6_hdr)+len, 0);
    //sequence_number++;

  }
}

int rel_socket(int domain, int type, int protocol) {
	sequence_number = 0;
	rtt = 500;
	deviation = 50;
	//timeout.tv_usec = 600000;
	timeout.tv_usec = (rtt + 4*deviation) * 1000;
	return socket(domain, type, protocol);
}

int rel_recv(int sock, void * buffer, size_t length) {
	char packet[MAX_PACKET];
	memset(&packet,0,sizeof(packet));
	struct hw6_hdr* hdr=(struct hw6_hdr*)packet;	

	while(1){

	  //struct sockaddr_in fromaddr;
	unsigned int addrlen=sizeof(otherhost_addr);	
	int recv_count = recvfrom(sock, packet, MAX_PACKET, 0, (struct sockaddr*)&otherhost_addr, &addrlen);		

	// this is a shortcut to 'connect' a listening server socket to the incoming client.
	// after this, we can use send() instead of sendto(), which makes for easier bookkeeping
	//if(connect(sock, (struct sockaddr*)&fromaddr, addrlen)) {
	//perror("couldn't connect socket");
	//}
	
	if(recv_count<0)
	  break;
	
	/* Got the expected packet....Send an ACK for it*/
	if(ntohl(hdr->sequence_number) == exp_sequence_number){
	  struct hw6_hdr ack;
	  ack.ack_number = hdr->sequence_number;
	  //send(sock, &ack, sizeof(struct hw6_hdr)+length, 0);
	  sendto(sock, &ack, sizeof(ack), 0, (struct sockaddr*)&otherhost_addr, addrlen);
	  
	  exp_sequence_number++;
	  fprintf(stderr, "Got packet %d.......Waiting for packet %d\n", ntohl(hdr->sequence_number), exp_sequence_number);

	  memcpy(buffer, packet+sizeof(struct hw6_hdr), recv_count-sizeof(struct hw6_hdr));
	  return recv_count - sizeof(struct hw6_hdr);
	}
	else {
	  //ACK what we recieved so far
	  struct hw6_hdr ack;
	  fprintf(stderr, "ACKing what we received so far till Sequence Number %d\n", (exp_sequence_number-1));
	  ack.ack_number = htonl(exp_sequence_number-1);
	  sendto(sock, &ack, sizeof(ack), 0, (struct sockaddr*)&otherhost_addr, addrlen);        
	}
	
	}
	
}

int rel_close(int sock) {
	rel_send(sock, 0, 0); // send an empty packet to signify end of file
	fprintf(stderr, "Sent EOF. Now closing the connection\n");
	
	/*if(close_retry_attempts<20){
	  rel_recv(sock,0,0);

	  }*/
	struct timeval t;
        t.tv_sec = 2;
        t.tv_usec = 0;
        setsockopt(sock, SOL_SOCKET, SO_RCVTIMEO, &t, sizeof(t));	
	
	/* Waiting for some time before both Sender and Receiver can shut down i.e for 2 secs*/
	int close_waitstart = current_msec();
	while(current_msec() - close_waitstart < 2000) {                
	  rel_recv(sock,0,0);
        }
	
	
	close(sock);
}

