#ifndef Artist_H
#define Artist_H

#include<iostream>
#include"String.h"
#include"LinkedList.h"
#include"LinkedList.cpp"
#include"Painting.h"
using namespace std;

class Painting;
class Artist
{
  String _AFName;
  String _ALName;
  LinkedList<Painting> pList;
  int numofpaintings; 
  
 public:
  Artist(const String, const String);
  Artist(const Artist& artist);
  bool operator==(const Artist&);
  bool operator!=(const Artist&);
  friend ostream& operator<<(ostream &os, const Artist&);
  void addPainting(Painting& );
  void removePaintings();
  Painting* searchPainting(const Painting& );
  void removeP(Painting& );
  void displayA();

  String getFName();
  String getLName();
  
};

ostream& operator<<(ostream &os, const Artist& artist);
#endif

