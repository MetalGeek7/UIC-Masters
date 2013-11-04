#ifndef Still_H
#define Still_H

#include<iostream>
#include"String.h"
#include"Painting.h"
using namespace std;

class Stilllife: public Painting
{
  int colortype;

public:
  Stilllife(const String &title, int height, int width, int _ctype);
  Stilllife(const Stilllife& );
  virtual void displayPaintAttr() const;
  virtual Stilllife& vcopy() const;

};
#endif
