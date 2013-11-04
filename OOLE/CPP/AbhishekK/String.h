#ifndef STRING_H
#define STRING_H

#include<iostream>
using namespace std;

class String
{
  char* arr;
  int size;
  
 public:
  String(){size=1; arr = new char[size]; arr[0] = '\0';}    //Default constructor
  String(const char* str);
  String(const String& str);                                  //Copy Constructor
  void printString(){cout<<arr<<endl;}
  void inputStr();
  int getSize();

  friend String operator+(const String& str1, const String& str2);                  //Overloaded '+' for string concatenation
  friend String operator+(const String& str1, char *str2);
  String& operator+=(const String& str);
  String& operator+=(char *str);
  friend ostream& operator<<(ostream &os, const String& str);
  friend istream& operator>>(istream &is, String& str);
  bool operator==(const String& str);                                               //Overloaded '==' for string comparison
  //bool operator==(const char* str);

  bool operator!=(const String& str);
 
  String& operator=(const String& str); 

  //operator char*();
  
  ~String();  
};
    
#endif
