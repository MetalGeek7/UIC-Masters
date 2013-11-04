#ifndef Painting_H
#define Painting_H

#include<iostream>
#include"String.h"
using namespace std;

class Artist;
class Painting
{
  
 protected:
  String title;  
  int height;
  int width;
  static int pid;
  int id;
  
 public:
  Painting();                                                //Default constructor
  Painting(const String&, int, int);                          //Parametrized constructor
  Painting(int);
  Painting(const Painting& );                                //Copy constructor doing a deep copy
  virtual void displayPaintAttr() const;
  friend ostream& operator<<(ostream &os, const Painting&);
  
  virtual Painting& vcopy() const;
  bool operator==(const Painting&);
  virtual ~Painting();
};  

ostream& operator<<(ostream &os, const Painting& paint);
#endif
