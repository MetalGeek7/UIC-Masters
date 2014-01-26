#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/uio.h>
#include <sys/socket.h>
#include <sys/select.h>
#include <netinet/in.h>
#include "hw7.h"
#include "cqueue.c"

#define ALPHA .80
#define WINDOW 300
#define min(a, b) (((a) < (b)) ? (a) : (b))

enum user{
  UNKNOWN = 0, SENDER = 1, RECEIVER = 2
}user = UNKNOWN;

 
struct sockaddr_in otherhost_addr;
struct timeval timeout;

int sequence_number;
int exp_sequence_number = 0;
unsigned int start_seq_no;
unsigned int rx_start_seq_no;
unsigned int window_size;
unsigned int rx_window_size;
unsigned int last_packet_seqnum_sent = -1;

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
  return 0;
}

int rel_rtt(int socket) {
  return rtt;
}

struct senderQElements{
  
  char packet[MAX_PACKET];
  char id_flag;
  char retx:1;
  char ack_retx:1;
  int packet_len;
  unsigned sent_time;
  
};

void send_UnACKed_pkts(int sock)
{
  int size;
  size = getCurrentQSize();
  for(int i=0; i<size; i++){
    
    struct senderQElements* s;
    s = getNthElement(i);
    if(!s->ack_retx){
      //Resend
      fprintf(stderr,"\rRetransmitting Packet# %d with rtt %d dev %d timeout - %d ms  \n",i+start_seq_no,rtt, deviation,timeval_to_msec(&timeout)); //might have to change this
      s->retx = 1;
      
      sendto(sock, s->packet, s->packet_len, 0,(struct sockaddr*)&otherhost_addr,sizeof(otherhost_addr));
    }
  }
}

void increase_window_size()
{
  if(window_size == WINDOW){
    return;
  }
  if(window_size >= 51){ 
  //Slow down
    window_size+=1;
  }
  else{
    window_size*=2;
  }
  
  //safety double check
  if(window_size > WINDOW){
    window_size = WINDOW;
  }
}

void decrease_window_size()
{
  if(window_size > 1)
    window_size-=1;
}

