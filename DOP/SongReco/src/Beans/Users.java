package Beans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import MongoEntities.AlbumBean;
import MongoEntities.ArtistBean;
import MongoEntities.SongBean;

import newEntities.AlbumRating;
import newEntities.UserData;

public class Users {

	public static final ArrayList<User> users=new ArrayList<User>();
	public static UserData user=new UserData();
	public static List<SongBean> listSongs;
	public static List<AlbumBean> listAlbums;
	public static List<ArtistBean> listArtists;
	//public static AlbumRa
	public ArrayList<User> getUsers()
	{
		/*for(int i=0;i<users.size();i++){
		System.out.println("users list:"+users.get(i));

		}*/
		return users;
	}
	public static void main(String args[])
	{
		//Albumbean a=new Albumbean("abe", 43.0);
		//System.out.println(a);
		testConnection1 con=new testConnection1();
		con.initializeParams();
		String loginName="rskinne27";
		//String query_string="from UserData where loginName='rskinne27'";
		List list=con.testQuery(loginName);
		UserData user=null;
		//UserData user=(UserData)list.get(0);
		//Users.user=user;
		for (Iterator it = list.iterator(); it.hasNext();) {
			user = (UserData) it.next();
			System.out.println("in UI:"+user.getUserId());
		}
		//System.out.println(Users.user.getUserName());
	}
}
