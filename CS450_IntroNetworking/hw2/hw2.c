/*
  
  Name: Abhishek Kanchan
  University: UIC
  Course: CS 450 - Introduction to Networking
  Description: This is a multithreaded server program 
  Date: 9/9/2013
*/

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/stat.h>
#include <netinet/in.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <sys/wait.h>
#include <signal.h>
#include <pthread.h>
#include <dirent.h>

#define DIR_SIZE 100
#define PATH_SIZE 200
#define FILEN_SIZE 100 

struct encaps{
  int sockfd_conn;
  char directory_name[DIR_SIZE];
};

void *thread_handler(void* ptr);
void Request_handler(int, char*);

int main(int argc, char* argv[])                       //Takes a port no and directory as the program input: 8080 WWW
{
  
  int sockfd, incom_fd;
  struct addrinfo hints, *servinfo, *p;
  struct sockaddr_storage peer_addr;                 // connector's address information
  struct sockaddr_in server_addr;
  struct encaps* e;
  socklen_t p_size;
  int rv;
  int status = 0;
  int yes=1;

  int port;
  char dir[30] = {'\0'};
  char* endptr;
  int base = 10;
  
  memset(&hints, 0, sizeof(struct addrinfo));
  hints.ai_family = AF_UNSPEC;
  hints.ai_socktype = SOCK_STREAM;
  hints.ai_flags = AI_PASSIVE;
  
  port = strtol(argv[1], &endptr, base);    //converts input char[] arg to integer
  //argv[2] can be WWW or WWW/something
  strcpy(dir, argv[2]);                     
  //printf("Input parameter is %d and %s \n",port, dir);

  
  //Create a listening socket connection
  sockfd = socket(AF_INET, SOCK_STREAM, 0);
  if(sockfd < 0)
  {
    perror("TCP Socket connection failed\n");
    exit(2);
  }  

  if (setsockopt(sockfd, SOL_SOCKET, SO_REUSEADDR, &yes,                     
		 sizeof(int)) == -1)                               //Error while setsockopt for sockfd 
  {
    perror("setsockopt error");
    exit(2);
  }
  server_addr.sin_family = AF_INET;
  server_addr.sin_port = htons(port);
  server_addr.sin_addr.s_addr = INADDR_ANY;                        //Optimize to use getaddrinfo()

  status = bind(sockfd, (struct sockaddr*)&server_addr, sizeof(server_addr));
  if(status < 0)
  {
    perror("Could not bind to server port \n");
    exit(2);
  }
  else
    printf("Successfully bound to port: %d \n",port);

  //Listen on port for incoming client requests
  int listen_stat;
  while(1)
  {
    p_size = sizeof peer_addr;
    
    listen_stat = listen(sockfd,0);
    if(listen_stat == -1)
    {
      perror("Listening failed on port");
      exit(2);	
    }  
    incom_fd = accept(sockfd, (struct sockaddr* )&peer_addr, &p_size);
    
    if (incom_fd == -1) {
      perror("accept failed");
      continue;
    }
    
    e = (struct encaps*)malloc(sizeof(struct encaps));    
    e->sockfd_conn = incom_fd;
    strncpy(e->directory_name,dir,strlen(dir));

    //create a new thread and pass the incoming request
    int pt=0;
    int thread_count=0;
    pthread_t threads[100];
    //pthread_attr_t thread_attr;
    pt = pthread_create(&threads[thread_count], NULL, thread_handler, (void*) e);                // e is passed as the sole arg to thread_handler
    thread_count = (thread_count + 1) % 100; 
    
    
  }


  
  return 0;
}


void *thread_handler(void *ptr)
{
  //printf("Message passed to thread %s \n",(char*)ptr);
  struct encaps* msg;
  
  msg = (struct encaps*)ptr; 
  Request_handler(msg->sockfd_conn, msg->directory_name);

  //Request_handler(ptr->sockfd_conn, ptr->directory_name);
  free(msg);
  return NULL;
}


