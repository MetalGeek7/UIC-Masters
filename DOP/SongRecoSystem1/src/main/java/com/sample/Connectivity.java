package com.sample;

import java.io.FileNotFoundException;
import java.util.List;

import newEntities.SongArtists;
import newEntities.SongRating;
import newEntities.UserData;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import MongoEntities.AlbumBean;
import MongoEntities.ArtistDataBean;
import MongoEntities.SongBean;

import dop.musicreco.ListResult;
import dop.musicreco.MongoConnection;
import dop.musicreco.MySql;


public class Connectivity {

	Configuration cfg = null;
	SessionFactory factory = null;
	ServiceRegistry registry = null;
	KnowledgeBase kbase=null;
	public void initializeParams(){
		cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		registry = new ServiceRegistryBuilder().applySettings(cfg.getProperties()).buildServiceRegistry();
		factory = cfg.buildSessionFactory(registry);
	}

	public static void retrieve(StatefulKnowledgeSession ksession,List res)
	{
		for(Object o:res)
		{
			ksession.insert(o);
		}
		//System.out.println("inserted");
	}

	public static List getList(String className, Session session)
	{
		//List l=null;
		Query query = session.createQuery("from newEntities."+className);
		query.setFirstResult(0);
		query.setMaxResults(500000);
		List res = query.list();
		return res;
	}

	public static List getSongRating(Session session)
	{
		//List l=null;
		Query query = session.createQuery("select s.id.songId,avg(s.id.songRating)  from SongRating s order by s.id.songId ");
		query.setFirstResult(0);
		query.setMaxResults(10000);
		List res = query.list();
		SongRating s;//=(SongRating) res.get(0);
		//s.setId(id)
		System.out.println(res.get(0));
		return res;
	}

	public void createKnowledgeBase() throws Exception
	{
		if(kbase==null)
		 kbase = readKnowledgeBase();
	}

	public StatefulKnowledgeSession getKnowledgeSession() throws Exception{
		createKnowledgeBase();
		return kbase.newStatefulKnowledgeSession();
	}

