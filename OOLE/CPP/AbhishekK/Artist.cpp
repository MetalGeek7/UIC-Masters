#include<iostream>
#include"LinkedList.h"
#include"String.h"
#include"Artist.h"

using namespace std;

Artist::Artist(const String firstName, const String lastName): _AFName(firstName), _ALName(lastName)
{
  _AFName = firstName;
  _ALName = lastName;  
}

Artist::Artist(const Artist& artist):pList(artist.pList)
{
  _AFName = artist._AFName;
  _ALName = artist._ALName;  
}

bool Artist::operator==(const Artist& artist)
{
  if(_AFName==artist._AFName && _ALName==artist._ALName)
    return true;
  else
    return false;
}

bool Artist::operator!=(const Artist& artist)
{
  return !(operator==(artist));
}

ostream& operator<<(ostream &os, const Artist& artist)
{
  cout<<"Artist First Name:"<<artist._AFName<<" Artist Last Name:"<<artist._ALName<<endl;
  artist.pList.displayList();
}

void Artist::addPainting(Painting& paint)
{
  pList.add(paint);
}

void Artist::removePaintings()
{
  pList.removeAll();  
}

void Artist::removeP(Painting& paint)
{
  pList.remove(paint);
}

Painting* Artist::searchPainting(const Painting& paint)
{
  Painting* result = pList.search(paint);
  return result;
}

void Artist::displayA()
{
  cout<<"Artist First Name:"<<_AFName<<" Artist Last Name:"<<_ALName<<endl;
  pList.displayList();
}

String Artist::getFName()
{
  return _AFName;  
}

String Artist::getLName()
{
  return _ALName;  
}

