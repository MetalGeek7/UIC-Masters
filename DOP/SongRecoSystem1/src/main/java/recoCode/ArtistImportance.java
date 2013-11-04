package recoCode;


import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import newEntities.AlbumRating;
import newEntities.AlbumsLiked;
import newEntities.ArtistRating;
import newEntities.ArtistsLiked;
import newEntities.SongAlbums;
import newEntities.SongArtists;
import newEntities.UserData;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;


public class ArtistImportance {

	HashMap<String, UserData> userData = new HashMap<String, UserData>();
	HashMap<String, SongArtists> artistData = new HashMap<String, SongArtists>();

	HashSet<String> liked = new HashSet<String>();
	HashSet<String> rated = new HashSet<String>();

	Configuration cfg = null;
	SessionFactory factory = null;
	ServiceRegistry registry = null;

	public ArtistImportance() {
		initializeParams();
	}

	public void initializeParams() {
		cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		registry = new ServiceRegistryBuilder().applySettings(
				cfg.getProperties()).buildServiceRegistry();
		factory = cfg.buildSessionFactory(registry);
	}

	public void getAllData(){
		this.getLikedData();
		System.out.println("Got likes");
		this.getRatingData();
		System.out.println("Got rated");
		this.getAlbumData();
		System.out.println("Got albums");
		this.getUserData();
		System.out.println("Got users");
	}

	public void getLikedData() {
		Query query = null;
		ArtistsLiked like = null;
		int size;

		Session session = factory.openSession();
		query = session.createQuery("from ArtistsLiked");
		List res = query.list();
		size = res.size();

		for (int i = 0; i < size; i++) {
			like = (ArtistsLiked) res.get(i);
			liked.add(like.getId().getUserId() + like.getId().getArtistId());
		}
		session.close();
	}

	public void getRatingData() {
		Query query = null;
		ArtistRating rate = null;
		int size;

		Session session = factory.openSession();
		query = session.createQuery("from ArtistRating");
		List res = query.list();
		size = res.size();

		for (int i = 0; i < size; i++) {
			rate = (ArtistRating) res.get(i);
			rated.add(rate.getId().getUserId() + rate.getId().getArtistId());
		}
		session.close();
	}

	public void getUserData() {
		Query query = null;
		int size;
		UserData user = null;

		Session session = factory.openSession();
		query = session.createQuery("from UserData");
		List users = query.list();
		size = users.size();

		for (int i = 0; i < size; i++) {
			user = (UserData) users.get(i);
			userData.put(user.getUserId(), user);
		}
		session.close();
	}

	public void getAlbumData() {
		Query query = null;
		int size;
		SongArtists artist = null;

		Session session = factory.openSession();
		query = session.createQuery("from SongArtists");
		List albums = query.list();
		size = albums.size();

		for (int i = 0; i < size; i++) {
			artist = (SongArtists) albums.get(i);
			artistData.put(artist.getArtistId(), artist);
		}
		session.close();
	}

	public boolean hasLikedArtist(String userID, String artistID) {
		if(liked.contains(userID+artistID)){
			return true;
		} else {
			return false;
		}
	}

	public boolean hasRatedArtist(String userID, String artistID) {
		if(rated.contains(userID+artistID)){
			return true;
		} else {
			return false;
		}
	}

	public void calculateImportance() {
		Set<String> artistID = artistData.keySet();
		Set<String> userID = userData.keySet();
		String maxArtist;
		double maxSim,sim;

		for (String u : userID) {
			maxSim = 0;
			maxArtist = "";
			System.out.println("for user: " + u);
			for (String a : artistID) {
				sim = 0;
				if(userData.get(u).getUserPreferredArtist().equals(artistData.get(a).getArtistId())){
					sim += 1*0.3;
				}

				if(userData.get(u).getUserPreferredGenre().equals(artistData.get(a).getGenre())){
					sim += 1*0.3;
				}
				if(hasLikedArtist(u, a)){
					sim += 1*0.2;
				}

				if(hasRatedArtist(u, a)){
					sim += 1*0.2;
				}

				if(sim > maxSim){
					maxSim = sim;
					maxArtist = a;
				}
			}

			System.out.println("Most imp for " + u + " is " + maxArtist);

		}
	}

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		ArtistImportance a = new ArtistImportance();

		/*s.getUserData(); System.out.println("Got user data");
		s.getSongData(); System.out.println("Got song data");*/
		a.getAllData();

		System.out.println("Calculating importance");

		a.calculateImportance();

		/*s.hasSharedSong("user_6007", "song_10628");
		System.out.println("Got song shared data");*/
		long end = System.currentTimeMillis();
		System.out.println("Time of exec: " + (end - start));
	}

}
