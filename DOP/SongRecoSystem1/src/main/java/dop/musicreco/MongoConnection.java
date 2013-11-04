/**
 * 
 */
package dop.musicreco;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoOptions;
import com.mongodb.WriteConcern;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import net.sourceforge.javajson.JsonException;
import net.sourceforge.javajson.JsonObject;

/**
 * @author Abhishek
 *
 */
public class MongoConnection {
	
	public static String connection;
	public static DBCollection coll;
	public static DB db;
	public static Mongo m;
	
	public static void MongoConnection()
	{
		connection = "";
		db = null;
	}
	
	public static DB getConnection(String DbName, String serverhost)
	{
		boolean conn_val = false;
		//DB db = null;
		
		try {
			m = new Mongo(serverhost);
			db = m.getDB(DbName);
			
			if(db!=null)
			{
				conn_val = true;
				System.out.println("Connection established!!! ");
				//return db;
			}
			else
			{
				System.out.println("ERROR while establishing connection");
			}
			
		}
		catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		return db;
	}
	
	/**
	 * All functions defined below this were used for reference purpose while playing around with Mongo API's......not used newhere in the application
	 */
	public void executeMongoQuery()
	{
		 
		DBObject myDoc = coll.findOne();
        System.out.println("\n"+myDoc+"\n");
        System.out.println("Printing the number of docs in collection "+coll.getCount()+"\n");
        System.out.println("Printing the total docs in collection\n");
        DBCursor cursor = coll.find();
        //coll.fin
        
        BasicDBObject query = new BasicDBObject();
        query.put("userId", "user1006");
        
        DBCursor cursor1 = coll.find(query);

        try {
            while(cursor.hasNext()) 
            {
                System.out.println(cursor.next()+"\n next queryresult :");
            }
            
            while(cursor1.hasNext()) 
            {
                //System.out.println(cursor1.next());
            	DBObject doc_obj = cursor1.next();
            	
            	doc_obj.get("songID");
            	System.out.println("Song ID's that we want :"+doc_obj.get("songId"));
            }
        } 
        finally 
        {
            cursor.close();
            cursor1.close();
        }
         
	}
	
	//private void insertDocument()
	
	public void insertLikedItem(String userID, ArrayList<String> value, String CollecType)
	{
		HashMap<String, String> map_ip = new HashMap<String, String>();
		DBCollection coll;
		DB db = getConnection("MusicReco", "localhost");
	
		if(CollecType.equals("LikedSongs"))
		{
			coll = db.getCollection("LikedSongs");
			if(coll!=null)
			{
				BasicDBObject dbObj = new BasicDBObject();
				dbObj.put("UserID", userID);
				
				if(value.size() >= 1)		//User Likes one or more than one songs at a time
				{
					BasicDBObject songListObj = new BasicDBObject();
					for(int i=0; i<value.size(); i++)
					{	
						//map_ip.put(String.valueOf(i), value[i]);
						
						//map_ip.put("songID", value[i]);
						
						
						songListObj.put(String.valueOf(i), value.get(i));
					}
					//Puttin the songids in a nested document
					//BasicDBObject songListObj = new BasicDBObject(map_ip);
					//songListObj.putAll(map_ip);	
					
					//Putting the nested document in the original document
					dbObj.put("SongIDs", songListObj);
					coll.insert(dbObj);
					System.out.println("Record inserted successfully");
					m.setWriteConcern(WriteConcern.SAFE);
					
				}
				
				System.out.println(coll.getCount());
				
			}
			else												//Logic for handling the case if the collection doesnt exists in Db
			{
				db.createCollection(CollecType, (DBObject) db );
			}
		}
		else if(CollecType.equals("LikedArtists"))
		{
			coll = db.getCollection("LikedArtists");
			if(coll!=null)										//Checks whether the collection exists in the Database
			{
				BasicDBObject dbObj = new BasicDBObject();
				dbObj.put("UserID", userID);
				
				if(value.size() >= 1)
				{
					
					for(int i=0; i<value.size(); i++)
					{
						map_ip.put(String.valueOf(i), value.get(i));
					}
					BasicDBObject artistListObj = new BasicDBObject();
					artistListObj.putAll(map_ip);
					
					//Putting the nested document in the original document
					dbObj.put("ArtistIDs", artistListObj);
					coll.insert(dbObj);
					System.out.println("Record inserted successfully");
					m.setWriteConcern(WriteConcern.SAFE);
					
				}
				System.out.println(coll.getCount());
				
			}
			else												//Logic for handling the case if the collection doesnt exists in Db
			{
				db.createCollection(CollecType, (DBObject) db );
			}
		}
		else if(CollecType.equals("LikedAlbums"))
		{
			coll = db.getCollection("LikedAlbums");
			if(coll!=null)
			{
				BasicDBObject dbObj = new BasicDBObject();
				dbObj.put("UserID", userID);
				
				if(value.size() >= 1)
				{
					
					for(int i=0; i<value.size(); i++)
					{
						map_ip.put(String.valueOf(i), value.get(i));
					}
					BasicDBObject albumListObj = new BasicDBObject();
					albumListObj.putAll(map_ip);
					
					//Putting the nested document in the original document
					dbObj.put("AlbumIDs", albumListObj);
					coll.insert(dbObj);
					System.out.println("Record inserted successfully");
					m.setWriteConcern(WriteConcern.SAFE);
				}	
			}
			else												//Logic for handling the case if the collection doesnt exists in Db
			{
				db.createCollection(CollecType, (DBObject) db );
			}
		}
		
	}
	
