package dop.musicreco;

import java.sql.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import MongoEntities.ArtistDataBean;
import MongoEntities.MongoUtil;
import MongoEntities.SongBean;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.mongodb.WriteConcern;
import com.sun.org.apache.bcel.internal.generic.NEW;

import net.sourceforge.javajson.JsonException;
import net.sourceforge.javajson.JsonObject;

public class SongOperation {

	/*
	 * Creates the whole SongList in MongoDb with LikedCount, PurchasedCount
	 * etc...
	 */
	public static void createSongCollection() // Creates a song collection which
												// has likedcount,avg rating...
	{
		PreparedStatement st;
		ResultSet rs = null;

		DBCollection songColl = MongoConnection.db.getCollection("SongList");
		// BasicDBObject[] songObjs = new BasicDBObject[600000]; //Setting the
		// size of documents to be added in the collection

		Vector<String> song_IDs = new Vector<String>(500000, 50000);

		if (songColl != null) // SongList collection exists in MongoDb
		{
			try {
				// Getting song_ids of all the songs
				st = MySql.conn
						.prepareStatement("select song_id from song_data order by song_id asc");
				ResultSet rs_songids = st.executeQuery();
				while (rs_songids.next()) {
					float shared_count = 0;
					float liked_count = 0;
					float purchase_count = 0;
					float avg_rating = 0;
					float hotness = 0;
					float energy = 0;
					float danceability = 0;
					float tempo = 0;

					// rs.getString("song_id");
					song_IDs.add(rs_songids.getString("song_id"));
					System.out.println("Size of SongID Vector ="
							+ song_IDs.size());

					Iterator<String> iter = song_IDs.iterator();
					while (iter.hasNext()) // Song_ids still left in Vector to
											// be added in MongoDb
					{
						String song_id = (String) iter.next();

						// st =
						// MySql.conn.prepareStatement("select song_id,count(song_id) as song_sharedcount from songs_shared where song_id = '"+song_id+"'");

						st = MySql.conn.prepareStatement("SELECT  ("
								+ "SELECT COUNT(*)"
								+ " FROM songs_shared where song_id='"
								+ song_id
								+ "'"
								+ ") AS song_sharedcount,"
								+ "( SELECT COUNT(*)"
								+ " FROM songs_liked where song_id='"
								+ song_id
								+ "'"
								+ ") AS song_likedcount,"
								+ "( SELECT COUNT(*)"
								+ " FROM songs_purchased where song_id='"
								+ song_id
								+ "'"
								+ ") AS song_purchasecount,"
								+ "( SELECT AVG(song_rating)"
								+ " FROM song_rating where song_id='"
								+ song_id
								+ "'"
								+ ") AS song_avgrating,"
								+ "( SELECT song_hotness"
								+ " FROM song_data where song_id='"
								+ song_id
								+ "'"
								+ ") AS song_hotness,"
								+ "( SELECT song_energy"
								+ " FROM song_data where song_id='"
								+ song_id
								+ "'"
								+ ") AS song_energy,"
								+ "( SELECT song_danceability"
								+ " FROM song_data where song_id='"
								+ song_id
								+ "'"
								+ ") AS song_danceability,"
								+ "( SELECT song_tempo"
								+ " FROM song_data where song_id='"
								+ song_id
								+ "'" + ") AS song_tempo" + " FROM dual");

						rs = st.executeQuery();

						while (rs.next()) {
							shared_count = rs.getInt("song_sharedcount");
							liked_count = rs.getInt("song_likedcount");
							purchase_count = rs.getInt("song_purchasecount");
							avg_rating = rs.getFloat("song_avgrating");
							hotness = rs.getFloat("song_hotness");
							energy = rs.getFloat("song_energy");
							danceability = rs.getFloat("song_danceability");
							tempo = rs.getFloat("song_tempo");
							// System.out.println("Count of shared for songId: "+song_id+
							// " is "+shared_count);
						}

						// Inserting one song with its count values in MongoDb
						BasicDBObject songListObj = new BasicDBObject();
						songListObj.put("SongID", song_id);
						songListObj.put("LikedCount", liked_count);
						songListObj.put("SharedCount", shared_count);
						songListObj.put("PurchasedCount", purchase_count);
						songListObj.put("AvgRating", avg_rating);
						songListObj.put("SongHotness", hotness);
						songListObj.put("SongEnergy", energy);
						songListObj.put("SongDanceability", danceability);
						songListObj.put("SongTempo", tempo);

						songColl.insert(songListObj);
						// System.out.println("Document inserted successfully");
						MongoConnection.m.setWriteConcern(WriteConcern.SAFE);

						/*
						 * for(int i=0; i<songObjs.length; i++) {
						 * songObjs[i].put
						 *
						 * }
						 */

					}

				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else // Collection does not exist in MongoDb
		{
			MongoConnection.db.createCollection("SongList",
					(DBObject) MongoConnection.db);
			createSongCollection();
		}

	}

	public static void getSongDataBetweenYears(String collectionColumn,
			int start, int end) {
		// SongBean songBean = new SongBean();
		JsonObject jsonObject = null;
		DB db = MongoConnection.getConnection("MusicReco", "localhost");
		DBCollection collection = MongoConnection.db.getCollection("SongList");
		String song_id, song_artist, song_album, song_genre, release_year;

		if (collection != null) {
			BasicDBObject query = new BasicDBObject("YearofRelease",
					new BasicDBObject("$gte", start).append("YearofRelease",
							new BasicDBObject("$lte", end)));

			DBCursor cursor = collection.find(query);
			while (cursor.hasNext()) // For each song found from the query
			{
				DBObject dbObj = cursor.next();
				try {
					jsonObject = JsonObject.parse(dbObj.toString());
				} catch (JsonException e) {
					e.printStackTrace();
				}

				song_id = jsonObject.getString("SongID");
				song_artist = jsonObject.getString("ArtistID");
				song_album = jsonObject.getString("AlbumID");
				song_genre = jsonObject.getString("GenreID");
				release_year = jsonObject.getString("YearofRelease");

				System.out.println("Song:" + song_id + " Artist:" + song_artist
						+ " Genre:" + song_genre + " Album:" + song_album
						+ " Year:" + release_year);

				// Integer likedCount =
				// Integer.parseInt(jsonObject.getString("LikedCount"));
				// Double avgRating =
				// Double.parseDouble(jsonObject.getString("AvgRating"));

				// songBean = new SongBean(song_id, 0, 0, 0, 0, 0, 0, 0, 0);
				// System.out.println("Artist Id got after creating Artist Obj: "+
				// artistObj.getArtistLocation());
			}
		} else {
			System.err.println("Collection does not exist in MongoDb");
		}

	}

	/**
	 * Method to fetch the song data from song collection in MongoDB
	 *
	 * @param attributeValue
	 */
	public static List<Object> getSongData(String collectionColumn,
			String attributeValue) {

		List<Object> songList = new ArrayList<Object>();

		SongBean songObj = null;
		JsonObject jsonObject = null;
		DB db = MongoConnection.getConnection("MusicReco", "localhost");
		DBCollection collection = MongoConnection.db.getCollection("SongList");
		String song_id, song_artist, song_album, song_genre;
		int release_year;

		if (collection != null) {
			BasicDBObject query = new BasicDBObject();
			query.put(collectionColumn, attributeValue);

			DBCursor cursor = collection.find(query);
			while (cursor.hasNext()) // For each song found from the query
			{
				DBObject dbObj = cursor.next();
				try {
					jsonObject = JsonObject.parse(dbObj.toString());
				} catch (JsonException e) {
					e.printStackTrace();
				}

				song_id = jsonObject.getString("SongID");
				song_artist = jsonObject.getString("ArtistID");
				song_album = jsonObject.getString("AlbumID");
				song_genre = jsonObject.getString("GenreID");
				release_year = Integer.parseInt(jsonObject.getString("YearofRelease"));

				System.out.println("Song:" + song_id + " Artist:" + song_artist
						+ " Genre:" + song_genre + " Album:" + song_album
						+ " Year:" + release_year);

				Double likedCount = Double.parseDouble(jsonObject
						.getString("LikedCount"));
				Double avgRating = Double.parseDouble(jsonObject
						.getString("AvgRating"));
				Double sharedCount = Double.parseDouble(jsonObject
						.getString("SharedCount"));
				Double purchasedCount = Double.parseDouble(jsonObject
						.getString("PurchasedCount"));
				Double hotness = Double.parseDouble(jsonObject
						.getString("SongHotness"));
				Double tempo = Double.parseDouble(jsonObject
						.getString("SongTempo"));
				Double danceability = Double.parseDouble(jsonObject
						.getString("SongDanceability"));
				Double energy = Double.parseDouble(jsonObject
						.getString("SongEnergy"));

				songObj = new SongBean(song_id, song_album, song_artist, song_genre,
						likedCount, sharedCount, purchasedCount, release_year,
						avgRating, hotness, energy,
						danceability, tempo);
				songList.add(songObj);
				// System.out.println("Artist Id got after creating Artist Obj: "+
				// artistObj.getArtistLocation());
			}
		} else {
			System.err.println("Collection does not exist in MongoDb");
		}

		return songList;

	}

	/**
	 *
	 * @param Song_ID
	 * @param collection
	 * @return
	 */
	public static int getSongLikedCount(String Song_ID, String collection) {
		System.out.println(" in getSongLikedCount    ");
		int likeCount = 0;
		String json = null;
		JsonObject myjson = null;
		DB db = MongoConnection.getConnection("MusicReco", "localhost");
		DBCollection coll = MongoConnection.db.getCollection(collection);

		if (coll != null) {
			// System.out.println("here");
			BasicDBObject query = new BasicDBObject();
			query.put("SongID", Song_ID);

			DBCursor cursor = coll.find(query);
			while (cursor.hasNext()) {
				DBObject bobj = cursor.next();
				json = bobj.toString();
				try {
					myjson = JsonObject.parse(json);
				} catch (JsonException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Double countd =
				// Double.parseDouble(myjson.getString("LikedCount"));
				likeCount = Integer.parseInt(myjson.getString("LikedCount"));
				System.out.println("The value is : " + likeCount);
			}

		} else {
			System.out.println("cannot get inside the collection");
		}
		return likeCount;
	}

	public static SongBean getSongObjfrmMongo(String songID, String collection) {
		SongBean songObj = null;
		JsonObject myjson = null;
		DBCollection coll = MongoConnection.db.getCollection(collection);

		if (coll != null) {
			// System.out.println("here");
			BasicDBObject query = new BasicDBObject();
			query.put("SongID", songID); // Puts SongID viz document key and
											// songID viz song that we want to
											// fetch in the query

			DBCursor cursor = coll.find(query);
			while (cursor.hasNext()) {
				DBObject bobj = cursor.next();

				try {
					myjson = JsonObject.parse(bobj.toString());
				} catch (JsonException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				int likedCount = Integer
						.valueOf(myjson.getString("LikedCount"));
				int sharedCount = Integer.valueOf(myjson
						.getString("SharedCount"));
				int purchaseCount = Integer.valueOf(myjson
						.getString("PurchasedCount"));
				String songid = myjson.getString("SongID");

				double avgRating = Double.parseDouble(myjson
						.getString("AvgRating"));
				double hot = Double
						.parseDouble(myjson.getString("SongHotness"));
				double energy = Double.parseDouble(myjson
						.getString("SongEnergy"));
				double danceable = Double.parseDouble(myjson
						.getString("SongDanceability"));
				double tempo = Double
						.parseDouble(myjson.getString("SongTempo"));

				songObj = new SongBean(songid, likedCount, sharedCount,
						purchaseCount, avgRating, hot, energy, danceable, tempo);
			}
		}
		return songObj;
	}

	public static void updateUser(String song_ID, String attribute,
			String att_value) {
		DBCollection coll;
		DB db = MongoConnection.getConnection("MusicReco", "localhost");
		try {
			coll = MongoConnection.db.getCollection("SongList");

			DBObject criteria = new QueryBuilder().put("SongID").is(song_ID)
					.get();

			DBObject update = new Builder().set(attribute, att_value).get();
			coll.update(criteria, update);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static  List<Object> getSimilarSong(String SongID)
	{
		String json = null;
		MongoUtil mongoUtil = new MongoUtil();
		DBCollection songColl = MongoConnection.db.getCollection("SimilarSongsCollection");
		List<String> similarSongs = new ArrayList<String>();
		List<Object> similarSongs1 = new ArrayList<Object>();
		String val[] = {};
		String SongID1 = null;

		if(songColl != null)
		{
			BasicDBObject query = new BasicDBObject();
			query.put("SongID", SongID);

			DBCursor cursor = songColl.find(query);
			while(cursor.hasNext())
			{
				DBObject doc_obj = cursor.next();
				BasicDBObject bdbObj = ((BasicDBObject)doc_obj.get("SimilarSongs"));
			    similarSongs1 = mongoUtil.parseSimilarSongs(bdbObj);
			}

		}

	//	System.out.println(" For the Song : " + SongID +  " The similar Song are : " );
		/*for(int i = 0; i < similarSongs.size(); i++){
			similarSongs1.get(i)
			System.out.println( .getSongID());
		}*/
		return similarSongs1;

	}
	//getMostGrossedSongs

	public static  List<Object> fetchMostGrossedSongs()
	{
		String json = null;
		MongoUtil mongoUtil = new MongoUtil();
		DBCollection songColl = MongoConnection.db.getCollection("GrossedSongsCollection");
		//List<String> similarSongs = new ArrayList<String>();
		List<Object> similarSongs1 = new ArrayList<Object>();
		String val[] = {};
		String SongID1 = null;
		SongBean songObj = null;
		JsonObject myjson = null;

		if(songColl != null)
		{
			//BasicDBObject query = new BasicDBObject();
	//		query.put("SongID", SongID);

			DBCursor cursor = songColl.find();
			while(cursor.hasNext())
			{
				DBObject bobj = cursor.next();

				try {
					myjson = JsonObject.parse(bobj.toString());
				} catch (JsonException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				String song_id = myjson.getString("song_id");
				double purchaseCount = Double.valueOf(myjson.getString("song_mostGrossed"));
				if(purchaseCount != 0)
				similarSongs1.add(new SongBean(song_id,null,null,null,0,0,purchaseCount,0,0,0,0,0,0));
			}

		}
		return similarSongs1;
	}

}
