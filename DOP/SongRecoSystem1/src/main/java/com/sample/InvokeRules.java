package com.sample;

import java.util.ArrayList;
import java.util.List;

import newEntities.SongAlbums;
import newEntities.SongArtists;
import newEntities.SongData;
import newEntities.SongRatingId;
import newEntities.UserData;

import org.drools.KnowledgeBase;
import org.drools.runtime.StatefulKnowledgeSession;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import MongoEntities.AlbumBean;
import MongoEntities.ArtistDataBean;
import MongoEntities.GenreBean;
import MongoEntities.SongBean;
import MongoEntities.UserBean;

import dop.musicreco.ListResult;
import dop.musicreco.SongOperation;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class InvokeRules {

	/**
	 * Method to return the song data from Mongo DB as a list of Song beans
	 * @param list
	 * @return
	 */
	public List constructSongList(List list)
	{
		List<SongBean> songData=new ArrayList();
		for(Object o:list)
		{
			songData.add((SongBean) o);

		}
		list.clear();
		return songData;
	}

	/**
	 * Method to return the artist data from Mongo DB as a list of artist beans
	 * @param list
	 * @return
	 */
	public List constructArtistList(List list)
	{
		List<ArtistDataBean> artistData=new ArrayList();
		for(Object o:list)
		{
			artistData.add((ArtistDataBean) o);

		}
		list.clear();
		return artistData;
	}

	/**
	 * Method to return the album data from Mongo DB as a list of album beans
	 * @param list
	 * @return
	 */
	public List constructAlbumList(List list)
	{
		List<AlbumBean> albumList=new ArrayList();
		for(Object o:list)
		{
			albumList.add((AlbumBean) o);

		}
		list.clear();
		return albumList;
	}

	/**
	 * Method to return the artist data fro
	 * @param list
	 * @return
	 */
	public void constructGenreList(List list)
	{
	//	System.out.println("herer3");
		GenreBean a ;
		List<GenreBean> stringData=new ArrayList();
		for(Object o:list)
		{
			stringData.add((GenreBean) o);
			a = ((GenreBean) o);
			System.out.println(a.getGenre_id());

		}
		list.clear();
	}

	public SessionFactory getHibernateSessionFactory(){
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		ServiceRegistry registry = new ServiceRegistryBuilder().applySettings(cfg.getProperties()).buildServiceRegistry();
		SessionFactory factory = cfg.buildSessionFactory(registry);
		return factory;
	}

	public List getDanceabilty(Session session, int start, int end) {
		// List l=null;

		Query query = session.createQuery("from SongData s");
		query.setFirstResult(start);
		query.setMaxResults(1000);
		List res = query.list();
		return res;
	}

	public List getSongRating(Session session, int start, int end) {
		// List l=null;

		Query query = session.createQuery("from SongRating s");
		query.setFirstResult(start);
		query.setMaxResults(end);
		List res = query.list();
		return res;
	}

	public void showDanceabilty(Session session,
			StatefulKnowledgeSession ksession, KnowledgeBase kbase) {
		UserData current = new UserData();
		current.setUserLocation("India");
		current.setUserPreferredGenre("genre_4");
		current.setUserId("user_120283");
		ksession.setGlobal("current", current);
		List list = new ArrayList();
		ksession.setGlobal("list", list);
		List ratings = getSongRating(session, 0, 10000);
		insert(ksession, ratings);
		ksession.fireAllRules();

	}

	public List showDanceabiltyList(Session session,
			StatefulKnowledgeSession ksession, KnowledgeBase kbase) {
		UserData current = new UserData();
		current.setUserLocation("India");
		current.setUserPreferredGenre("genre_4");
		current.setUserId("user_120283");
		ksession.setGlobal("current", current);
		List list = new ArrayList();
		ksession.setGlobal("list", list);
		List ratings = getSongRating(session, 0, 10000);
		insert(ksession, ratings);
		ksession.fireAllRules();
		return list;

	}

	public void performActions(Session session,
			StatefulKnowledgeSession ksession, KnowledgeBase kbase) {
		int i = 0;
		Connectivity c = new Connectivity();
		// List artist=c.getList("SongArtists", session);
		List songData;// =getDanceabilty(session);
		List location = getLocations(session, 0, 48);
		SongArtists s = new SongArtists();

		while (i < location.size()) {
			for (int start = 0; start < 500000; start = start + 10000) {
				songData = getDanceabilty(session, start, 10000);
				ksession = kbase.newStatefulKnowledgeSession();
				s.setArtistLocation((String) location.get(i));
				ksession.setGlobal("tempGlobal", s);
				// insert(ksession,artist);
				insert(ksession, songData);

				ksession.fireAllRules();

			}

		}
	}

	public static List getLocations(Session session, int start, int end) {
		Query query = session
				.createQuery("select s.artistLocation from SongArtists s group by s.artistLocation"); // group
																										// by
																										// u.userLocation");
		query.setFirstResult(start);
		query.setMaxResults(end);
		List results = query.list();
		System.out.println(results.get(0) + " " + results.get(1));
		return results;
	}

	public void locationBasedFilter() {

	}

	public static void insert(StatefulKnowledgeSession ksession, List res) {
		for (Object o : res) {
			ksession.insert(o);
		}
		// System.out.println("inserted");
	}

	// gets the database connection from getStatement
	// and updates the likes from rules
	public void updateSongLikes(StatefulKnowledgeSession ksession,
			String userID, String songID) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {

		int retrievedSongLikeCount;
		Statement state = getStatement();
		// ksession.setGlobal("statement", state);

		// MySQL component
		UserData u = new UserData();
		u.setUserId(userID);
		SongData s = new SongData();
		s.setSongId(songID);
		ksession.insert(u);
		ksession.insert(s);
		System.out.println("firing updatelikes");

		// Get and update count
		retrievedSongLikeCount = SongOperation.getSongLikedCount(songID,
				"SongList");
		System.out.println("Retrieved Count:" + retrievedSongLikeCount);
		retrievedSongLikeCount++;
		System.out.println(" Incremented Count:" + retrievedSongLikeCount);

		// MongoDB count
		SongBean sbean = new SongBean();
		sbean.setSongID(songID);
		sbean.setLikedCount(retrievedSongLikeCount);
		ksession.insert(sbean);
		ksession.getAgenda().getAgendaGroup("Likes").setFocus();
		ksession.fireAllRules();
	}

	/**
	 * Method to fetch similar artists to current artist ID
	 *
	 * @param ksession
	 * @param artistID
	 */
	public void fetchSimilarArtists(StatefulKnowledgeSession ksession,
			String artistID) {
		ArtistDataBean artist = new ArtistDataBean();
		artist.setArtistId(artistID);

		ksession.insert(artist);
		ksession.getAgenda().getAgendaGroup("Sim").setFocus();
		ksession.fireAllRules();
	}

	/**
	 * Method to fetch similar albums for the current album ID
	 * @param ksession
	 * @param albumID
	 */
	public void fetchSimilarAlbums(StatefulKnowledgeSession ksession,
			String albumID) {
		AlbumBean album = new AlbumBean();
		album.setAlbumId(albumID);
		ksession.insert(album);
		ListResult result = new ListResult();
		ksession.insert(result);
		ksession.getAgenda().getAgendaGroup("SimilarAlbums").setFocus();
		ksession.fireAllRules();
		System.out.println(result.getList().size());
	    constructAlbumList(result.getList());
	}

	/**
	 * Method to fetch similar songs to current song ID
	 *
	 * @param ksession
	 * @param artistID
	 */
	public void fetchSimilarSongs(StatefulKnowledgeSession ksession,
			String songID) {
		SongBean song = new SongBean();
		song.setSongID(songID);
		ListResult result = new ListResult();
		ksession.insert(result);
		ksession.insert(song);
		ksession.getAgenda().getAgendaGroup("SimilarSongs").setFocus();
		ksession.fireAllRules();
		System.out.println(result.getList().size());
	    constructSongList(result.getList());
	}
	/**
	 * Method to run rule to fetch artist data from Mongo DB
	 *
	 * @param ksession
	 * @param artistID
	 * @param agendaGroup
	 */
	public List fetchArtistData(StatefulKnowledgeSession ksession,
			String artistID, String agendaGroup) {
		ArtistDataBean artist = new ArtistDataBean();
		artist.setArtistId(artistID);
		ksession.insert(artist);
		ListResult result = new ListResult();
		ksession.insert(result);
		ksession.getAgenda().getAgendaGroup(agendaGroup).setFocus();
		ksession.fireAllRules();
		return (constructArtistList(result.getList()));
	}

	/**
	 * Method to fetch song data using song ID
	 *
	 * @param ksession
	 * @param songID
	 * @param agendaGroup
	 */
	public List fetchSongDataForSong(StatefulKnowledgeSession ksession,
			String songID, String agendaGroup) {
		SongBean songBean = new SongBean();
		songBean.setSongID(songID);

		ksession.insert(songBean);
		ListResult result = new ListResult();
		ksession.insert(result);
		ksession.getAgenda().getAgendaGroup(agendaGroup).setFocus();
		ksession.fireAllRules();

		return (constructSongList(result.getList()));
	}

	/**
	 * Method to fetch song data using album ID
	 * @param ksession
	 * @param albumID
	 * @param agendaGroup
	 * @return
	 */
	public List fetchSongDataForAlbum(StatefulKnowledgeSession ksession,
			String albumID, String agendaGroup) {
		SongBean songBean = new SongBean();
		songBean.setAlbumID(albumID);

		ksession.insert(songBean);
		ListResult result = new ListResult();
		ksession.insert(result);
		ksession.getAgenda().getAgendaGroup(agendaGroup).setFocus();
		ksession.fireAllRules();

		return (constructSongList(result.getList()));
	}

	/**
	 * Method to fetch album data using album id
	 * @param ksession
	 * @param albumID
	 * @param agendaGroup
	 */
	public List fetchAlbumDataForAlbum(StatefulKnowledgeSession ksession,
			String albumID, String agendaGroup) {
		AlbumBean albumBean =  new AlbumBean();
		albumBean.setAlbumId(albumID);

		ksession.insert(albumBean);
		ListResult result = new ListResult();
		ksession.insert(result);
		ksession.getAgenda().getAgendaGroup(agendaGroup).setFocus();
		ksession.fireAllRules();

		return (constructAlbumList(result.getList()));
	}

	/**
	 * Method to fetch album data using songID
	 * @param ksession
	 * @param songID
	 * @param agendaGroup
	 */
	public List fetchAlbumDataforSong(StatefulKnowledgeSession ksession,
			String songID, String agendaGroup) {
		String album_id = null;
		SessionFactory factory = getHibernateSessionFactory();
		Session session = factory.openSession();

		Query query = session.createQuery("from SongData where songId = :song_id");
		query.setParameter("song_id", songID);
		List res = query.list();
		SongData song = null;

		for(Object obj : res){
			song = (SongData) obj;
			album_id = song.getSongAlbums().getAlbumId();
		}

		return(this.fetchAlbumDataForAlbum(ksession, album_id, agendaGroup));
	}

	/**
	 * Method to fetch artist data using song ID
	 * @param ksession
	 * @param songID
	 * @param agendaGroup
	 */
	public List fetchArtistDataforSong(StatefulKnowledgeSession ksession,
			String songID, String agendaGroup) {
		String artist_id = null;
		SessionFactory factory = getHibernateSessionFactory();
		Session session = factory.openSession();

		Query query = session.createQuery("from SongData where songId = :song_id");
		query.setParameter("song_id", songID);
		List res = query.list();
		SongData song = null;

		for(Object obj : res){
			song = (SongData) obj;
			artist_id = song.getSongArtists().getArtistId();
		}

		return(this.fetchArtistData(ksession, artist_id, agendaGroup));
	}

	/**
	 * Method to fetch song data using artist ID
	 *
	 * @param ksession
	 * @param artistID
	 * @param agendaGroup
	 */
	public List fetchSongDataForArtist(StatefulKnowledgeSession ksession,
			String artistID, String agendaGroup) {
		SongBean songBean = new SongBean();
		songBean.setArtistID(artistID);

		ksession.insert(songBean);
		ListResult result = new ListResult();
		ksession.insert(result);
		ksession.getAgenda().getAgendaGroup(agendaGroup).setFocus();
		ksession.fireAllRules();
		return (constructSongList(result.getList()));
	}

	/**
	 * Method to fetch album data based on artist ID
	 * @param ksession
	 * @param artistID
	 * @param agendaGroup
	 */
	public List fetchAlbumDataForArtist(StatefulKnowledgeSession ksession,
			String artistID, String agendaGroup) {

		AlbumBean albumBean = new AlbumBean();
		albumBean.setAlbumArtist(artistID);

		ksession.insert(albumBean);
		ListResult result = new ListResult();
		ksession.insert(result);
		ksession.getAgenda().getAgendaGroup(agendaGroup).setFocus();
		ksession.fireAllRules();

		return (constructAlbumList(result.getList()));
	}

	/**
	 * Method to fetch song data using genre ID
	 *
	 * @param ksession
	 * @param genreID
	 * @param agendaGroup
	 */
	public List fetchSongDataForGenre(StatefulKnowledgeSession ksession,
			String genreID, String agendaGroup) {
		SongBean songBean = new SongBean();
		songBean.setGenreID(genreID);

		ksession.insert(songBean);
		ListResult result = new ListResult();
		ksession.insert(result);
		ksession.getAgenda().getAgendaGroup(agendaGroup).setFocus();
		ksession.fireAllRules();
		return (constructSongList(result.getList()));
	}

	/**
	 * Method to fetch artist data using genre ID
	 * @param ksession
	 * @param genreID
	 * @param agendaGroup
	 */
	public List fetchArtistDataForGenre(StatefulKnowledgeSession ksession,
			String genreID, String agendaGroup) {
		ArtistDataBean artistDataBean = new ArtistDataBean();
		artistDataBean.setArtistGenreId(genreID);

		ksession.insert(artistDataBean);
		ListResult result = new ListResult();
		ksession.insert(result);
		ksession.getAgenda().getAgendaGroup(agendaGroup).setFocus();
		ksession.fireAllRules();

		return (constructArtistList(result.getList()));
	}

	/**
	 * Method to fetch album data using genre ID
	 * @param ksession
	 * @param genreID
	 * @param agendaGroup
	 */
	public List fetchAlbumDataForGenre(StatefulKnowledgeSession ksession,
			String genreID, String agendaGroup) {
		AlbumBean albumBean = new AlbumBean();
		albumBean.setAlbumGenre(genreID);

		ksession.insert(albumBean);
		ListResult result = new ListResult();
		ksession.insert(result);
		ksession.getAgenda().getAgendaGroup(agendaGroup).setFocus();
		ksession.fireAllRules();
		return (constructAlbumList(result.getList()));
	}

	/**
	 * Method to fetch artist data based on location
	 * @param ksession
	 * @param location
	 * @param agendaGroup
	 */
	public List fetchSongDataForLocation(StatefulKnowledgeSession ksession,
			String location, String agendaGroup) {
		SongBean songBean = null;

		SessionFactory factory = getHibernateSessionFactory();
		Session session = factory.openSession();

		Query query = session.createQuery("from SongArtists where artistLocation = :locname");
		query.setParameter("locname", location);
		List res = query.list();
		SongArtists artist = null;

		for(Object obj : res){
			artist = (SongArtists) obj;
			songBean = new SongBean();
			songBean.setArtistID(artist.getArtistId());
			ksession.insert(songBean);
		}

		ListResult result = new ListResult();
		ksession.insert(result);

		//ksession.insert(songBean);
		ksession.getAgenda().getAgendaGroup(agendaGroup).setFocus();
		ksession.fireAllRules();

		return (constructSongList(result.getList()));
	}

	/**
	 * Method to fetch album data based on location
	 * @param ksession
	 * @param location
	 * @param agendaGroup
	 */
	public List fetchAlbumDataForLocation(StatefulKnowledgeSession ksession,
			String location, String agendaGroup) {

		AlbumBean albumBean = null;

		SessionFactory factory = getHibernateSessionFactory();
		Session session = factory.openSession();

		Query query = session.createQuery("from SongArtists where artistLocation = :locname");
		query.setParameter("locname", location);
		List res = query.list();
		SongArtists artist = null;

		for(Object obj : res){
			artist = (SongArtists) obj;
			//songBean = new SongBean();
			//songBean.setArtistID(artist.getArtistId());
			albumBean = new AlbumBean();
			albumBean.setAlbumArtist(artist.getArtistId());
			ksession.insert(albumBean);
		}

		ListResult result = new ListResult();
		ksession.insert(result);

		//ksession.insert(songBean);
		ksession.getAgenda().getAgendaGroup(agendaGroup).setFocus();
		ksession.fireAllRules();

		return (constructAlbumList(result.getList()));
	}

	/**
	 * Method to fetch artist data based on artist location
	 * @param ksession
	 * @param location
	 * @param agendaGroup
	 */
	public List fetchArtistDataForLocation(StatefulKnowledgeSession ksession,
			String location, String agendaGroup) {

		ArtistDataBean artistDataBean = null;

		SessionFactory factory = getHibernateSessionFactory();
		Session session = factory.openSession();

		Query query = session.createQuery("from SongArtists where artistLocation = :locname");
		query.setParameter("locname", location);
		List res = query.list();
		SongArtists artist = null;

		for(Object obj : res){
			artist = (SongArtists) obj;
			//songBean = new SongBean();
			//songBean.setArtistID(artist.getArtistId());
			//albumBean = new AlbumBean();
			//albumBean.setAlbumArtist(artist.getArtistId());
			artistDataBean = new ArtistDataBean();
			artistDataBean.setArtistId(artist.getArtistId());
			ksession.insert(artistDataBean);
		}

		ListResult result = new ListResult();
		ksession.insert(result);

		//ksession.insert(songBean);
		ksession.getAgenda().getAgendaGroup(agendaGroup).setFocus();
		ksession.fireAllRules();

		return (constructArtistList(result.getList()));
	}

	public void fetchSongDataBetweenYears(StatefulKnowledgeSession ksession,
			int start, int end, String agendaGroup) {
		SongBean songBean = new SongBean();
		songBean.setReleaseYear(start);

		SessionFactory factory = getHibernateSessionFactory();
		Session session = factory.openSession();
		Query query = session.createQuery("from SongData where yearOfRelease between :from and :to");
		query.setParameter("from", start);
		query.setParameter("to", end);
		List res = query.list();
		SongData song = null;

		for(Object o : res){
			song = (SongData) o;
			ksession.insert(o);
		}

		ksession.insert(songBean);
		ksession.getAgenda().getAgendaGroup(agendaGroup).setFocus();
		ksession.fireAllRules();
	}

	public void updateSongRating(StatefulKnowledgeSession ksession,
			String userID, String songID, Integer songRating)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {

		int count;
		Statement state = getStatement();
		ksession.setGlobal("statement", state);

		SongRatingId s = new SongRatingId();
		s.setSongId(songID);
		s.setUserId(userID);
		s.setSongRating(songRating);
		ksession.insert(s);
		System.out.println("firing  updateSongRatiing method");
		ksession.getAgenda().getAgendaGroup("Ratings").setFocus();
		ksession.fireAllRules();
	}

	public void updateArtistLiked(StatefulKnowledgeSession ksession,
			String userID, String artistID) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {

		int count;
		Statement state = getStatement();
		ksession.setGlobal("statement", state);

		UserData u = new UserData();
		u.setUserId(userID);
		SongArtists s = new SongArtists();
		s.setArtistId(artistID);
		ksession.insert(s);
		ksession.insert(u);
		System.out.println("firing updateArtistLiked Method ");
		ksession.getAgenda().getAgendaGroup("ArtistLiked").setFocus();
		ksession.fireAllRules();
	}

	public void updateAlbumLiked(StatefulKnowledgeSession ksession,
			String userID, String albumID) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {

		int count;
		Statement state = getStatement();
		ksession.setGlobal("statement", state);

		UserData u = new UserData();
		u.setUserId(userID);
		SongAlbums a = new SongAlbums();
		a.setAlbumId(albumID);
		ksession.insert(u);
		ksession.insert(a);
		System.out
				.println("firing from updateAlbumLiked method whose parameters are session,userid,albumid");
		ksession.getAgenda().getAgendaGroup("AlbumLiked").setFocus();
		ksession.fireAllRules();
	}

	// returns database statement
	public Statement getStatement() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {
		return null;
		/*Configuration conf;
		conf = new Configuration();
		Class.forName(conf.getDriver()).newInstance();
		Connection con = null;
		con = conf.getConnection();
		Statement st = con.createStatement();
		return st;*/

	}

	// user details

		/**
		 * Fetch the liked album for a userid
		 */
		public void fetchAlbumsLiked(StatefulKnowledgeSession ksession, String userID)
		{
			ListResult result = new ListResult();
			ksession.insert(result);
			UserBean user = new UserBean();
			user.setUserId(userID);
			ksession.insert(user);
			ksession.getAgenda().getAgendaGroup("FetchAlbumsLiked").setFocus();
			//ksession.getAgenda().getAgendaGroup("Rule").setFocus();
			ksession.fireAllRules();
			constructAlbumList(result.getList());

		}

		/**
		 * Fetch the liked album for a userid
		 */
		public void fetchAlbumsShared(StatefulKnowledgeSession ksession, String userID)
		{
			ListResult result = new ListResult();
			ksession.insert(result);
			UserBean user = new UserBean();
			user.setUserId(userID);
			ksession.insert(user);
			ksession.getAgenda().getAgendaGroup("FetchAlbumsShared").setFocus();
			ksession.fireAllRules();
			constructAlbumList(result.getList());

		}

		/**
		 * Fetch the purchased album for a userid
		 */
		public void fetchAlbumsPurchased(StatefulKnowledgeSession ksession, String userID)
		{
			ListResult result = new ListResult();
			ksession.insert(result);
			UserBean user = new UserBean();
			user.setUserId(userID);
			ksession.insert(user);
			ksession.getAgenda().getAgendaGroup("FetchAlbumsPurchased").setFocus();
			ksession.fireAllRules();

		}
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		/**
		 * Fetch the liked songs for a userid
		 */
		public void fetchSongsLiked(StatefulKnowledgeSession ksession, String userID)
		{
			ListResult result = new ListResult();
			ksession.insert(result);
			UserBean user = new UserBean();
			user.setUserId(userID);
			ksession.insert(user);
			ksession.getAgenda().getAgendaGroup("FetchSongsLiked").setFocus();
			ksession.fireAllRules();
			constructSongList(result.getList());
		}

		/**
		 * Fetch the liked songs for a userid
		 */
		public void fetchSongsShared(StatefulKnowledgeSession ksession, String userID)
		{
			ListResult result = new ListResult();
			ksession.insert(result);
			UserBean user = new UserBean();
			user.setUserId(userID);
			ksession.insert(user);
			ksession.getAgenda().getAgendaGroup("FetchSongsShared").setFocus();
			ksession.fireAllRules();
			constructSongList(result.getList());
		}

		/**
		 * Fetch the purchased songs for a userid
		 */
		public void fetchSongsPurchased(StatefulKnowledgeSession ksession, String userID)
		{
			ListResult result = new ListResult();
			ksession.insert(result);
			UserBean user = new UserBean();
			user.setUserId(userID);
			ksession.insert(user);
			ksession.getAgenda().getAgendaGroup("FetchSongsPurchased").setFocus();
			ksession.fireAllRules();
			constructSongList(result.getList());
		}

		/**
		 * Fetch the liked artists for a userid
		 */
		public void fetchArtistsLiked(StatefulKnowledgeSession ksession, String userID)
		{
			ListResult result = new ListResult();
			ksession.insert(result);
			UserBean user = new UserBean();
			user.setUserId(userID);
			ksession.insert(user);
			ksession.getAgenda().getAgendaGroup("FetchArtistsLiked").setFocus();
			ksession.fireAllRules();
			constructArtistList(result.getList());
		}


		/**
		 * Fetch the list of song gnere liked by the user
		 */
		public void fetchSongsGenreLiked(StatefulKnowledgeSession ksession, String userID)
		{
			ListResult result = new ListResult();
			ksession.insert(result);
			UserBean user = new UserBean();
			user.setUserId(userID);
			ksession.insert(user);
			ksession.getAgenda().getAgendaGroup("FetchSongsGenreLiked").setFocus();
			ksession.fireAllRules();
			constructGenreList(result.getList());
		}


		/**
		 * Fetch the list of song gnere shared by the user
		 */
		public void fetchSongsGenreShared(StatefulKnowledgeSession ksession, String userID)
		{
			ListResult result = new ListResult();
			ksession.insert(result);
			UserBean user = new UserBean();
			user.setUserId(userID);
			ksession.insert(user);
			ksession.getAgenda().getAgendaGroup("FetchSongsGenreShared").setFocus();
			ksession.fireAllRules();
			constructGenreList(result.getList());
		}

		/**
		 * Fetch the list of song gnere liked by the user
		 */
		public void fetchSongsGenrePurchased(StatefulKnowledgeSession ksession, String userID)
		{
			ListResult result = new ListResult();
			ksession.insert(result);
			UserBean user = new UserBean();
			user.setUserId(userID);
			ksession.insert(user);
			ksession.getAgenda().getAgendaGroup("FetchSongsGenrePurchased").setFocus();
			ksession.fireAllRules();
			constructGenreList(result.getList());
		}



		//////////////album///////////////////////different
		/**
		 * Fetch the list of song gnere liked by the user
		 */
		public void fetchAlbumsGenreLiked(StatefulKnowledgeSession ksession, String userID)
		{
			ListResult result = new ListResult();
			ksession.insert(result);
			UserBean user = new UserBean();
			List<GenreBean> gb = new ArrayList<GenreBean>();
			user.setUserId(userID);
			ksession.insert(user);
			ksession.getAgenda().getAgendaGroup("FetchAlbumsGenreLiked").setFocus();
			ksession.fireAllRules();
			System.out.println();

			gb = user.getSonggenreliked();
			System.out.println(" outside" + gb.get(0).getGenre_id());
		}


		/**
		 * Fetch the list of album gnere shared by the user
		 */
		public void fetchAlbumsGenreShared(StatefulKnowledgeSession ksession, String userID)
		{
			ListResult result = new ListResult();
			ksession.insert(result);
			UserBean user = new UserBean();
			user.setUserId(userID);
			ksession.insert(user);
			ksession.getAgenda().getAgendaGroup("FetchAlbumsGenreShared").setFocus();
			ksession.fireAllRules();
			constructGenreList(result.getList());
		}

		/**
		 * Fetch the list of album gnere liked by the user
		 */
		public void fetchAlbumsGenrePurchased(StatefulKnowledgeSession ksession, String userID)
		{

			UserBean user = new UserBean();
			ListResult result = new ListResult();
			ksession.insert(result);
			user.setUserId(userID);
			ksession.insert(user);
			ksession.getAgenda().getAgendaGroup("FetchAlbumsGenrePurchased").setFocus();
			ksession.fireAllRules();
			//constructGrossedSongList(result.getList());
			constructGenreList(result.getList());
		}


		/**
		 * Fetch the top 100 most grossed song
		 */
		public List fetchMostGrossedSongs(StatefulKnowledgeSession ksession)
		{
		//	UserBean user = new UserBean();
			ListResult result = new ListResult();
		//	user.setUserId(userID);
			ksession.insert(result);
		//	ksession.insert(user);
			ksession.getAgenda().getAgendaGroup("FetchMostGrossedSongs").setFocus();
			ksession.fireAllRules();
			System.out.println(result.getList().size());
			List<SongBean> list=constructSongList(result.getList());
			return list;
		}

		/**
		 * Fetch the top 400 most popular ALbum
		 */
		public List fetchMostPopularAlbums(StatefulKnowledgeSession ksession)
		{
		//	UserBean user = new UserBean();
			ListResult result = new ListResult();
		//	user.setUserId(userID);
			ksession.insert(result);
		//	ksession.insert(user);
			ksession.getAgenda().getAgendaGroup("FetchMostPopularAlbums").setFocus();
			ksession.fireAllRules();
			System.out.println(result.getList().size());
			List<AlbumBean> list=constructAlbumList(result.getList());
			return list;
		}



		/**
		 * Fetch the top 100 most grossed album
		 */
		public List fetchMostGrossedAlbums(StatefulKnowledgeSession ksession)
		{

		//	UserBean user = new UserBean();
			ListResult result = new ListResult();
		//	user.setUserId(userID);
			ksession.insert(result);
		//	ksession.insert(user);
			ksession.getAgenda().getAgendaGroup("FetchMostGrossedAlbums").setFocus();
			ksession.fireAllRules();
			System.out.println("here in album");
			System.out.println(result.getList().size());
			List<AlbumBean> list=constructAlbumList(result.getList());
			return list;
		}


}
