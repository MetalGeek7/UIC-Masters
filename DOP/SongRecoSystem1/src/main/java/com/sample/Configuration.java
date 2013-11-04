package com.sample;

import java.sql.DriverManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;


public class Configuration {

	public Connection con;
	public String driver;
	//returns the connection
	public Connection  getConnection() throws SQLException
	{		
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/songrecosystem","root","prime");
		return con;		
	}
	
	public String getDriver()
	{
	  driver="com.mysql.jdbc.Driver";
	  return driver;
	}
	/*public User validateLogin(String username,String userPass) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		ResultSet result=null;
		User user=null;
		String query="select * from login where name='"+username+"' and password='"+userPass+"'";			
		result=executeQuery(query);
		
		return user;
		
	}*/
	public Statement databaseConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{ 
		Statement st=null;
		Class.forName(getDriver()).newInstance();	
		Connection con=null;		
		con =getConnection();
		System.out.println(con);
		st=con.createStatement();
		return st;
		
	}
	//All the Select(executeQuery) are sent here
	public ResultSet executeQuery(String query) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		ResultSet result=null;
		Statement st=databaseConnection();
		result=st.executeQuery(query);		
		return result;
		
	}
}