	/**
	 * Method to increment the likes of a song for a user in both MongoDB and MySQL
	 * @param user_id
	 * @param song_id
	 * @throws Exception
	 */
	public void getLikes(String user_id,String song_id) throws Exception
	{
		createKnowledgeBase();
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
	    KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "test");
	    InvokeRules ruleInvoker=new InvokeRules();
	    ruleInvoker.updateSongLikes(ksession, user_id, song_id);
	}

	/**
	 * Method to fetch artist data based on on artist ID from MongoDB
	 * @param artistID
	 * @throws Exception
	 */
	public void getArtistData(String artistID) throws Exception{
		createKnowledgeBase();
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
	    KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "test");
	    InvokeRules ruleInvoker=new InvokeRules();
	    ruleInvoker.fetchArtistData(ksession, artistID, "ArtistDetails");
	}

	/**
	 * Method to fetch similar artists based on current artist ID
	 * @param artist_id
	 * @throws Exception
	 */
	public void getSimilarArtists(String artist_id) throws Exception{
		createKnowledgeBase();
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
	    KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "test");
	    InvokeRules ruleInvoker = new InvokeRules();
	    ruleInvoker.fetchSimilarArtists(ksession, artist_id);
	}

	public String getPreferredArtist(String userID) throws Exception{

		if(factory == null){
			initializeParams();
		}
		Session session = factory.openSession();
		Query query=session.createQuery(" from UserData where userId = :uid"); //group by u.userLocation");
		query.setParameter("uid", userID);
		List results=query.list();
		UserData user = null;
		for(Object o : results){
			 user = (UserData) o;
		}
		return user.getUserPreferredArtist();
	}

	public void getSongRatings(String user_id,String song_id, Integer rating) throws Exception
	{
			createKnowledgeBase();
		 StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
	        KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "test");
	        InvokeRules s=new InvokeRules();
	        s.updateSongRating(ksession, user_id, song_id, rating);
	}

	public void getArtistLiked(String user_id,String artist_id) throws Exception
	{
			createKnowledgeBase();
		 StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
	        KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "test");
	        InvokeRules s=new InvokeRules();
	        s.updateArtistLiked(ksession, user_id, artist_id);
	}

	public void getAlbumLiked(String user_id, String album_id) throws Exception
	{
			createKnowledgeBase();
		 StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
	        KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "test");
	        InvokeRules s=new InvokeRules();
	        s.updateAlbumLiked(ksession, user_id, album_id);
	}

	public static List getLocations(Session session,int start,int end)
	{
		Query query=session.createQuery("select s.artistLocation from SongArtists s group by s.artistLocation"); //group by u.userLocation");
		query.setFirstResult(start);
		query.setMaxResults(end);
		List results=query.list();
		System.out.println(results.get(0)+" "+results.get(1));
		return results;
	}

	public static List getUsers(Session session,int start,int end)
	{
		Query query=session.createQuery(" from SongData s order by s.songId "); //group by u.userLocation");
		query.setFirstResult(start);
		query.setMaxResults(end);
		List results=query.list();
		return results;
	}

	public static List getArtists(Session session,int start,int end)
	{
		Query query=session.createQuery(" from SongArtists a order by a.artistLocation"); //group by u.userLocation");
		query.setFirstResult(start);
		query.setMaxResults(end);
		List results=query.list();
		return results;

	}

	public static void  passUsers(Session session, StatefulKnowledgeSession ksession,KnowledgeBase kbase) throws FileNotFoundException
	{
		List results;List artists;List ratings;
		artists=getArtists(session, 0, 1000);
		results=getUsers(session,0, 1000);
		ratings=getSongRating(session);
		System.out.println("size of songdata" +results.size());

		System.out.println(results.get(0));
		List locations=getLocations(session, 0, 2000);
		System.out.println("size of location "+locations.size());
		int count=0;
		ksession.setGlobal("count", count);
		SongArtists s=new SongArtists();
		//PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
		//System.setOut(out);
		int i=0;
		while(i<1)
		{
			ksession = kbase.newStatefulKnowledgeSession();
			s.setArtistLocation((String)locations.get(i));
			System.out.println(s.getArtistLocation());
			ksession.setGlobal("location", s);
			retrieve(ksession,results);
			retrieve(ksession,artists);
			retrieve(ksession,ratings);

			ksession.fireAllRules();
			//System.out.println("Here "+locations.get(i));
			i++;

		}





		/*int start=0;
		int end=10000;
		SongArtists current;
		List results;List artists;
		int count=0;
		while(end<200000)
		{
			results=getUsers(session,start,end);
			artists=getArtists(session, start, end);
			retrieve(ksession, results);
			retrieve(ksession,artists);
			System.out.println();
			System.out.println("size "+results.size());
			int i=0;
			while(i<results.size())
			{
				//current=(UserData)results.get(i);
				//System.out.println("current "+current.getUserLocation()+"  "+current.getLoginName());
				//Thread t;
				count++;
				System.out.println("count "+count);
				ksession.setGlobal("current", current);
				//System.out.println("hi");

				//ksession.getAgenda().getAgendaGroup( "location" ).setFocus();
				//ksession.fireAllRules();
				i++;


			}
			start=end+1;
			end=end+10000;

		}*/


	}

	@SuppressWarnings("rawtypes")
	public void ruleTest() throws Exception{
		KnowledgeBase kbase = readKnowledgeBase();
        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
        KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "test");

        Session session = factory.openSession();

        InvokeRules s=new InvokeRules();
        s.updateSongRating(ksession, "user_10", "song_1", 5);
		logger.close();

	}

	private static KnowledgeBase readKnowledgeBase() throws Exception {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
       // kbuilder.add(ResourceFactory.newClassPathResource("student.drl"), ResourceType.DRL);
       // kbuilder.add(ResourceFactory.newClassPathResource("requestHandler.drl"), ResourceType.DRL);
        //kbuilder.add(ResourceFactory.newClassPathResource("preference.drl"), ResourceType.DRL);
      //  kbuilder.add(ResourceFactory.newClassPathResource("location.drl"), ResourceType.DRL);
       // kbuilder.add(ResourceFactory.newClassPathResource("danceability.drl"), ResourceType.DRL);
      // kbuilder.add(ResourceFactory.newClassPathResource("updateData.drl"), ResourceType.DRL);
        kbuilder.add(ResourceFactory.newClassPathResource("Mongo.drl"), ResourceType.DRL);
       // kbuilder.add(ResourceFactory.newClassPathResource("user.drl"), ResourceType.DRL);
        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        if (errors.size() > 0) {
            for (KnowledgeBuilderError error: errors) {
                System.err.println(error);
            }
            throw new IllegalArgumentException("Could not parse knowledge.");
        }
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
        return kbase;
    }

	public static void main(String[] args) {
		Connectivity connect = new Connectivity();
		connect.initializeParams();
		try {
			MySql.getConnection1("root", "tejas");
			MongoConnection.getConnection("songrecosystem", "localhost");
			//c.getArtistLiked("user_100","artist_1");
			//c.getAlbumLiked("user_100","album_1");
		//	c.getLikes("user_1", "song_64243");
			//connect.getSimilarArtists("artist_417501");

			StatefulKnowledgeSession ksession = connect.getKnowledgeSession();
			InvokeRules ruleInvoker = new InvokeRules();

			/// mongo
			System.out.println("---------------------------------------------");
			List<SongBean> list=ruleInvoker.fetchMostGrossedSongs(ksession);
			System.out.println("list size:"+list.size());
			List<AlbumBean> list1=ruleInvoker.fetchMostGrossedAlbums(ksession);
			System.out.println("list1 size"+list1.size());
			List<AlbumBean> list2=ruleInvoker.fetchMostPopularAlbums(ksession);
			//System.out.println("---------------------------------------------");*/
			/////


		//	ruleInvoker.fetchAlbumsGenreLiked(ksession, "user_87028");
			/*ruleInvoker.fetchSongsGenreLiked(ksession, "user_29662");
	    	System.out.println("before song genre liked---------------------------------------------");
		 	ruleInvoker.fetchAlbumsLiked(ksession,"user_87028");
		 	System.out.println("---------------------------------------------");
			ruleInvoker.fetchAlbumsShared(ksession,"user_87028");
			System.out.println("---------------------------------------------");
			ruleInvoker.fetchAlbumsPurchased(ksession,"user_87028");
			System.out.println("---------------------------------------------");
			ruleInvoker.fetchSongsLiked(ksession,"user_29662");
			System.out.println("---------------------------------------------");
			ruleInvoker.fetchSongsShared(ksession,"user_87028");
			System.out.println("---------------------------------------------");
			ruleInvoker.fetchSongsPurchased(ksession,"user_87028");
			System.out.println("---------------------------------------------");
			ruleInvoker.fetchArtistsLiked(ksession,"user_87028");
			System.out.println("---------------------------------------------");
			System.out.println("start");

			ruleInvoker.fetchSimilarArtists(ksession,"artist_417501");
			System.out.println("---------------------------------------------");
			ruleInvoker.fetchSimilarAlbums(ksession,"album_213469");
			System.out.println("---------------------------------------------");
			ruleInvoker.fetchSimilarSongs(ksession,"song_125137");
			System.out.println("---------------------------------------------");
			System.out.println("finish");
			System.out.println("---------------------------------------------");*/
		//

			//album_213469
			//ruleInvoker.fetchSongDataForArtist(ksession, "artist_417501", "SongFromArtist");
			//ruleInvoker.fetchSongDataForSong(ksession, "song_417501", "SongData");
			//ruleInvoker.fetchSongDataForGenre(ksession, "genre_8", "SongFromGenre");
			//ruleInvoker.fetchSongDataForLocation(ksession, "India", "SongFromArtist");
			//ruleInvoker.fetchSongDataForArtist(ksession, connect.getPreferredArtist("user_99999"), "SongFromArtist");
		//	ruleInvoker.fetchSongDataBetweenYears(ksession, 1995, 2000, "SongFromYearRange");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
