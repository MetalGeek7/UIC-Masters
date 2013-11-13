/* 

Author: Abhishek Kanchan (akanch4@uic.edu)
University: UIC
Degree: Masters in Computer Science
Year: 2013

Minimal TCP client example. The client connects to port 8080 on the 
local machine and prints up to 255 received characters to the console, 
then exits. To test it, try running a minimal server like this on your
local machine:

echo "Here is a message" | nc -l -6 8080

*/

#include <stdio.h>
#include <sys/socket.h>
#include <netdb.h>
#include <string.h>
#include <errno.h>

#define MAXGETREQUESTSIZE 256
#define MAXURLSIZE 100

int main(int argc, char **argv)
{

  int sock_fd, ssd;
  int bytesrecvd = 0;
  char getrequest[MAXGETREQUESTSIZE];
  char URL[MAXURLSIZE]={(char)0};
  char recvBuffer[1024*1024];
  struct addrinfo hints;
  struct addrinfo *result, *rp;
  struct sockaddr_in serverAddr;
  hints.ai_family = AF_UNSPEC;             //AF_INET or AF_INET6 to force version                                                                                                                            
  hints.ai_socktype = SOCK_STREAM;

  memset(&hints,0,sizeof(struct addrinfo));
  memset(&serverAddr, 0, sizeof(serverAddr));
  serverAddr.sin_family = AF_INET;
  serverAddr.sin_port = htons(80);         //The htons socket function translates a short integer from host byte order to network byte order.                                                                

  if(argc < 2)
    {
      printf("Error.......no argument passed for program execution \n");
      return 1;
    }
  else
    {

      strcpy(URL, argv[1]);

      char *resource = NULL;
      char host[100];
      char *temp = NULL;
   
      int scanned = sscanf(argv[1],"http://%[^/]/%s",host, resource);
      //sscanf(URL, "http://%[^/]", host);                           
      resource = strchr(URL, '/');
      resource = strchr(resource + 2 , '/');
      if(resource == NULL)
	resource = "/index.html";
      printf("Resource to be found is: %s \n", resource);
      printf("Host is: %s \n", host);

      int ip_lookup = getaddrinfo(host, "80", &hints, &result);

      /*Socket connection code taken from skeleton hw1.c to talk to host */
      for (rp = result; rp != NULL; rp = rp->ai_next) {
	sock_fd = socket(rp->ai_family, rp->ai_socktype,
			 rp->ai_protocol);
	if (sock_fd == -1)
	  continue;

	if (connect(sock_fd, rp->ai_addr, rp->ai_addrlen) != -1)
	  break; /* Success */

	close(sock_fd);
      }

      printf("Connection successful \n");

      /* Create the HTTP request and send */
      //sprintf(getrequest, "GET %s HTTP/1.0\r\nHOST:%s \r\n\r\n", resource, host);                                                                                                                            
      sprintf(getrequest, "GET %s HTTP/1.0 \r\n\r\n", resource);
      printf("Request generated is: %s ", getrequest);

      send(sock_fd, getrequest, strlen(getrequest), 0);

      /* Receive the Response */
      bytesrecvd = 0;
      do{
	ssd = recv(sock_fd, &recvBuffer[bytesrecvd], 256, 0);
	if(ssd < 0)
	  {
	    printf("Error while receiving response %d", ssd);
	    exit(1);
	  }
	else if(ssd == 0)
	  printf("Received total of %d bytes \n", bytesrecvd);

	bytesrecvd+=ssd;
      }
      while(ssd > 0);

      printf("%s \n", recvBuffer);
      
      
      /* Write the Response to a file */                                                                                                                                                                    
      int f_size=0;
      FILE *file = NULL;
      char *end_header;
      char *filename;
      char fname[100];

      end_header = strstr(recvBuffer,"\r\n\r\n");               //Find for end of HTTP header
      end_header+=4;
      f_size = bytesrecvd - (end_header - recvBuffer);
      //printf("Size = %d",f_size);
      
      filename = strrchr(resource, '/');
      //printf("Filename : %s and its len: %d", filename, strlen(filename));
      if(filename && strlen(filename+1))
      strcpy(fname,filename+1);
      else
      strcpy(fname,"index.html");
      
      file = fopen(fname,"w+");
      fwrite(end_header,1,f_size,file);
      fclose(file);
      
      int HTTP_ResponseCode = 0;
      char dummy_str[10];
      sscanf(recvBuffer,"%s %d", dummy_str, &HTTP_ResponseCode);
      if(HTTP_ResponseCode == 404)
      {
	perror("HTTP 404:File not found \n");
      }
      else if(HTTP_ResponseCode == 200)
	printf("Name of the downloaded file is: %s \n", fname);
      else if(HTTP_ResponseCode == 400)
	perror("HTTP 400:Bad Request \n");
      
      freeaddrinfo(result);
      shutdown(sock_fd, SHUT_RDWR);
      
    }

  
  return 0;
}
