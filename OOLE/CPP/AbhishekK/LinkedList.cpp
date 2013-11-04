#ifndef LIST_CPP
#define LIST_CPP

#include<iostream>
#include"LinkedList.h"
using namespace std;

/*
  Adds a new Node of type Item to the end of the list 
*/
template <class Item>
void LinkedList<Item>::add(Item& item)
{
  Node* n = new Node(item);
  if(head == NULL)                           //Initially empty list, add item to the head of list
    head = n;
  else
  {  
    Node* ptr = head;
    while(ptr->getNext()!=NULL)
      ptr = ptr->getNext();
      //ptr->setNext() = ptr->getNext();           //Equivalent to ptr = ptr->next
      
    ptr->setNext(n);                             
  }
  //cout>>"element added">>ptr->getNext()->data;
}

template <class Item>
Item* LinkedList<Item>::search(const Item& item)
{
  Node* ptr = head;
  while(ptr!=NULL)
  { 
    if(ptr->getData() == item)
      break;
    ptr = ptr->getNext();
  }  
  
  if(ptr==NULL)
  {  
    cout<<"Item not found"<<endl;
    return NULL;
  } 
  else
  {
    cout<<"Element found"<<endl;
    return &(ptr->getData());                       //Returns the Node which holds the argument item
  }    
}

template <class Item>
bool LinkedList<Item>::remove(const Item& item)
{
  Node* currNode; 
  Node* prevNode;
  currNode = head;
  prevNode = NULL;

  if(head->getData() == item)             //Item to be removed is the first item in the list
  {  
    head = head->getNext();
    delete currNode;
    return true;
  }
  else
  {  
    while(currNode!=NULL && !(currNode->getData()==item))
    {  
      prevNode = currNode;
      currNode = currNode->getNext();
    }
    
    if(currNode!=NULL)              //Item found in middle or end of list
    {
      prevNode->setNext(currNode->getNext());
      delete currNode;
      return true;
    }
    else
    {  
      cout<<"Element not present in the List ";
      return false;
    }  
  }  
}

template <class Item>
void LinkedList<Item>::displayList() const
{
  Node* curr = head;
  while(curr)
  {
    //curr->getData().display();
    cout<<"|";
    cout<<curr->getData()<<"-->";
    curr = curr->getNext();
  }    
  cout<<"NULL";
}

/*template <class Item>
ostream& operator<<(ostream &os, const LinkedList<Item>& list)
{
  list.displayList();
  return cout;
}*/

/* Yet to be Tested

*/

template <class Item>
bool LinkedList<Item>::removeAll() 
{
  Node* ptr = head;
  while(ptr!=NULL) 
  {  
    Node* temp = ptr;
    ptr = ptr->getNext();
    delete temp;
  }
  if(ptr==NULL)
    return true;
  else
    return false;
}

template <class Item>
LinkedList<Item>::LinkedList(const LinkedList& list)
{
  Node* curr = NULL;
  head = NULL;  
  size=0;
  curr = list.head;
  while(curr!=NULL)
  {
    this->add(curr->getData());
    curr = curr->getNext();
  }
  size = list.size;
}

template <class Item>
LinkedList<Item>::~LinkedList()
{
  Node* ptr = head;
  while(ptr!=NULL) 
  {  
    Node* temp = ptr;
    ptr = ptr->getNext();
    delete temp;
  }  
}

#endif



