package recoCode;


import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import newEntities.AlbumRating;
import newEntities.AlbumsLiked;
import newEntities.AlbumsPurchased;
import newEntities.AlbumsShared;
import newEntities.SongAlbums;
import newEntities.UserData;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class AlbumImportance {

	HashMap<String, UserData> userData = new HashMap<String, UserData>();
	HashMap<String, SongAlbums> albumData = new HashMap<String, SongAlbums>();

	HashSet<String> shared = new HashSet<String>();
	HashSet<String> liked = new HashSet<String>();
	HashSet<String> purchased = new HashSet<String>();
	HashSet<String> rated = new HashSet<String>();

	Configuration cfg = null;
	SessionFactory factory = null;
	ServiceRegistry registry = null;

	public AlbumImportance() {
		initializeParams();
	}

	public void getAllData(){
		this.getLikedData();
		System.out.println("Got likes");
		this.getPurchasedData();
		System.out.println("Got purchased");
		this.getRatingData();
		System.out.println("Got rated");
		this.getSharedData();
		System.out.println("Got shared");
		this.getAlbumData();
		System.out.println("Got albums");
		this.getUserData();
		System.out.println("Got users");
	}

	/**
	 * Initialize song params
	 */
	public void initializeParams() {
		cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		registry = new ServiceRegistryBuilder().applySettings(
				cfg.getProperties()).buildServiceRegistry();
		factory = cfg.buildSessionFactory(registry);
	}

	public void getSharedData() {
		Query query = null;
		AlbumsShared share = null;
		int size;

		Session session = factory.openSession();
		query = session.createQuery("from AlbumsShared");
		List res = query.list();
		size = res.size();

		for (int i = 0; i < size; i++) {
			share = (AlbumsShared) res.get(i);
			shared.add(share.getId().getUserId() + share.getId().getAlbumId());
		}
		session.close();
	}

	public void getLikedData() {
		Query query = null;
		AlbumsLiked like = null;
		int size;

		Session session = factory.openSession();
		query = session.createQuery("from AlbumsLiked");
		List res = query.list();
		size = res.size();

		for (int i = 0; i < size; i++) {
			like = (AlbumsLiked) res.get(i);
			liked.add(like.getId().getUserId() + like.getId().getAlbumId());
		}
		session.close();
	}

	public void getRatingData() {
		Query query = null;
		AlbumRating rate = null;
		int size;

		Session session = factory.openSession();
		query = session.createQuery("from AlbumRating");
		List res = query.list();
		size = res.size();

		for (int i = 0; i < size; i++) {
			rate = (AlbumRating) res.get(i);
			rated.add(rate.getId().getUserId() + rate.getId().getAlbumId());
		}
		session.close();
	}

	public void getPurchasedData() {
		Query query = null;
		AlbumsPurchased buy = null;
		int size;

		Session session = factory.openSession();
		query = session.createQuery("from AlbumsPurchased");
		List res = query.list();
		size = res.size();

		for (int i = 0; i < size; i++) {
			buy = (AlbumsPurchased) res.get(i);
			purchased.add(buy.getId().getUserId()+buy.getId().getAlbumId());
		}
		session.close();
	}

	public boolean hasSharedAlbum(String userID, String albumID) {
		if(shared.contains(userID+albumID)){
			return true;
		} else {
			return false;
		}
	}

	public boolean hasLikedAlbum(String userID, String albumID) {
		if(liked.contains(userID+albumID)){
			return true;
		} else {
			return false;
		}
	}

	public boolean hasRatedAlbum(String userID, String albumID) {
		if(rated.contains(userID+albumID)){
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Check whether user has purchased song
	 * @param userID
	 * @param songID
	 * @return
	 */
	public boolean hasPurchasedAlbum(String userID, String albumID) {
		if(purchased.contains(userID+albumID)){
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Get user data
	 */
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
		SongAlbums album = null;

		Session session = factory.openSession();
		query = session.createQuery("from SongAlbums");
		List albums = query.list();
		size = albums.size();

		for (int i = 0; i < size; i++) {
			album = (SongAlbums) albums.get(i);
			albumData.put(album.getAlbumId(), album);
		}
		session.close();
	}

	public void calculateImportance() {
		Set<String> albumID = albumData.keySet();
		Set<String> userID = userData.keySet();
		String maxAlbum;
		double maxSim,sim;

		for (String u : userID) {
			maxSim = 0;
			maxAlbum = "";
			System.out.println("for user: " + u);
			for (String a : albumID) {
				sim = 0;
				if(userData.get(u).getUserPreferredArtist().equals(albumData.get(a).getSongArtists())){
					sim += 1*0.3;
				}

				if(userData.get(u).getUserPreferredGenre().equals(albumData.get(a).getGenre())){
					sim += 1*0.3;
				}
				if(hasLikedAlbum(u, a)){
					sim += 1*0.1;
				}

				if(hasPurchasedAlbum(u, a)){
					sim += 1*0.1;
				}
				if(hasRatedAlbum(u, a)){
					sim += 1*0.1;
				}
				if(hasSharedAlbum(u, a)){
					sim += 1*0.1;
				}

				if(sim > maxSim){
					maxSim = sim;
					maxAlbum = a;
				}
			}

			System.out.println("Most imp for " + u + " is " + maxAlbum);

		}
	}

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		AlbumImportance a = new AlbumImportance();

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