void rel_send(int sock, void *buf, int len)
{
  if(user == UNKNOWN){
    user = SENDER;
  }
  
  int flag_ack_chked;
  int flag_pkt_dropped;

  //int close_packet = 0;
  //if(len == 0)
  //close_packet = 1;

  // make the packet = header + buf
  char packet[1400];
  struct hw7_hdr *hdr = (struct hw7_hdr*)packet;
  flag_ack_chked = 0;
  flag_pkt_dropped = 0;
  memset(hdr, 0, sizeof(struct hw7_hdr));
  hdr->sequence_number = htonl(sequence_number);
  memcpy(hdr+1,buf,len); //hdr+1 is where the payload starts

  int rexmit = 0;
  fprintf(stderr,"\rPacket %d with rtt %d timeout %d ms \n",sequence_number, rtt, timeval_to_msec(&timeout));
  sendto(sock, packet, sizeof(struct hw7_hdr)+len, 0,(struct sockaddr*)&otherhost_addr,sizeof(otherhost_addr));

  /* insert packet in queue */
  struct senderQElements *s;
  s = calloc(sizeof(struct senderQElements),1);
  s->id_flag='S';
  s->retx = 0;
  s->ack_retx = 0;
  s->packet_len = sizeof(struct hw7_hdr) + len;
  s->sent_time = current_msec();
  memcpy(s->packet, packet, s->packet_len);
  doQInsert((char *)s);
  
  /* 1. If queue has num of elements == window size or if this is the close packet */
  if(getCurrentQSize() == window_size || len == 0){
    int ack_count = 0;
    
    while(ack_count < window_size){
      if(getCurrentQSize() == 0)
	break;
      
      fd_set readset;
      FD_ZERO(&readset);
      FD_SET(sock,&readset); 
      struct timeval t = timeout;
      
      int ready = select(FD_SETSIZE,&readset,0,0,&t);
      if(ready==0){
	fprintf(stderr,"We timed out...hence retransmit.....\n");
	send_UnACKed_pkts(sock);
	flag_pkt_dropped = 1;
	
	/* timed out ....send again with double the timeout value......tweaking left */
	msec_to_timeval(min(1000,2*timeval_to_msec(&timeout)), &timeout);
	
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
	struct hw7_hdr *hdr = (struct hw7_hdr*)incoming;
	if(ntohl(hdr->ack_number) >= start_seq_no && ntohl(hdr->ack_number) <= (start_seq_no + window_size - 1)) {
	  fprintf(stderr, "\r Got ACK for packet# %d\n",ntohl(hdr->ack_number));
	  
	  struct senderQElements *s;
	  s = getNthElement(ntohl(hdr->ack_number) - start_seq_no);
	  fprintf(stderr, "Seq No. of nth element in queue = %d\n", ntohl(((struct hw7_hdr *)s->packet)->sequence_number));
	  if(s->ack_retx){
	    fprintf(stderr,"Ack ReXmitted - %d , doing nothing\n", (ntohl(hdr->ack_number)) - start_seq_no);
	  }
	  else{
	    ack_count++;
	    if(!s->retx){
	      ack_time = current_msec();
	  
	      deviation = 0.75*deviation + 0.25*(abs((ack_time - (s->sent_time)) - rtt));
	      rtt = (float)ALPHA * rtt + (float)(1 - ALPHA) * (ack_time - (s->sent_time));
	      msec_to_timeval(rtt+4*deviation,&timeout);

	    }
	  }
	  s->ack_retx = 1;
	}
	
	/* Case: 1.Sender sends close 2.receiver sends ACK(viz is lost) 3.rcver sends close 4. sender receives close in rel_send() */  
	/*
	if(!(hdr->flags & ACK)){
	  struct hw7_hdr ack;
	  ack.flags = ACK;
	  if(ntohl(hdr->sequence_number) == exp_sequence_number) {
	    exp_sequence_number++;
	  }
	  else {
	    fprintf(stderr,"Unexpected non-ACK in rel_send(), size %d. Acking what we have so far. \n",recv_count);
	  }
	  ack.ack_number = htonl(exp_sequence_number-1);
	  fprintf(stderr,"\rException: Sending ACK - %d for a non-ack packet receivd \n",ntohl(ack.ack_number));
	  sendto(sock, &ack, sizeof(ack), 0,(struct sockaddr*)&otherhost_addr,sizeof(otherhost_addr));
	  
	  } */      
      }
      flag_ack_chked=1;
      
    }//end of while
  }

  if(len==0 && !flag_ack_chked){
    //perror("Problem occcured\n");
    exit(2);
  }
  if(flag_ack_chked){
    while(getCurrentQSize() > 0){
      struct senderQElements* s;
      s = doQDelete();
      free(s);
    }

    start_seq_no = start_seq_no + window_size;
    if(start_seq_no != sequence_number + 1)
      exit(2);
    if(flag_pkt_dropped == 1)
      decrease_window_size();
    else
      increase_window_size();
  }

  sequence_number++;

  /*
  while(1)
  {
    if(close_packet)
    {
      close_retry_attempts++;
      
    }
    
    int sent_time = current_msec();              //start of RTT
    
    //fd_set readset;
    //FD_ZERO(&readset);
    //FD_SET(sock,&readset); 
   //struct timeval t = timeout;
    
    //int ready = select(FD_SETSIZE,&readset,0,0,&t);
    if(ready==0){
      msec_to_timeval(min(5000,2*timeval_to_msec(&timeout)), &timeout);
      fprintf(stderr,"We timed out...hence retransmit.....\n");
      fprintf(stderr,"\rPacket %d with rtt %d timeout %d ms \n",sequence_number, rtt, timeval_to_msec(&timeout));
      sendto(sock, packet, sizeof(struct hw7_hdr)+len, 0,(struct sockaddr*)&otherhost_addr,sizeof(otherhost_addr));
      //send(sock, packet, sizeof(struct hw7_hdr)+len, 0);
      rexmit = 1;
    }
    else if(ready==-1)
    perror("Error while select \n");
  */
    /* Listen for acknowledgement from "OTHER_HOST" after sending */
    //else{
    //char incoming[1400];
    //struct sockaddr_in from_addr;
    //unsigned int addrsize = sizeof(from_addr);
    //int recv_count=recvfrom(sock,incoming,1400,0,(struct sockaddr*)&from_addr,&addrsize);
    //if(recv_count<0) {
    //	perror("Error while receiving packet.");
    //	return;
    //}
      
      /* ACK received is for the latest packet....Alls Well......update RTT for this packet.*/
      //struct hw7_hdr *hdr = (struct hw7_hdr*)incoming;
      /*if(ntohl(hdr->ack_number) == sequence_number) {
	if(!rexmit){
	  //update RTT here
	  ack_time = current_msec();
	  
	  deviation = 0.75*deviation + 0.25*(abs((ack_time - sent_time) - rtt));
	  rtt = (float)ALPHA * rtt + (float)(1 - ALPHA) * (ack_time - sent_time);
	  msec_to_timeval(rtt+4*deviation,&timeout);
	}
	  
	sequence_number++;
	break;
	}*/
      
      /* End of the stream code goes over here */
      /*      else if(ntohl(hdr->ack_number) < sequence_number){
	fprintf(stderr, "Closing gracefully at the Senders side \n");
	break;
	}*/

      
  //}
    //send(sock, packet, sizeof(struct hw7_hdr)+len, 0);
    //sequence_number++;

    //}


}