	public void updateCollection(String CollectionName, String UserID, String objtoUpdate, String newVal)
	{
		DB db = getConnection("MusicReco", "localhost");					
		BasicDBObject query = new BasicDBObject();
        query.put("UserID", UserID);							// Getting particular collection for a UserID
        
        DBCollection coll = db.getCollection(CollectionName);
        
        DBCursor cursor1 = coll.find(query);
        
        try {
            while(cursor1.hasNext()) 
            {
                //System.out.println("Next queryresult :"+cursor1.next());
                DBObject queryRes = cursor1.next();
                String str = queryRes.toString();
                System.out.println("String version of query result :"+str);
                
                //DBObject queryRes = cursor1.next();
                           
                //System.out.println(" Expected Value :"+ queryRes.get("1"));
               
                BasicDBObject query2 = new BasicDBObject();
                query2.put("SongIDs", objtoUpdate);
                
                
                DBCollection bObj = (DBCollection)queryRes;
                //bObj.update(, "U1011");
                
                parseJSON(str);
                //System.out.println("Our required parsed val :"+reqdVal );
            }
            
            /*while(cursor1.hasNext()) 
            {
                //System.out.println(cursor1.next());
            	DBObject doc_obj = cursor1.next();
            	
            	doc_obj.get("songID");
            	System.out.println("Documents that we want :"+doc_obj.get("songId"));
            }*/
        }
        catch(Exception ex)
        {
        	
        }
        finally 
        {
            cursor1.close();
        }
         
	}
	
	public void executeMongoQuery(String DbName, String CollectionName, String argType, String argVal)
	{
		BasicDBObject query = new BasicDBObject();
		if(MongoConnection.db==null)
		{
			System.out.println("Connection not established with MongoDB");
		}
		else
		{	
			DBCollection coll = db.getCollection(CollectionName);
			query.put(argType, argVal);
			
			DBCursor cursor1 = coll.find(query);
			
			try{
				while(cursor1.hasNext())
				{
					DBObject queryRes = cursor1.next();
	                String str = queryRes.toString();
	                System.out.println("String version of query result :"+str);
	                
	                BasicDBObject query2 = new BasicDBObject();
	                //query2.put("");
	                ((DBCollection) queryRes).find();
	                //queryRes.
				}
			}
			catch(Exception ex)
			{
				
			}
		}
		
	}
	
	public static JsonObject parseJSON(String json)								
	{
		JsonObject myjson = null;
		
		try {
			myjson = JsonObject.parse(json);
			
			String title = myjson.getString("UserID");
			String username = myjson.get("SongIDs").toString();
			
			System.out.println("Two reqd values :" + title+" with "+ username);
				

		} catch (JsonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return myjson;
	}
	
	public static void main(String args[])
	{	
		
		//MongoConnection mObj = new MongoConnection();
		//mObj.executeMongoQuery("MusicReco","SimilarArtistsCollection","ArtistID","artist_417500");
		
	}
	
}
