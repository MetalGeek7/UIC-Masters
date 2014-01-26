/*
Name: Abhishek Kanchan
Title: C implementation of Circular Queue
College: University of Illinois at Chicago
Course/Semester: CS 450 / Fall 2013
Description: implementation of all the basic functions of a circular queue. Reference from Jude's and Sureshan's nodes
*/

#include <stdio.h>
#include <stdlib.h>

int front=-1, rear=-1, queue_size=301;
char* arr[301];

int isQEmpty()
{
  if(front == -1)
    return 1;
  else
    return 0;
}

int isQFull()
{
  if(front == (rear+1) % queue_size)
    return 1;
  else
    return 0;
}

int doQInsert(char* element)
{
  if(isQFull()){
    fprintf(stderr,"Queue is full\n");
    exit(0);
    return 0;
  }
  if(front == -1){
    front=0;
    rear=0;
  } 
  arr[rear]=element;
  rear = (rear+1) % queue_size;

  return 1;
}

char* doQDelete()
{
  char* ret;
  if(isQEmpty()){
    fprintf(stderr,"Queue is empty\n");
    return NULL;
  }
  ret = arr[front];
  front = (front+1) % queue_size;
  
  if(front == rear){
    front = rear = -1;
  }
  
  return ret;
}

int getCurrentQSize()
{
  if(isQEmpty()){
    fprintf(stderr,"current queue size => %d\n",0);
    return 0;
  }
  int i = front,count=0;
  while(1){
    if(i == rear){
      fprintf(stderr,"current queue size => %d\n",count);
      break;
    }
    count++;
    i = (i + 1) % queue_size;
  }
  return count;
}

char* getNthElement(int n)
{
  if(isQEmpty()){
    fprintf(stderr,"getNthelement() : current queue size --> %d\n",0);
    exit(0);
    return NULL;
  }
  int f = front;
  int count = 0;
  char* elem = NULL;
  while(1){
    if(f == rear){
      fprintf(stderr,"getNthelement(): OutofBound - current queue size --> %d\n",count);
      exit(0);
      break;
    }
    if(count == n){
      elem = arr[f];
      break;
    }
    count++;
    f = (f+1) % queue_size; 
    
  }  
  return elem;
}

/*void printQ()
{
  if(!isQEmpty()){
    
    int curr_size = getCurrentQSize();
    for(int i=0; i<curr_size; i++){
      if(i!=0)
	printf("-->");
      char tmp = *arr;
      
      printf("%c", *tmp);
      arr+1;
    }
        
  }
  else
    printf("Circular Queue is empty\n");
    }*/

 /*
int main()
{
  printf("Starting Circular queue standalone testing\n");
  if(isQEmpty())
    printf("Queue is empty\n");
  
  doQInsert(2);
  doQInsert(21);
  doQInsert(220);
  doQInsert(2301);
  doQInsert(312200);
  
  printf("Current size of the Circular Queue is %d\n", getCurrentQSize());
  //printQ();
  
  doQDelete();
  doQDelete();
  
  printf("Current size of the Circular Queue after deletions is %d\n", getCurrentQSize());
  //printQ();
}

 */

