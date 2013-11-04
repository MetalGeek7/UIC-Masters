package dop.musicreco;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import net.sourceforge.javajson.JsonException;
import net.sourceforge.javajson.JsonObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.WriteConcern;

public class MySql {

	//String conn_name = "";
	public static Connection conn;
	public static String connection;
	public static DBCollection coll;
	public static DB db;
	public static Mongo m;

	public static void MySql()
	{
		connection = "";
		db = null;

	}

	public static void getConnection1(String userName, String psswd)
	{
		//Connection conn = null;
		boolean conn_val = false;

        try
        {

        	//jdbc
        	String url = "jdbc:mysql://localhost:3307/songrecosystem";
        	Class.forName ("com.mysql.jdbc.Driver").newInstance ();
            conn = DriverManager.getConnection (url, userName, psswd);
            System.out.println ("MySQL Db connection established");

        }
        catch (Exception e)
        {
            System.err.println ("Cannot connect to database server");
            System.out.println("Exception while connecting to MySQL:"+e);
        }
	}

	public static void closeDBConnection()
	{
		if(conn != null)
		{
			try
			{
				conn.close();
				System.out.println("Database connection terminated");

			}
			catch(Exception exc)
			{
				System.out.println("Exception while closing connection");
			}
		}

	}

}
