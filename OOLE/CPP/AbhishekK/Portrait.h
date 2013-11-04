#ifndef Portrait_H
#define Portrait_H

#include<iostream>
#include<vector>
#include"String.h"
#include"Painting.h"
using namespace std;

class Portrait: public Painting
{
  vector<String> listofPpl;
  int numofPpl;
  
  public:
  Portrait();
  Portrait(const String &title, int height, int width, int _numPpl, vector<String> _listPpl);
  Portrait(const Portrait& );
  void displayPeople() const;
  virtual void displayPaintAttr() const;
  virtual Portrait& vcopy() const;

};

#endif
