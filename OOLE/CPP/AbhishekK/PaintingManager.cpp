#include<iostream>
#include<cstring>
#include<string>
#include<vector>

#include"String.h"
#include"Artist.h"
#include"LinkedList.h"
#include"Painting.h"
#include"Portrait.h"
#include"Landscape.h"
#include"Stilllife.h"
using namespace std;

int main()
{
  char c;
  vector<Artist*> artistVec;
  Artist* a;

  while(1)
  {
    cout<<"Enter from one of the choices below: "<<endl;
    cout<<"(a) --> Add artist"<<endl;
    cout<<"(l) --> List all Paintings"<<endl;
    cout<<"(p) --> Add a new Painting"<<endl;
    cout<<"(d) --> Delete all paintings for an Artist"<<endl;
    cout<<"(r) --> Remove a Painting"<<endl;
    cout<<"(c) --> Clone an Artist"<<endl;
    cout<<"(q) --> QUIT"<<endl;
    cin>>c;
  
    switch(c){
    
    case 'a': {
      String fName;
      String lName;
      cout<<"Enter Artist first name"<<endl;
      cin>>fName;
      cout<<"Enter Artist last name"<<endl;
      cin>>lName;
    
      a = new Artist(fName,lName);
      artistVec.push_back(a);
      break;
    }

    case 'p': {
      char pType;
      String title;
      int height,width;
      Painting* p;
      
      String artistFname;
      String artistLname;
      cout<<"Enter Artist first name.."<<endl;
      cin>>artistFname;
      cout<<"Enter Artist last name.."<<endl;
      cin>>artistLname;
      int index;
      
      for(int i=0; i<artistVec.size(); i++)                            //Search Artist
      {	
	if(artistVec.at(i)->getFName()==artistFname && artistVec.at(i)->getLName()==artistLname)
	{ 
	  cout<<"Artist found in the system.."<<endl;
	  index = i;
	  break;
	}
	else
	{
	  //Create a new Artist in the System
	  a = new Artist(artistFname,artistLname);
	  artistVec.push_back(a);
	  //cout<<"Artist not present in the system!!!"<<endl;
	}    
      }
      cout<<"Which kind of Painting do you want to add? "<<endl;
      cout<<"(p):Portrait"<<endl;
      cout<<"(l):Landscape"<<endl;
      cout<<"(s):StillLife"<<endl;
      cin>>pType;

      cout<<"Enter Title"<<endl;
      cin>>title;
      cout<<"Enter Height"<<endl;
      cin>>height;
      cout<<"Enter Width"<<endl;
      cin>>width;
	
      if(pType=='p')                                              //Add Portrait Type
      {
	int numofPeople;
	vector<String> listofPeople;

	cout<<"Enter no. of People in Portrait"<<endl;
	cin>>numofPeople;
	
	cout<<"Enter names of people if known"<<endl;
	String temp;
	while(true)
	{
	  char response;
	  cout<<"Enter name of the person: "<<endl;
	  cin>>temp;
	  listofPeople.push_back(temp);
	  cout<<"Do you want to add more people? (Y/N)"<<endl;
	  cin>>response;
	  if(response=='Y' || response=='y')
	    continue;
	  else if(response=='N' || response=='n')
	    break;
	  else
	    cout<<"Not a Valid Response"<<endl;
	}  
	
	p = new Portrait(title,height,width,numofPeople,listofPeople);
	cout<<*p<<endl;
	cout<<*artistVec.at(index)<<endl;
	artistVec.at(index)->addPainting(*p);
      }  
      else if(pType=='l')                                        //Add Landscape Type
      {
	String country;
	cout<<"Enter country of the Landscape painting"<<endl;
	cin>>country;
	
	p = new Landscape(title,height,width,country);
	cout<<*p<<endl;
	artistVec.at(index)->addPainting(*p);
      }
      else if(pType=='s')                                        //Add StillLife Type
      {
	String str;
	int medium;
	cout<<"Medium of painting(type oil or water):";
	cin>>str;
	if(str == "oil"){
	    medium = 0;
	}
	else{
	    medium = 1;
	}
	
	p = new Stilllife(title,height,width,medium);	
	artistVec.at(index)->addPainting(*p);
      }
      
      break;
    }  
      
    case 'l': {
      for(int i=0; i<artistVec.size(); i++)
      { 	
	artistVec.at(i)->displayA();
        cout<<endl;
      }	
    break;
    }

    case 'd':{
      String fn,ln;
      cout<<"Enter Artist first name..."<<endl;
      cin>>fn;
      cout<<"Enter Artist last name..."<<endl;
      cin>>ln;

      int flag = 0;
      for(int i=0; i<artistVec.size(); i++)                            
      {	
	if(artistVec.at(i)->getFName()==fn && artistVec.at(i)->getLName()==ln)
	{ 
	  cout<<"Artist found in the system.."<<endl;
	  artistVec.at(i)->removePaintings();
	  flag = 1;
	  break;
	}
      }
      if(flag == 1)
	cout<<"All Paintings successfully deleted"<<endl;
      else
	cout<<"Artist not found in the system"<<endl;
      break;
    }  
      
    case 'r':{
      int _id;
      cout<<"Enter the Painting id to be removed"<<endl;
      cin>>_id;

      Painting dummyPaint(_id);
      
      for(int i=0; i<artistVec.size(); i++)
      {
	if(artistVec.at(i)->searchPainting(dummyPaint))
	{
	  artistVec.at(i)->removeP(dummyPaint);
	  break;
	}
      }
      break;
    }  
  
    case 'c':{
      
      


    }
  
    case 'q':{
      return 0;
    }  
      
    default: {
      cout<<"Invalid Response"<<endl;
      break;
    }
    }
  } 
 
  //for(int i=0; i<artistVec.size(); i++)
  //delete (artistVec.at(i));
  return 0;
}
