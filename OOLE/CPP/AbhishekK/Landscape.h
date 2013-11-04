#ifndef Landscape_H
#define Landscape_H

#include<iostream>
#include"String.h"
#include"Painting.h"
using namespace std;

class Landscape: public Painting
{
  String pcountry;

 public:
  Landscape();
  Landscape(const String &title, int height, int width, const String& _country);
  Landscape(const Landscape& );
  
  virtual void displayPaintAttr() const;
  virtual Landscape& vcopy() const;
};

#endif
