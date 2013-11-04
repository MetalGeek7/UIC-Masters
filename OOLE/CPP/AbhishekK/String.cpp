#include<iostream>
#include<cstring>
#include<cstdio>
#include"String.h"
using namespace std;

/*
  This function calculates the length of the character array up to the null character 
 */
int calculateStringSize(char* str)
{
  int length=0;
  while(*str)                     //Up to null character of input string
  {
    length++;
    ++str;
  }
  return length;
}

String::String(const char* inputstr)
{
  size = strlen(inputstr)+1;
  arr = new char[size];
  strncpy(arr,inputstr,strlen(inputstr));
  arr[size-1] = '\0';   
}

String::String(const String& str1)
{
  size = str1.size;
  arr = new char[size];
  strncpy(arr, str1.arr, str1.size-1);
  arr[size-1] = '\0';
}

void String::inputStr()
{
  cout<<"Enter the string\n";
  gets(arr);
}

int String::getSize()
{
  return this->size;
}


String operator+(const String& str1, const String& str2)
{
  String str;
  delete[] str.arr;
  str.arr = new char[str1.size + str2.size -1];
  str.size = str1.size + str2.size - 1;
  strncpy(str.arr, str1.arr, strlen(str1.arr));              //Copies the string excluding the null
  str.arr[strlen(str1.arr)] = '\0';
  strncat(str.arr, str2.arr, strlen(str2.arr));
  return str;
}

String operator+(const String& str1, char *str2)
{
  String str;
  delete[] str.arr;
  str.arr = new char[str1.size + strlen(str2)];
  str.size = str1.size + strlen(str2);
  strncpy(str.arr, str1.arr, strlen(str1.arr));
  str.arr[strlen(str1.arr)] = '\0';
  strncat(str.arr, str2, strlen(str2));
  return str;
}

String& String::operator+=(const String& str)
{
  String result(*this + str);                                //Calls overloaded + operator for String type
  *this = result;
  return *this;
}

String& String::operator+=(char *str)
{
  String result(*this + str);
  *this = result;
  return *this;
}

String& String::operator=(const String& str1)
{
  size = str1.size;
  delete[] arr;                                              //Deallocates old character array
  arr = new char[size];
  strncpy(arr, str1.arr, strlen(str1.arr));
  arr[size-1] = '\0';
  return *this; 
}

/*String& String::operator=(char *str)
{
  size = strlen(str)+1;
  delete[] arr;
  arr = new char[size];
  strncpy(arr, str, strlen(str));
  arr[size-1] = '\0';
  return *this;
}*/

ostream& operator<<(ostream &os, const String& str1)
{
  cout<<str1.arr;
}

istream& operator>>(istream &is, String& str1)
{
  char temp[100];
  cin>>temp;
  str1 = temp;
  str1.size = strlen(str1.arr)+1;
}

bool String::operator==(const String& str1)
{
  
  if( this->size == str1.size && strncmp(this->arr, str1.arr, str1.size-1)==0)
    return true;
  else
    return false;
}

/*bool String::operator==(const char *str1)
{
  if( (this->size -1) == strlen(str1) && strncmp(this->arr, str1, strlen(str1))==0)
    return true;
  else
    return false;
}*/

bool String::operator!=(const String& str)
{
  return !(operator==(str));
}

/*bool String::operator!=(const char *str1)
{
  return !(operator==(str1));
}*/

 /*String::operator char*()                            //Outward conversion from String type to char*
{
  char* dest;
  dest = new char[size];
  strncpy(dest, arr, size);
  return dest;
}*/

String::~String()
{
  delete[] arr;
}