int rel_socket(int domain, int type, int protocol) {
	sequence_number = 0;
	rtt = 500;
	deviation = 50;
	//timeout.tv_usec = 600000;
	start_seq_no = 0;
	exp_sequence_number = 0;
	window_size = 1;
	timeout.tv_usec = (rtt + 4*deviation) * 1000;
	return socket(domain, type, protocol);
}

struct receiverQElements{
  char rcv_flag;
  int flag_has_data;
  char packet[MAX_PACKET];
  char ack_sent:1;
  int packet_len;
}rlist[WINDOW];

//Returns -1 if not found, otherwise will return the index, which can be used with getNthElement
int isPacketAvailableinQueue(int seq_num)
{
  struct receiverQElements *r;
  r = getNthElement(0);
  if (r->flag_has_data)
    return 0;
  else
    return -1;
}

int rel_recv(int sock, void * buffer, size_t length) {

  if(user == UNKNOWN){
    user = RECEIVER;
  }

  static int first_time = 1;
  if(first_time == 1){
    for(int i=0; i<WINDOW; i++){  //insert WINDOW blank elements in queue
      struct receiverQElements *r;
      r = calloc(1,sizeof(struct receiverQElements));
      r->flag_has_data = 0;
      r->ack_sent = 0;
      r->packet_len = 0;
      if(r==NULL){
	perror("Memory error for Receiver window\n");
	exit(2);
      }
      
      doQInsert(r);
    }
    first_time = 0;

  }
 
  char packet[MAX_PACKET];
  memset(&packet,0,sizeof(packet));
  struct hw7_hdr* hdr=(struct hw7_hdr*)packet;
  
  unsigned int addrlen=sizeof(otherhost_addr);

  struct receiverQElements *r;
  int index;

  while(-1 ==( index = isPacketAvailableinQueue(last_packet_seqnum_sent + 1))){    
    setsockopt(sock, SOL_SOCKET, SO_RCVTIMEO, NULL, 0); //wait indefinitely!!!
    int recv_count = recvfrom(sock, packet, MAX_PACKET, 0, (struct sockaddr*)&otherhost_addr, &addrlen);
    fprintf(stderr, "Got packet %d.......Waiting for packet %d\n", ntohl(hdr->sequence_number), exp_sequence_number);

    if(ntohl(hdr->sequence_number) <= (last_packet_seqnum_sent + WINDOW -1)){
      struct hw7_hdr ack;
      ack.ack_number = hdr->sequence_number;	  
      sendto(sock, &ack, sizeof(ack), 0, (struct sockaddr*)&otherhost_addr, addrlen);
      fprintf(stderr, "Sent ACK#: %d\n", ntohl(hdr->sequence_number));

      struct receiverQElements *r;
      r = NULL;
      if(ntohl(hdr->sequence_number) > (last_packet_seqnum_sent) || last_packet_seqnum_sent == -1){
	r = getNthElement(ntohl(hdr->sequence_number) - (last_packet_seqnum_sent+1));

      }

      if(r == NULL || r->flag_has_data){
	//Retransmit scenario
	fprintf(stderr,"%d Retransmission  detected - ignoring - Sent ACK in above log : %d \n",ntohl(hdr->sequence_number),ntohl(ack.ack_number));
		
      }
      else{
	r->flag_has_data = 1;
	memcpy(r->packet, packet, recv_count);
	r->packet_len = recv_count;
	//store it in window;
      }

    }
  }
  
  if(index!=0){
    perror("index != 0\n");
    exit(2);
  }
  
  r = doQDelete();                       //Returned last packet sequence # will be last_packet_seqnum_sent+1
  hdr = (struct hw7_hdr *)r->packet;
  fprintf(stderr,"Sent packet upwards: %d \n",ntohl(hdr->sequence_number));
  memcpy(buffer, r->packet+sizeof(struct hw7_hdr), r->packet_len-sizeof(struct hw7_hdr));
  last_packet_seqnum_sent = ntohl(hdr->sequence_number);
  int packlen = r->packet_len;
  
  doQInsert(r);
  memset(r,0,sizeof(struct receiverQElements));
  r->rcv_flag = 'R';
  r->flag_has_data = 0;
  
  return packlen - sizeof(struct hw7_hdr);
}


int rel_close(int sock) {
  
  if(user == SENDER)
	rel_send(sock, 0, 0); // send an empty packet to signify end of file
	fprintf(stderr, "Sent EOF. Now closing the connection\n");
	
	/*if(close_retry_attempts<20){
	  rel_recv(sock,0,0);

	  }*/
	//struct timeval t;
        //t.tv_sec = 2;
        //t.tv_usec = 0;
        //setsockopt(sock, SOL_SOCKET, SO_RCVTIMEO, &t, sizeof(t));	
	
	/* Waiting for some time before both Sender and Receiver can shut down i.e for 2 secs*/
	//int close_waitstart = current_msec();
	//while(current_msec() - close_waitstart < 2000) {                
	//rel_recv(sock,0,0);
	  //}
	
	
	close(sock);
}

