package dop.musicreco;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.javajson.JsonException;
import net.sourceforge.javajson.JsonObject;

import MongoEntities.MongoUtil;
import MongoEntities.SongBean;

import MongoEntities.ArtistDataBean;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;

public class ArtistOperation {

	public static void createArtistCollection()									//Creates a artist collection which has likedcount,avg rating...
	{
		PreparedStatement st;
		ResultSet rs = null;
		DBCollection artistColl = MongoConnection.db.getCollection("ArtistList");
		//BasicDBObject[] artistObjs = new BasicDBObject[600000];					//Setting the size of documents to be added in the collection
		//Vector<String> artist_IDs = new Vector<String>(500000, 50000);
		BasicDBObject artistListObj;
		int count=1;

		if(artistColl!=null)														//ArtistList collection exists in MongoDb
		{
			try {
				//Getting artist_ids of all the artists
				st = MySql.conn.prepareStatement("select * from song_artists order by artist_id asc");
				ResultSet rs_artistids = st.executeQuery();
				while(rs_artistids.next())
				{
					System.out.println("Count of artist added="+count);
					float liked_count=0;
					float avg_rating=0;

					String artist_id = rs_artistids.getString("artist_id");
					String artist_name = rs_artistids.getString("artist_name");
					String artist_dob = rs_artistids.getString("artist_dob");
					String artist_locn = rs_artistids.getString("artist_location");
					String artist_genre = rs_artistids.getString("artist_genre_id");
					{

						st = MySql.conn.prepareStatement( "SELECT ( SELECT COUNT(*)"+
															" FROM artists_liked where artist_id='"+artist_id+"'"+
															") AS artist_likedcount,"+
															"( SELECT AVG(artist_rating)"+
															" FROM artist_rating where artist_id='"+artist_id+"'"+
															") AS artist_avgrating"+
															" FROM dual");
						rs = st.executeQuery();
						while(rs.next())
						{

							liked_count = rs.getInt("artist_likedcount");

							avg_rating = rs.getFloat("artist_avgrating");
						}

						//Inserting one artist with its count values in MongoDb
						artistListObj = new BasicDBObject();
						artistListObj.put("ArtistID", artist_id);
						artistListObj.put("ArtistName", artist_name);
						artistListObj.put("ArtistDOB", artist_dob);
						artistListObj.put("ArtistLocation", artist_locn);
						artistListObj.put("ArtistGenre", artist_genre);
						artistListObj.put("LikedCount", liked_count);
						artistListObj.put("AvgRating", avg_rating);

						artistColl.insert(artistListObj);
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
 * Returns a list of songs composed by the artistID
 * @param artistID
 * @return ArrayList of SongBean objects
 */
    public static ArrayList<SongBean> getSongsforArtist(String artistID)
    {
    	ArrayList<SongBean> songList = new ArrayList<SongBean>();
    	JsonObject myjson = null;
    	SongBean songObj = null;

    	DBCollection coll = MongoConnection.db.getCollection("SongList");

    	if(coll != null)														//Collection exists in MongoDb
		{
			BasicDBObject query = new BasicDBObject();
			query.put("ArtistID", artistID);

			DBCursor cursor = coll.find(query);
			System.out.println("Size of query result"+cursor.size());
			while(cursor.hasNext())												// For each song found from the query
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

				//System.out.println(myjson.getString("LikedCount"));

				Integer likedCount = Integer.parseInt(myjson.getString("LikedCount"));
				Integer sharedCount = Integer.parseInt(myjson.getString("SharedCount"));
				Integer purchaseCount = Integer.parseInt(myjson.getString("PurchasedCount"));
				String songid = myjson.getString("SongID");

				Double avgRating = Double.parseDouble(myjson.getString("AvgRating"));
				Double hot = Double.parseDouble(myjson.getString("SongHotness"));
				Double energy = Double.parseDouble(myjson.getString("SongEnergy"));
				Double danceable = Double.parseDouble(myjson.getString("SongDanceability"));
				Double tempo = Double.parseDouble(myjson.getString("SongTempo"));

				System.out.println("Song Retrieved :"+songid);
				songObj = new SongBean(songid, likedCount, sharedCount, purchaseCount, avgRating, hot, energy, danceable, tempo);

				songList.add(songObj);
			}

		}
    	else
    	{
    		System.err.println("Collection does not exist in MongoDb");
    	}

    	return songList;
    }

    /**
     * Retrieves artist related data from MongoDb for the given artistID
     * @param columnValue
     * @return ArtistBean object
     */
    public static List<Object> getArtistData(String columnName, String columnValue)
    {
    	List<Object> artistList = new ArrayList<Object>();
    	ArtistDataBean artistObj = null;
    	JsonObject myjson = null;
    	DB db = MongoConnection.getConnection("MusicReco", "localhost");
    	DBCollection coll = MongoConnection.db.getCollection("ArtistList");

    	if(coll != null)
		{
			System.out.println("here");
			BasicDBObject query = new BasicDBObject();
			query.put(columnName, columnValue);

			DBCursor cursor = coll.find(query);
			while(cursor.hasNext())												// For each song found from the query
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

				String artistId = myjson.getString("ArtistID");
				String artistName = myjson.getString("ArtistName");
				String artistDOB = myjson.getString("ArtistDOB");
				String artistLocn = myjson.getString("ArtistLocation");
				String artistGenre = myjson.getString("ArtistGenre");

				Double likedCount =    Double.parseDouble(myjson.getString("LikedCount"));
				Double avgRating =  Double.parseDouble(myjson.getString("AvgRating"));

				System.out.println("Artist Name:" + artistName + " DOB:" + artistDOB + " Location:" + artistLocn);

				artistObj = new ArtistDataBean(artistId, artistName, artistDOB, artistLocn, artistGenre, likedCount, avgRating);
				artistList.add(artistObj);
				//System.out.println("Artist Id got after creating Artist Obj: "+artistObj.getArtistLocation());
			}
		}
    	else
    	{
    		System.err.println("Collection does not exist in MongoDb");
    	}

    	return artistList;
    }

    /**
     * Method to return a list of similar artists to the user based on artist ID
     * @param ArtistID
     */
    public static List<Object> getSimilarArtist(String ArtistID)
	{
		String json = null;
		MongoUtil mongoUtil = new MongoUtil();
		DBCollection artistColl = MongoConnection.db.getCollection("SimilarArtistsCollection");
		List<Object> similarArtists = new ArrayList<Object>();
		String val[] = {};
		String ArtistID1 = null;
		if(artistColl != null)
		{
			BasicDBObject query = new BasicDBObject();
			query.put("ArtistID", ArtistID);

			DBCursor cursor = artistColl.find(query);
			while(cursor.hasNext())
			{
				DBObject doc_obj = cursor.next();
				BasicDBObject bdbObj = ((BasicDBObject)doc_obj.get("SimilarArtists"));
		        similarArtists = mongoUtil.parseSimilarArtists(bdbObj);
			}

		}
		//return similarArtists;
		return similarArtists;

		/*for(int i = 0; i < similarArtists.size(); i++){
			System.out.println("Similar Album:" + similarArtists.get(i).getArtistName());
		}*/

	}
}