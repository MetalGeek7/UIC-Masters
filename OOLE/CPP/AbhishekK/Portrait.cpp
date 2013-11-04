#include<iostream>
#include"Portrait.h"
#include"String.h"

using namespace std;

Portrait::Portrait()
{
  //Calls default constructor of Painting class implicitly
  numofPpl = 0;
}

Portrait::Portrait(const String& title, int height, int width, int _numPpl, vector<String> _listPpl): Painting(title, height, width)
{
  numofPpl = _numPpl;
  for(int i=0; i<_listPpl.size(); i++)
    listofPpl.push_back(_listPpl.at(i));
}

Portrait::Portrait(const Portrait& _portrait): Painting(_portrait)
{
  numofPpl = _portrait.numofPpl;
  
  for(int i=0; i<_portrait.listofPpl.size(); i++)
  {
    listofPpl.push_back(_portrait.listofPpl.at(i));
  }    
}

void Portrait::displayPeople() const
{
  if(listofPpl.size()!=0)
  {  
    cout<<", Names of people in Portrait:"; 
   for(int i=0; i<listofPpl.size(); i++)
   {
     cout<<listofPpl.at(i)<<",";
   }
  }
  else
    cout<<"NIL,";

  cout<<"|";
}

void Portrait::displayPaintAttr() const
{
  cout<<"No. of People:"<<numofPpl;
  displayPeople();
}

Portrait& Portrait::vcopy() const
{
  return *(new Portrait(*this)); 
} 