void Request_handler(int sockfd_new, char* dir_name)
{
  char filename[FILEN_SIZE];
  char fname[FILEN_SIZE];
  char filepath[PATH_SIZE];                   //Path of the resource requested by the client within dir_name(eg. WWW or WWW/something)
  FILE *fp;
  char recvBuffer[1024*1024];
  char sendBuffer[1024*1024*2];
  int status = 0;
  int file_status = 0;
  int fstat = 0;
  int bytes_recv = 0;
  int offset = 0;
  int flag_dir = 0;

  DIR *pdir = NULL;
  struct dirent *pentr = NULL;
  struct stat fst;
  /* 
  http://cs450.uicbits.net/cs450/cs450.html
  
  Parse this for 
  GET /cs450/cs450.html HTTP/1.1[CRLF]
  Host: cs450.uicbits.net[CRLF]

  */
  
  do{
    status = recv(sockfd_new, &recvBuffer[bytes_recv], 256,0);
      if(status < 0)
      {
	perror("Receive error");
	exit(1);
      }
  
      bytes_recv+=status;
  }while(status < 0);
  
  printf("Received %d bytes \n", bytes_recv);
  printf("Request for: \n %s\n",recvBuffer);
  sscanf(recvBuffer, "GET %s", filepath);                          //Will read input request from GET until the first whitespace is found i.e blank, newline or tab  
  sprintf(filename, "./%s%s" ,dir_name, filepath);
  
  file_status = stat(filename, &fst);                              //Using stat-2 
  if(file_status == 0)
  {
    switch(fst.st_mode & S_IFMT)
    {
    default: printf("Error:Unknown file mode \n");
                  break;  
		  
    case S_IFDIR: strcat(filename,"index.html");
                  flag_dir = 1;
                  break;
		  
    case S_IFREG: //do nothing...serve the file
                  break;

    }  


  }
  else
  {
    //Should be Error 404: Not Found
    sprintf(sendBuffer, "HTTP/1.0  404 Not Found\r\n\r\n");
    strcat(sendBuffer, "<html><head><title>404 Not Found</title></head><body><h1>Not Found</h1><p>The requested URL was not found on this server.</p></body></html>");
    offset = strlen(sendBuffer);
    printf("Sent response: %s \n", sendBuffer);
	   
  }
  
  //Create the response buffer and write it to a file
  char* response;
  fp = fopen(filename, "r");
    
  if(fp == NULL && flag_dir == 0)
  {
    //Should be Error 404: Not Found
    sprintf(sendBuffer, "HTTP/1.0  404 Not Found\r\n\r\n");
    strcat(sendBuffer, "<html><head><title>404 Not Found</title></head><body><h1>Not Found</h1><p>The requested URL was not found on this server.</p></body></html>");
    offset = strlen(sendBuffer);
    printf("Sent response: %s \n", sendBuffer);
  }
  else
  {
    sprintf(sendBuffer, "HTTP/1.0 200 Ok\r\n");
    
    if(fp == NULL && flag_dir == 1)               //Probably not an empty directory
    {
      char temp_f[100];
      sprintf(temp_f, "./%s" ,dir_name);
      pdir = opendir(temp_f);
      if(pdir == NULL)
      {
	  printf("Error while Listing contents of directory..\n");
	  exit(1);
      }
      else
      {
	//sprintf(sendBuffer, "HTTP/1.0 200 Ok\r\n");
	strcat(sendBuffer, "<html><title>Directory Listing</title>");
	strcat(sendBuffer, "<body><h2>Directory Listing</h2><hr><ul>");
	while((pentr = readdir(pdir)) != NULL)
	{
	  char temp_dir[30] = {'\0'};
	  printf("%s \n",pentr->d_name);
	  sprintf(temp_dir, "<li><a href=%s>%s</a>", pentr->d_name, pentr->d_name);
	  strcat(sendBuffer, temp_dir);
	}
	strcat(sendBuffer,"</ul></hr></body></html>\r\n");
	offset = strlen(sendBuffer);
	closedir(pdir);
      }


    }
    
    char* ext, *temp;
    temp = filename+1;
    do{
      ext = temp;
      temp = strchr(temp+1, '.');      
    }while(temp != '\0');
    
    //ext = strchr(filename, '.');
    //sscanf(filename,".%s",ext);
    
    if(*ext == '.')
    {
      ext++;
      if(strcmp(ext,"html")==0)
      {
	strcat(sendBuffer,"Content-Type: ");
	strcat(sendBuffer,"text/html\r\n");
      }
      if(strcmp(ext,"jpg")==0 || strcmp(ext,"jpeg")==0)
      {
	strcat(sendBuffer,"Content-Type: ");
	strcat(sendBuffer,"image/jpeg\r\n");
      }
      if(strcmp(ext,"gif")==0)
      {
	strcat(sendBuffer,"Content-Type: ");
	strcat(sendBuffer,"image/gif\r\n");
      }
      if(strcmp(ext,"png")==0)
      {
	strcat(sendBuffer,"Content-Type: ");
	strcat(sendBuffer,"image/png\r\n");
      }
      if(strcmp(ext,"pdf")==0)
      {
	strcat(sendBuffer,"Content-Type: ");
	strcat(sendBuffer,"application/pdf\r\n");
      }
      if(strcmp(ext,"ico")==0)
      {
	strcat(sendBuffer,"Content-Type: ");
	strcat(sendBuffer,"image/x-icon\r\n");
      }




    }
    
    strcat(sendBuffer,"\r\n");
    printf("<<<HTTP Response>>> \n");
    printf("%s", sendBuffer);
    offset = strlen(sendBuffer);
    
    do{
      fstat = fread(&sendBuffer[offset],1,1,fp);
      offset+=1;
    }while(fstat!=0);

    offset--;
    fclose(fp);

  }  

  send(sockfd_new, sendBuffer, offset, 0);
  shutdown(sockfd_new, SHUT_RDWR);
  return;
}








