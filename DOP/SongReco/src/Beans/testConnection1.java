package Beans;

import newEntities.UserData;

import org.drools.runtime.StatefulKnowledgeSession;
import org.hibernate.Session;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import MongoEntities.AlbumBean;
import MongoEntities.SongBean;

import com.sample.Connectivity;
import com.sample.InvokeRules;

import dop.musicreco.MongoConnection;
import dop.musicreco.MySql;

import newEntities.*;
public class testConnection1 {
	Configuration cfg = null;
	SessionFactory factory = null;
	ServiceRegistry registry = null;

	public void initializeParams(){
		cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		registry = new ServiceRegistryBuilder().applySettings(cfg.getProperties()).buildServiceRegistry();
		factory = cfg.buildSessionFactory(registry);
	}

	public List testQuery(String varValue) {
		Session session = factory.openSession();
		Query query = session.createQuery("from UserData u where u.loginName=:varValue ").setParameter("varValue", varValue);
		//query.setParameter("varValue", varValue);
		List list = query.list();
		//UserData user=null;
		for (Iterator it = list.iterator(); it.hasNext();) {
			Users.user = (UserData) it.next();
			//System.out.println("user:"+Users.user.getUserId());
		}
		return list;
	}

	public void testQuerySongs() throws Exception {
		/*Session session = factory.openSession();
		Query query = session.createQuery("from SongData");
		query.setFirstResult(0);
		query.setMaxResults(10);*/

		Connectivity connect = new Connectivity();
		connect.initializeParams();
		StatefulKnowledgeSession ksession = connect.getKnowledgeSession();
		InvokeRules ruleInvoker = new InvokeRules();
		MySql.getConnection1("root", "tejas");
		MongoConnection.getConnection("songrecosystem", "localhost");
		Users.listSongs = ruleInvoker.fetchMostGrossedSongs(ksession);
		System.out.println("List size:"+Users.listSongs.size());

	}

	public void testQueryAlbums() throws Exception{
		Connectivity connect = new Connectivity();
		connect.initializeParams();
		StatefulKnowledgeSession ksession = connect.getKnowledgeSession();
		InvokeRules ruleInvoker = new InvokeRules();
		MySql.getConnection1("root", "tejas");
		MongoConnection.getConnection("songrecosystem", "localhost");
		Users.listAlbums = ruleInvoker.fetchMostGrossedAlbums(ksession);
		System.out.println("List size:"+Users.listAlbums.size());
	}

	public void testQueryArtists(){
		Session session = factory.openSession();
		Query query = session.createQuery("from SongArtists");
		query.setFirstResult(0);
		query.setMaxResults(10);
		Users.listArtists = query.list();
	}
	/*public List testQuery1(String varValue) {
		Session session = factory.openSession();
		Query query = session.createQuery("from UserData where userData = 'user_39832' ");
		//query.setParameter("user", "user_39832");
		List list = query.list();
		AlbumRating album=null;
		for (Iterator it = list.iterator(); it.hasNext();) {
			album = (AlbumRating) it.next();
			System.out.println(album.getId().getAlbumId());
		}
		return list;

	}*/

	public static void main(String args[])
	{
		//Connectivity connect = new Connectivity();
		//connect.initializeParams();
		try {
			//c.getArtistLiked("user_100","artist_1");
			//c.getAlbumLiked("user_100","album_1");
		//	c.getLikes("user_1", "song_64243");
			//connect.getSimilarArtists("artist_417501");

			//StatefulKnowledgeSession ksession = connect.getKnowledgeSession();
			//InvokeRules ruleInvoker = new InvokeRules();
			//ruleInvoker.fetchSongDataForArtist(ksession, "artist_417501", "SongFromArtist");
			//List<SongBean> l=ruleInvoker.fetchSongDataForSong(ksession, "song_417501", "SongData");
			//ruleInvoker.fetchSongDataForGenre(ksession, "genre_8", "SongFromGenre");
			//ruleInvoker.fetchSongDataForLocation(ksession, "India", "SongFromArtist");
			//ruleInvoker.fetchSongDataForArtist(ksession, connect.getPreferredArtist("user_99999"), "SongFromArtist");
			//ruleInvoker.fetchSongDataBetweenYears(ksession, 1995, 2000, "SongFromYearRange");
			//ruleInvoker.fetchArtistData(ksession, "artist_417501", "ArtistDetails");
			//ruleInvoker.fetchAlbumDataForArtist(ksession, "artist_417501", "AlbumFromArtist");
			//ruleInvoker.fetchAlbumDataForAlbum(ksession, "album_417501", "AlbumDetails");
			//ruleInvoker.fetchAlbumDataforSong(ksession, "song_417501", "AlbumDetails");
			//ruleInvoker.fetchArtistDataforSong(ksession, "song_417501", "ArtistDetails");
			//ruleInvoker.fetchAlbumDataForGenre(ksession, "genre_8", "AlbumFromGenre");
			//ruleInvoker.fetchArtistDataForGenre(ksession, "genre_8", "ArtistFromGenre");
			//ruleInvoker.fetchAlbumDataForLocation(ksession, "India", "AlbumFromArtist");
			//System.out.println("SongData:"+l.get(0).getSongID());


				//MySql.getConnection1("root", "tejas");
				//MongoConnection.getConnection("songrecosystem", "localhost");
				//c.getArtistLiked("user_100","artist_1");
				//c.getAlbumLiked("user_100","album_1");
			//	c.getLikes("user_1", "song_64243");
				//connect.getSimilarArtists("artist_417501");


				/// mongo
				/*System.out.println("---------------------------------------------");
				List<SongBean> list=ruleInvoker.fetchMostGrossedSongs(ksession);
				System.out.println("list size:"+list.size());
				List<AlbumBean> list1=ruleInvoker.fetchMostGrossedAlbums(ksession);
				System.out.println("list1 size"+list1.size());
				List<AlbumBean> list2=ruleInvoker.fetchMostPopularAlbums(ksession);
				//System.out.println("---------------------------------------------");*/
				/////
				testConnection1 test=new testConnection1();
				test.testQuerySongs();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
