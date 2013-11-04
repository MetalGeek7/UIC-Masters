#include<iostream>
#include"Painting.h"
#include"String.h"
using namespace std;

int Painting::pid=0;

Painting::Painting()
{
  title ="";
  height = 0;
  width = 0;
  pid++;
  id = pid;
}

Painting::Painting(const String& _title, int _height, int _width)
{
  title = _title;
  height = _height;
  width = _width;
  pid++;
  id = pid;
}

Painting::Painting(int _id)
{
  id = _id;
}

Painting::Painting(const Painting& paint1)
{
  //cout<<"Calling Copy constructor";
  // String _newTitle(paint1.title);             
  
  title = paint1.title;
  height = paint1.height;
  width = paint1.width;
  pid++;
}

void Painting::displayPaintAttr() const
{
  //Do nothing  
}

ostream& operator<<(ostream &os, const Painting& paint)
{
  cout<<"Title:"<<paint.title<<" , Height:"<<paint.height<<" , Width:"<<paint.width<<" , Paint ID:"<<paint.id<<" , ";
  paint.displayPaintAttr();
  cout<<endl;
  return cout;
}

bool Painting::operator==(const Painting& _paint1)
{
  if(id == _paint1.id)
    return true;
  else
    return false;
}

Painting& Painting::vcopy() const
{
    return *(new Painting(*this));
}

Painting::~Painting()
{


}

