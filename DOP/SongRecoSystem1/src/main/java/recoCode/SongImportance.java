package recoCode;


import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import newEntities.SongData;
import newEntities.SongRating;
import newEntities.SongsLiked;
import newEntities.SongsPurchased;
import newEntities.SongsShared;
import newEntities.UserData;
import newEntities.UserPlaycount;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class SongImportance {

	HashMap<String, UserData> userData = new HashMap<String, UserData>();
	HashMap<String, SongData> songData = new HashMap<String, SongData>();
	HashSet<String> shared = new HashSet<String>();
	HashSet<String> liked = new HashSet<String>();
	HashSet<String> purchased = new HashSet<String>();
	HashSet<String> played = new HashSet<String>();
	HashSet<String> rated = new HashSet<String>();


	Configuration cfg = null;
	SessionFactory factory = null;
	ServiceRegistry registry = null;

	public SongImportance() {
		initializeParams();
	}

	public void getAllData(){
		this.getLikedData();
		System.out.println("Got likes");
		this.getPlayedData();
		System.out.println("Got played");
		this.getPurchasedData();
		System.out.println("Got purchased");
		this.getRatingData();
		System.out.println("Got rated");
		this.getSharedData();
		System.out.println("Got shared");
		this.getSongData();
		System.out.println("Got songs");
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
		SongsShared share = null;
		int size;

		Session session = factory.openSession();
		query = session.createQuery("from SongsShared");
		List res = query.list();
		size = res.size();

		for (int i = 0; i < size; i++) {
			share = (SongsShared) res.get(i);
			shared.add(share.getId().getUserId() + share.getId().getSongId());
		}
		session.close();
	}

	public void getLikedData() {
		Query query = null;
		SongsLiked like = null;
		int size;

		Session session = factory.openSession();
		query = session.createQuery("from SongsLiked");
		List res = query.list();
		size = res.size();

		for (int i = 0; i < size; i++) {
			like = (SongsLiked) res.get(i);
			liked.add(like.getId().getUserId() + like.getId().getSongId());
		}
		session.close();
	}

	public void getRatingData() {
		Query query = null;
		SongRating rate = null;
		int size;

		Session session = factory.openSession();
		query = session.createQuery("from SongRating");
		List res = query.list();
		size = res.size();

		for (int i = 0; i < size; i++) {
			rate = (SongRating) res.get(i);
			rated.add(rate.getId().getUserId() + rate.getId().getSongId());
		}
		session.close();
	}

	public void getPlayedData() {
		Query query = null;
		UserPlaycount play = null;
		int size;

		Session session = factory.openSession();
		query = session.createQuery("from UserPlaycount");
		List res = query.list();
		size = res.size();

		for (int i = 0; i < size; i++) {
			play = (UserPlaycount) res.get(i);
			played.add(play.getId().getUserId() + play.getId().getSongId());
		}
		session.close();
	}

	public void getPurchasedData() {
		Query query = null;
		SongsPurchased buy = null;
		int size;

		Session session = factory.openSession();
		query = session.createQuery("from SongsPurchased");
		List res = query.list();
		size = res.size();

		for (int i = 0; i < size; i++) {
			buy = (SongsPurchased) res.get(i);
			purchased.add(buy.getId().getUserId()+buy.getId().getSongId());
		}
		session.close();
	}

	/**
	 * Check whether user has shared song
	 * @param userID
	 * @param songID
	 * @return
	 */
	public boolean hasSharedSong(String userID, String songID) {
		if(shared.contains(userID+songID)){
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Check whether user has liked song
	 * @param userID
	 * @param songID
	 * @return
	 */
	public boolean hasLikedSong(String userID, String songID) {
		if(liked.contains(userID+songID)){
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Check whether user has rated song
	 * @param userID
	 * @param songID
	 * @return
	 */
	public boolean hasRatedSong(String userID, String songID) {
		if(rated.contains(userID+songID)){
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
	public boolean hasPurchasedSong(String userID, String songID) {
		if(purchased.contains(userID+songID)){
			return true;
		} else {
			return false;
		}
	}

	public boolean hasPlayedSong(String userID, String songID) {
		if(played.contains(userID+songID)){
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Get songs data
	 */
	public void getSongData() {
		Query query = null;
		int size;
		SongData song = null;

		Session session = factory.openSession();
		query = session.createQuery("from SongData");
		List songs = query.list();
		size = songs.size();

		for (int i = 0; i < size; i++) {
			song = (SongData) songs.get(i);
			songData.put(song.getSongId(), song);
		}
		session.close();
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

	public void calculateImportance() {
		Set<String> songID = songData.keySet();
		Set<String> userID = userData.keySet();
		String maxSong;
		double maxSim,sim;

		for (String u : userID) {
			maxSim = 0;
			maxSong = "";
			System.out.println("for user: " + u);
			for (String s : songID) {
				sim = 0;
				if(userData.get(u).getUserPreferredArtist().equals(songData.get(s).getSongArtists())){
					sim += 1*0.25;
				}

				if(userData.get(u).getUserPreferredGenre().equals(songData.get(s).getGenre())){
					sim += 1*0.25;
				}
				if(hasLikedSong(u, s)){
					sim += 1*0.1;
				}
				if(hasPlayedSong(u, s)){
					sim += 1*0.1;
				}
				if(hasPurchasedSong(u, s)){
					sim += 1*0.1;
				}
				if(hasRatedSong(u, s)){
					sim += 1*0.1;
				}
				if(hasSharedSong(u, s)){
					sim += 1*0.1;
				}

				if(sim > maxSim){
					maxSim = sim;
					maxSong = s;
				}
			}

			System.out.println("Most imp for " + u + " is " + maxSong);

		}
	}

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		SongImportance s = new SongImportance();

		/*s.getUserData(); System.out.println("Got user data");
		s.getSongData(); System.out.println("Got song data");*/
		s.getAllData();
		System.out.println("Calculating importance");
		s.calculateImportance();

		/*s.hasSharedSong("user_6007", "song_10628");
		System.out.println("Got song shared data");*/
		long end = System.currentTimeMillis();
		System.out.println("Time of exec: " + (end - start));
	}

}
