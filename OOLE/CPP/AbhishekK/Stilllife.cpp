#include<iostream>
#include"Stilllife.h"
using namespace std;

Stilllife::Stilllife(const String &title, int height, int width, int _ctype):Painting(title,height,width),colortype(_ctype)
{

}

Stilllife::Stilllife(const Stilllife& _s): Painting(_s)
{
  colortype = _s.colortype;
}

void Stilllife::displayPaintAttr() const
{
  if(colortype==0)
    cout<<"Color Type: Oil Based Medium|";
  else if(colortype==1)
    cout<<"Color Type: Water Colors|";
}

Stilllife& Stilllife::vcopy() const
{
  return *(new Stilllife(*this)); 
}
