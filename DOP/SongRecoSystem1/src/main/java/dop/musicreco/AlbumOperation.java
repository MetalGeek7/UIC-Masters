package dop.musicreco;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.javajson.JsonException;
import net.sourceforge.javajson.JsonObject;
import MongoEntities.AlbumBean;
import MongoEntities.MongoUtil;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;

public class AlbumOperation {

	public static void createAlbumCollection()									//Creates a album collection which has likedcount,avg rating...
	{
		PreparedStatement st;
		ResultSet rs = null;
		DBCollection albumColl = MongoConnection.db.getCollection("AlbumList");
		//BasicDBObject[] albumObjs = new BasicDBObject[600000];					//Setting the size of documents to be added in the collection
		//Vector<String> album_IDs = new Vector<String>(500000, 50000);
		BasicDBObject albumListObj;
		int count=1;

		if(albumColl!=null)														//AlbumList collection exists in MongoDb
		{
			try {
				//Getting album_ids of all the albums
				st = MySql.conn.prepareStatement("select * from song_albums order by album_id asc");
				ResultSet rs_albumids = st.executeQuery();
				while(rs_albumids.next())
				{
					System.out.println("Count of artist added="+count);
					float shared_count=0;
					float liked_count=0;
					float purchase_count=0;
					float avg_rating=0;

					//rs.getString("album_id");
					String album_id = rs_albumids.getString("album_id");
					String album_name = rs_albumids.getString("album_name");
					String album_artistID = rs_albumids.getString("album_artist_id");
					String album_relYear = rs_albumids.getString("album_release_year");
					double album_cost = rs_albumids.getDouble("album_cost");
					String album_genre = rs_albumids.getString("album_genre_id");

					{

						st = MySql.conn.prepareStatement( "SELECT  ("+
															"SELECT COUNT(*)"+
															" FROM albums_shared where album_id='"+album_id+"'"+
															") AS album_sharedcount,"+
															"( SELECT COUNT(*)"+
															" FROM albums_liked where album_id='"+album_id+"'"+
															") AS album_likedcount,"+
															"( SELECT COUNT(*)"+
															" FROM albums_purchased where album_id='"+album_id+"'"+
															") AS album_purchasecount,"+
															"( SELECT AVG(album_rating)"+
															" FROM album_rating where album_id='"+album_id+"'"+
															") AS album_avgrating"+
															" FROM dual");
						rs = st.executeQuery();
						while(rs.next())
						{
							shared_count = rs.getInt("album_sharedcount");
							liked_count = rs.getInt("album_likedcount");
							purchase_count = rs.getInt("album_purchasecount");
							avg_rating = rs.getFloat("album_avgrating");
						}

						//Inserting one album with its count values in MongoDb
						albumListObj = new BasicDBObject();
						albumListObj.put("AlbumID", album_id);
						albumListObj.put("AlbumName", album_name);
						albumListObj.put("AlbumArtistID", album_artistID);
						albumListObj.put("AlbumReleaseYear", album_relYear);
						albumListObj.put("AlbumCost", album_cost);
						albumListObj.put("AlbumGenreID", album_genre);
						albumListObj.put("LikedCount", liked_count);
						albumListObj.put("SharedCount", shared_count);
						albumListObj.put("PurchasedCount", purchase_count);
						albumListObj.put("AvgRating", avg_rating);
						albumColl.insert(albumListObj);
						MongoConnection.m.setWriteConcern(WriteConcern.SAFE);
					}
					count++;
				}

			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else																		//Collection does not exist in MongoDb
		{
			System.out.println("Collection doesnt exist in MongoDb");
		}

	}

	/**
	 * This method gets all the Album objects composed by a particular Artist from AlbumList collection in Mongo
	 * @param artistId: Artist corresponding to whom the method should fetch all albums
	 * @return ArrayList of AlbumBean objects
	 */
	public static List<Object> getAlbumData(String columnName, String columnValue)
	{
		List<Object> albumList = new ArrayList<Object>();
    	JsonObject myjson = null;
    	AlbumBean albumObj = null;
    	DB db = MongoConnection.getConnection("MusicReco", "localhost");
    	DBCollection coll = MongoConnection.db.getCollection("AlbumList");
    	if(coll != null)														//Collection exists in MongoDb
		{
			BasicDBObject query = new BasicDBObject();
			query.put(columnName, columnValue);

			DBCursor cursor = coll.find(query);
			System.out.println("Size of query result"+cursor.size());
			while(cursor.hasNext())												// For each album found from the query
			{
				DBObject dbObj = cursor.next();
				try {
					myjson = JsonObject.parse(dbObj.toString());
				}
				catch (JsonException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				String albumID = myjson.getString("AlbumID");
				String albumName = myjson.getString("AlbumName");
				String albumArtist = myjson.getString("AlbumArtistID");
				String albumReleaseYr = myjson.getString("AlbumReleaseYear");
				String albumGenre = myjson.getString("AlbumGenreID");
				double likedCount = Double.parseDouble(myjson.getString("LikedCount"));
				double sharedCount = Double.parseDouble(myjson.getString("SharedCount"));
				double purchaseCount = Double.parseDouble(myjson.getString("PurchasedCount"));

				Double avgRating =  Double.parseDouble(myjson.getString("AvgRating"));
				Double albumCost =  Double.parseDouble(myjson.getString("AlbumCost"));

				System.out.println("Album Name:" + albumName + " Album Artist:" + albumArtist + " Album Release year:" + albumReleaseYr);

				albumObj = new AlbumBean(albumID, albumName, albumArtist, albumReleaseYr, albumCost, albumGenre, likedCount, sharedCount, purchaseCount, avgRating);
				albumList.add(albumObj);
			}

		}
    	else
    	{
    		System.err.println("Collection does not exist in MongoDb");
    	}

    	return albumList;
	}

	public static  List<Object> getSimilarAlbum(String AlbumID)
	{
		String json = null;
		MongoUtil mongoUtil = new MongoUtil();
		DBCollection albumColl = MongoConnection.db.getCollection("SimilarAlbumsCollection");
		List<Object> similarAlbums = new ArrayList<Object>();
		String val[] = {};
		String AlbumID1 = null;

		if(albumColl != null)
		{
			BasicDBObject query = new BasicDBObject();
			query.put("AlbumID", AlbumID);

			DBCursor cursor = albumColl.find(query);
			while(cursor.hasNext())
			{
				DBObject doc_obj = cursor.next();
				BasicDBObject bdbObj = ((BasicDBObject)doc_obj.get("SimilarAlbums"));
			    similarAlbums = mongoUtil.parseSimilarAlbums(bdbObj);
			}

		}
		return similarAlbums;

	}


 public static  List<Object> fetchMostGrossedAlbums()
	{
		String json = null;
		MongoUtil mongoUtil = new MongoUtil();
		DBCollection albumColl = MongoConnection.db.getCollection("GrossedAlbumsCollection");
		//List<String> similarAlbums = new ArrayList<String>();
		List<Object> similarAlbums1 = new ArrayList<Object>();
		String val[] = {};
		String AlbumID1 = null;
		AlbumBean albumObj = null;
		JsonObject myjson = null;

		if(albumColl != null)
		{
			DBCursor cursor = albumColl.find();
			while(cursor.hasNext())
			{
				DBObject bobj = cursor.next();

				try {
					myjson = JsonObject.parse(bobj.toString());
				} catch (JsonException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				String album_id = myjson.getString("album_id");
				double purchaseCount = Double.valueOf(myjson.getString("album_mostGrossed"));
				if(purchaseCount != 0)
				similarAlbums1.add(new AlbumBean(album_id,null,null,null,0,null,0,0,purchaseCount,0));
			}

		}
		return similarAlbums1;
	}


 public static  List<Object> FetchMostPopularAlbums()
	{
		String json = null;
		MongoUtil mongoUtil = new MongoUtil();
		DBCollection albumColl = MongoConnection.db.getCollection("PopularAlbumsCollection");
		//List<String> similarAlbums = new ArrayList<String>();
		List<Object> similarAlbums1 = new ArrayList<Object>();
		String val[] = {};
		String AlbumID1 = null;
		AlbumBean albumObj = null;
		JsonObject myjson = null;

		if(albumColl != null)
		{
			DBCursor cursor = albumColl.find().sort(new BasicDBObject( "album_mostGrossed" , -1 )) ;
			while(cursor.hasNext())
			{
				DBObject bobj = cursor.next();

				try {
					myjson = JsonObject.parse(bobj.toString());
				} catch (JsonException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				String album_id = myjson.getString("album_id");
				double purchaseCount = Double.valueOf(myjson.getString("album_mostGrossed"));
				if(purchaseCount != 0)
				similarAlbums1.add(new AlbumBean(album_id,null,null,null,0,null,0,0,purchaseCount,0));
			}

		}
		return similarAlbums1;
	}

}
