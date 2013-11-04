#ifndef LIST_H
#define LIST_H

#include<iostream>
using namespace std;

template <class Item>
class LinkedList
{
   class Node
   {
     Item& data;
     Node* next;
     
     public:
     Node(Item& item):data(item) { next = NULL;}
     Item& nodeVal();
     Node* getNext(){ return next;}                                //Getter for next 
     void setNext(Node* n){ next = n;}                             //Setter for next
     Item& getData(){ return data;}
     
   };  
   
   Node* head;                                 //pointer to head of linked list
   unsigned int size;

 public:
   LinkedList(){head = NULL;}                 //Default constructor for LinkedList
   LinkedList(const LinkedList& list);        
   void add(Item&);
   Item* search(const Item&);
   bool remove(const Item&);
   bool removeAll();
   //friend ostream& operator<<(ostream &os, const LinkedList<Item>& list);
   void displayList() const;
   ~LinkedList();
};

/*template <class Item>
  ostream& operator<<(ostream &os, const LinkedList<Item>& list);*/
#endif
