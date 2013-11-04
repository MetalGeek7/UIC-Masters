package MongoEntities;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.javajson.JsonObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import dop.musicreco.MongoConnection;

//MongoUtil

public class MongoUtil {

	public  ArrayList<ArtistDataBean> parseJSON1(BasicDBObject bdbObj, DBCollection coll)
	{

		DBObject doc_obj = null;
		BasicDBObject bdbOb =null;

		ArrayList<String> similarArtistId = new ArrayList<String>();
		ArrayList<ArtistDataBean> similarArtistIdFinal = new ArrayList<ArtistDataBean>();
		String json = bdbObj.toString();
		String innerjson = null;

		String similarartists = null;
		String innersimilarartists = null;
		JsonObject myjson = null;
		JsonObject inmyjson = null;
		try{

			myjson = JsonObject.parse(json);

			for(int i=1; i<=20; i++)
		{
				String reqdVal = myjson.getString(String.valueOf(i));
				similarArtistId.add(reqdVal);
		}


			for(int i=0;i<similarArtistId.size(); i++)
			{
				if(coll != null)
				{

					BasicDBObject query = new BasicDBObject();
					query.put("ArtistId", similarArtistId.get(i));
					DBCursor cursor = coll.find(query);

					while(cursor.hasNext())
					{

						doc_obj = cursor.next();
						innerjson = (String) doc_obj.get("ArtistName");
						System.out.println(innerjson);
						System.out.println("Artist Name : " + innerjson + "Artist ID : " + similarArtistId.get(i));
					    similarArtistIdFinal.add(new ArtistDataBean(similarArtistId.get(i),innerjson));

					}

				}

			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return similarArtistIdFinal;
	}

	public  List<Object> parseSimilarArtists(BasicDBObject bdbObj)
	{

		DBObject doc_obj = null;
		BasicDBObject bdbOb =null;
		DBCollection coll = MongoConnection.db.getCollection("ArtistData");
		ArrayList<String> similarArtistId = new ArrayList<String>();
		List<Object> similarArtistIdFinal = new ArrayList<Object>();
		String json = bdbObj.toString();
		String innerjson = null;

		String similarartists = null;
		String innersimilarartists = null;
		JsonObject myjson = null;
		JsonObject inmyjson = null;
		try{

			myjson = JsonObject.parse(json);

			for(int i=1; i<=20; i++)
		{
				String reqdVal = myjson.getString(String.valueOf(i));

				similarArtistIdFinal.add(new ArtistDataBean(reqdVal, null, null, null, null, 0, 0));
		}

			/*
			for(int i=0;i<similarArtistId.size(); i++)
			{
				if(coll != null)
				{

					BasicDBObject query = new BasicDBObject();
					query.put("ArtistId", similarArtistId.get(i));
					DBCursor cursor = coll.find(query);

					while(cursor.hasNext())
					{

						doc_obj = cursor.next();
						innerjson = (String) doc_obj.get("ArtistName");
					    similarArtistIdFinal.add(new ArtistDataBean(similarArtistId.get(i),innerjson));

					}

				}

			}*/
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return similarArtistIdFinal;
	}

	public  List<Object> parseSimilarAlbums(BasicDBObject bdbObj)
	{

		DBObject doc_obj = null;
		BasicDBObject bdbOb =null;
		DBCollection coll = MongoConnection.db.getCollection("AlbumList");
		ArrayList<String> similarAlbumId = new ArrayList<String>();
		List<Object> similarAlbumIdFinal = new ArrayList<Object>();
		String json = bdbObj.toString();
		String innerjson = null;

		String similaralbums = null;
		String innersimilaralbums = null;
		JsonObject myjson = null;
		JsonObject inmyjson = null;
		try{

			myjson = JsonObject.parse(json);

			for(int i=1; i<=20; i++)
		{
				String reqdVal = myjson.getString(String.valueOf(i));
				similarAlbumIdFinal.add(new AlbumBean(reqdVal,null,null,null,0,null,0,0,0,0));
		}


		/*	for(int i=0;i<similarAlbumId.size(); i++)
			{
				if(coll != null)
				{

					BasicDBObject query = new BasicDBObject();
					query.put("AlbumID", similarAlbumId.get(i));
					DBCursor cursor = coll.find(query);

					while(cursor.hasNext())
					{

						doc_obj = cursor.next();
						innerjson = (String) doc_obj.get("AlbumName");
					    similarAlbumIdFinal.add(new AlbumBean(similarAlbumId.get(i),innerjson, null, null, 0,null,0,0,0,0));

					}

				}

			}*/

		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return similarAlbumIdFinal;
	}

	public  ArrayList<Object> parseSimilarSongs(BasicDBObject bdbObj)
	{

		DBObject doc_obj = null;
		BasicDBObject bdbOb =null;
		DBCollection coll = MongoConnection.db.getCollection("SongList");
		ArrayList<String> similarSongId = new ArrayList<String>();
		ArrayList<Object> similarSongIdFinal = new ArrayList<Object>();
		String json = bdbObj.toString();
		String innerjson = null;

		String similarsongs = null;
		String innersimilarsongs = null;
		JsonObject myjson = null;
		JsonObject inmyjson = null;
		try{

			myjson = JsonObject.parse(json);

			for(int i=1; i<=20; i++)
		{
				String reqdVal = myjson.getString(String.valueOf(i));
				System.out.println(reqdVal);
				similarSongIdFinal.add(new SongBean(reqdVal,null,null,null, 0,0,0,0,0,0,0,0,0));
		}


			/*for(int i=0;i<similarSongId.size(); i++)
			{
				if(coll != null)
				{

					BasicDBObject query = new BasicDBObject();
					query.put("SongID", similarSongId.get(i));
					DBCursor cursor = coll.find(query);

					while(cursor.hasNext())
					{

						doc_obj = cursor.next();
						innerjson = (String) doc_obj.get("SongName");
					    similarSongIdFinal.add(new SongBean(similarSongId.get(i),innerjson, null, null, 0,null,0,0,0,0));

					}

				}

			}*/
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		System.out.println("The count is :" + similarSongIdFinal.size());
		return similarSongIdFinal;
	}



}

