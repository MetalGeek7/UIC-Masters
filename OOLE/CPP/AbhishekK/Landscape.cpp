#include<iostream>
#include"Landscape.h"

using namespace std;

Landscape::Landscape()
{
  //Calls default constructor of Painting class implicitly
  pcountry = "";
}

Landscape::Landscape(const String &title, int height, int width, const String &_country): Painting(title, height, width),pcountry(_country) 
{
  //pcountry = _country;  
}

Landscape::Landscape(const Landscape& _l): Painting(_l)
{
  pcountry = _l.pcountry;
}

void Landscape::displayPaintAttr() const
{
  cout<<"Country:"<<pcountry;
  cout<<"|";
}

Landscape& Landscape::vcopy() const
{
  return *(new Landscape(*this)); 
}
