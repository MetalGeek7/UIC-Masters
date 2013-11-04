package dop.musicreco;

import java.util.ArrayList;
import java.util.List;

import newEntities.AlbumsLiked;
import newEntities.SongAlbums;
import newEntities.SongArtists;
import newEntities.SongData;
import newEntities.SongRatingId;
import newEntities.SongsLiked;
import newEntities.UserData;

import org.drools.KnowledgeBase;
import org.drools.runtime.StatefulKnowledgeSession;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import dop.musicreco.MySql;
import com.mysql.jdbc.PreparedStatement;

import MongoEntities.AlbumBean;
import MongoEntities.ArtistDataBean;
import MongoEntities.GenreBean;
import MongoEntities.SongBean;
//import MongoEntities.UserBean;

import dop.musicreco.SongOperation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class UserOperation{

	public static SessionFactory getHibernateSessionFactory(){
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		ServiceRegistry registry = new ServiceRegistryBuilder().applySettings(cfg.getProperties()).buildServiceRegistry();
		SessionFactory factory = cfg.buildSessionFactory(registry);
		return factory;
	}



	/*public static void fetchSongsLiked(String userID) {



		SessionFactory factory = getHibernateSessionFactory();
		Session session = factory.openSession();

		String hql = "SELECT distinct * FROM SongsLiked where songId = :userID";
		Query query = session.createQuery(hql);
		List res = query.list();

		Query query = session.createQuery(" from SongsLiked where user_Id = :userID");
		query.setParameter("userID", userID);
		List res = query.list();
		SongsLiked songliked = null;

		for(Object obj : res){
			songliked = (SongsLiked) obj;
			System.out.println(songliked.getId().getSongId());
		}
	}
	*/

		/**
		 * used to fetch albumid which is been liked by the user
		 * @param userID
		 */
		public static List<Object> fetchAlbumsLiked(String userID) {

			String album_id = null;
			Connection conn = MySql.conn;
			List<Object> albumsLiked = new ArrayList<Object>();
			Statement s1;
			String stm;
			ResultSet rs;
			java.sql.PreparedStatement prest = null;
			try {
				s1 = conn.createStatement();
				stm = "select distinct(album_id) from albums_liked where user_id =?";
				prest = conn.prepareStatement(stm);
				prest.setString(1,userID);
				rs = prest.executeQuery();
				while(rs.next())
				{
					album_id = rs.getString(1);
					System.out.println(" The album_id liked is : " + album_id);
					albumsLiked.add(new AlbumBean(album_id,null,null,null,0,null,0,0,0,0));

				}
				rs.close();
				prest.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return albumsLiked;

	}

		/**
		 * used to fetch albumid which is been purchased by the user
		 * @param userID
		 */
		public static List<Object> fetchAlbumsShared(String userID) {

			String album_id = null;
			Connection conn = MySql.conn;
			List<Object> albumsShared = new ArrayList<Object>();
			Statement s1;
			String stm;
			ResultSet rs;
			java.sql.PreparedStatement prest = null;
			try {
				s1 = conn.createStatement();
				stm = "select distinct(album_id) from albums_shared where user_id =?";
				prest = conn.prepareStatement(stm);
				prest.setString(1,userID);
				rs = prest.executeQuery();
				while(rs.next())
				{
					album_id = rs.getString(1);
					System.out.println(" The album_id shared is : " + album_id);
					albumsShared.add(new AlbumBean(album_id,null,null,null,0,null,0,0,0,0));

				}
				rs.close();
				prest.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return albumsShared;
	}

		/**
		 * used to fetch albumid which is been purchased by the user
		 * @param userID
		 */
		public static List<Object> fetchAlbumsPurchased(String userID) {

			String album_id = null;
			Connection conn = MySql.conn;
			List<Object> albumsPurchased = new ArrayList<Object>();
			Statement s1;
			String stm;
			ResultSet rs;
			java.sql.PreparedStatement prest = null;
			try {
				s1 = conn.createStatement();
				stm = "select distinct(album_id) from albums_purchased where user_id =?";
				prest = conn.prepareStatement(stm);
				prest.setString(1,userID);
				rs = prest.executeQuery();
				while(rs.next())
				{
					album_id = rs.getString(1);
					System.out.println(" The album_id purchased is : " + album_id);
					albumsPurchased.add(new AlbumBean(album_id,null,null,null,0,null,0,0,0,0));

				}
				rs.close();
				prest.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return albumsPurchased;
	}

		/**
		 * Fetch the songid liked by the user
		 * @param userID
		 */

		public static List<Object> fetchSongsLiked(String userID) {

			String song_id = null;
			Connection conn = MySql.conn;
			List<Object> songsLiked = new ArrayList<Object>();
			Statement s1;
			String stm;
			ResultSet rs;
			java.sql.PreparedStatement prest = null;
			try {
				s1 = conn.createStatement();
				stm = "select distinct(song_id) from songs_liked where user_id =?";
				prest = conn.prepareStatement(stm);
				prest.setString(1,userID);
				rs = prest.executeQuery();
				while(rs.next())
				{
					song_id = rs.getString(1);
					System.out.println(" The song_id liked is : " + song_id);
					songsLiked.add(new SongBean(song_id,null,null,null,0,0,0,0,0,0,0,0,0));

				}
				rs.close();
				prest.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return songsLiked;
	}

		/**
		 * Fetch the songid shared by the user
		 * @param userID
		 */

		public static List<Object> fetchSongsShared(String userID) {

			String song_id = null;
			Connection conn = MySql.conn;
			List<Object> songsShared = new ArrayList<Object>();
			Statement s1;
			String stm;
			ResultSet rs;
			java.sql.PreparedStatement prest = null;
			try {
				s1 = conn.createStatement();
				stm = "select distinct(song_id) from songs_shared where user_id =?";
				prest = conn.prepareStatement(stm);
				prest.setString(1,userID);
				rs = prest.executeQuery();
				while(rs.next())
				{
					song_id = rs.getString(1);
					System.out.println(" The song_id shared is : " + song_id);
					songsShared.add(new SongBean(song_id,null,null,null,0,0,0,0,0,0,0,0,0));

				}
				rs.close();
				prest.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return songsShared;
	}


		/**
		 * Fetch the songid purchased by the user
		 * @param userID
		 */

		public static List<Object> fetchSongsPurchased(String userID) {

			String song_id = null;
			Connection conn = MySql.conn;
			List<Object> songsPurchased = new ArrayList<Object>();
			Statement s1;
			String stm;
			ResultSet rs;
			java.sql.PreparedStatement prest = null;
			try {
				s1 = conn.createStatement();
				stm = "select distinct(song_id) from songs_purchased where user_id =?";
				prest = conn.prepareStatement(stm);
				prest.setString(1,userID);
				rs = prest.executeQuery();
				while(rs.next())
				{
					song_id = rs.getString(1);
					System.out.println(" The song_id purchased is : " + song_id);
					songsPurchased.add(new SongBean(song_id,null,null,null,0,0,0,0,0,0,0,0,0));

				}
				rs.close();
				prest.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return songsPurchased;
	}



		/**
		 * Fetch the artistid liked by the user
		 * @param userID
		 */

		public static List<Object> fetchArtistsLiked(String userID) {

			String artist_id = null;
			Connection conn = MySql.conn;
			List<Object> artistsLiked = new ArrayList<Object>();
			Statement s1;
			String stm;
			ResultSet rs;
			java.sql.PreparedStatement prest = null;
			try {
				s1 = conn.createStatement();
				stm = "select distinct(artist_id) from artists_liked where user_id =?";
				prest = conn.prepareStatement(stm);
				prest.setString(1,userID);
				rs = prest.executeQuery();
				while(rs.next())
				{
					artist_id = rs.getString(1);
					System.out.println(" The artist_id liked is : " + artist_id);
					artistsLiked.add(new ArtistDataBean(artist_id,null,null,null,null,0,0));

				}
				rs.close();
				prest.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return artistsLiked;
	}

		/**
		 * Fetch the genreId and the count of the songs liked
		 * @param userID
		 */

		public static List<Object>  fetchSongsGenreLiked(String userID) {
			List<Object> songsLiked = new ArrayList<Object>();
			String genre_id = null;
			int song_liked_count =0;
			Connection conn = MySql.conn;
			Statement s1;
			String stm;
			ResultSet rs;
			java.sql.PreparedStatement prest = null;
			try {
				s1 = conn.createStatement();
				stm = "select count(song_id), song_genre_id from song_data where song_id in (select distinct(song_id) from songs_liked where user_id =? ) group by song_genre_id ";

				prest = conn.prepareStatement(stm);
				prest.setString(1,userID);
				rs = prest.executeQuery();
				System.out.println("in here");
				while(rs.next())
				{
					//song_liked_count = rs.getInt(1);
					song_liked_count = rs.getInt(1);
					genre_id = rs.getString(2);
					System.out.println(" The number of liked count is : " + song_liked_count + " for genere id :" + genre_id );
					songsLiked.add(new GenreBean(genre_id,0,0,0,song_liked_count,0,0,0));
				}
				rs.close();
				prest.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return songsLiked;

	}

		/**
		 * Fetch the genreId and the count of the songs shared
		 * @param userID
		 */

		public static List<Object>  fetchSongsGenreShared(String userID) {
			List<Object> songsShared = new ArrayList<Object>();
			String genre_id = null;
			int song_shared_count =0;
			Connection conn = MySql.conn;
			Statement s1;
			String stm;
			ResultSet rs;
			java.sql.PreparedStatement prest = null;
			try {
				s1 = conn.createStatement();
				stm = "select count(song_id), song_genre_id from song_data where song_id in (select distinct(song_id) from songs_shared where user_id =? ) group by song_genre_id ";

				prest = conn.prepareStatement(stm);
				prest.setString(1,userID);
				rs = prest.executeQuery();
				System.out.println("in here");
				while(rs.next())
				{
					//song_shared_count = rs.getInt(1);
					song_shared_count = rs.getInt(1);
					genre_id = rs.getString(2);
					System.out.println(" The number of shared count is : " + song_shared_count + " for genere id :" + genre_id );
					songsShared.add(new GenreBean(genre_id,0,0,0,0,0,song_shared_count,0));
				}
				rs.close();
				prest.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return songsShared;

	}

		/**
		 * Fetch the genreId and the count of the songs purchased
		 * @param userID
		 */

		public static List<Object>  fetchSongsGenrePurchased(String userID) {
			List<Object> songsPurchased = new ArrayList<Object>();
			String genre_id = null;
			int song_purchased_count =0;
			Connection conn = MySql.conn;
			Statement s1;
			String stm;
			ResultSet rs;
			java.sql.PreparedStatement prest = null;
			try {
				s1 = conn.createStatement();
				stm = "select count(song_id), song_genre_id from song_data where song_id in (select distinct(song_id) from songs_purchased where user_id =? ) group by song_genre_id ";

				prest = conn.prepareStatement(stm);
				prest.setString(1,userID);
				rs = prest.executeQuery();
				System.out.println("in here");
				while(rs.next())
				{
					//song_purchased_count = rs.getInt(1);
					song_purchased_count = rs.getInt(1);
					genre_id = rs.getString(2);
					System.out.println(" The number of purchased count is : " + song_purchased_count + " for genere id :" + genre_id );
					songsPurchased.add(new GenreBean(genre_id,0,0,0,0,song_purchased_count,0,0));
				}
				rs.close();
				prest.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return songsPurchased;

	}

		/**
		 * Fetch the genreId and the count of the album liked
		 * @param userID
		 */

		public static List<GenreBean>  fetchAlbumsGenreLiked(String userID) {
				//	System.out.println("in here");
				String genre_id = null;
				List<GenreBean> songgenreliked = new ArrayList<GenreBean>();

				int album_liked_count =0;
				Connection conn = MySql.conn;
				Statement s1;
				String stm;
				ResultSet rs;
				java.sql.PreparedStatement prest = null;
				try {
					s1 = conn.createStatement();
					stm = "select count(song_album_id), song_genre_id from song_data where song_album_id in (select distinct(album_id) from albums_liked where user_id =? ) group by song_genre_id ";

					prest = conn.prepareStatement(stm);
					prest.setString(1,userID);
					rs = prest.executeQuery();
					System.out.println("in here");
					while(rs.next())
					{
						//album_liked_count = rs.getInt(1);
						album_liked_count = rs.getInt(1);
						genre_id = rs.getString(2);
						GenreBean g = new GenreBean();
						g.setGenre_id(genre_id);
						g.setAlbum_liked_count(album_liked_count);
						songgenreliked.add(g);
						System.out.println(" The number of liked count is : " + album_liked_count + " for genere id :" + genre_id );

					}
					rs.close();
					prest.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return songgenreliked;

		}

		/**
		 * Fetch the genreId and the count of the album purchased
		 * @param userID
		 */

		public static List<Object>  fetchAlbumsGenrePurchased(String userID) {
					List<Object> albumsPurchased = new ArrayList<Object>();
					String genre_id = null;
					int album_purchased_count =0;
					Connection conn = MySql.conn;
					Statement s1;
					String stm;
					ResultSet rs;
					java.sql.PreparedStatement prest = null;
					try {
						s1 = conn.createStatement();
						stm = "select count(song_album_id), song_genre_id from song_data where song_album_id in (select distinct(album_id) from albums_purchased where user_id =? ) group by song_genre_id ";

						prest = conn.prepareStatement(stm);
						prest.setString(1,userID);
						rs = prest.executeQuery();
						System.out.println("in here");
						while(rs.next())
						{
							//album_purchased_count = rs.getInt(1);
							album_purchased_count = rs.getInt(1);
							genre_id = rs.getString(2);
							System.out.println(" The number of purchase count is : " + album_purchased_count + "for genere id :" + genre_id );
							albumsPurchased.add(new GenreBean(genre_id,0,album_purchased_count,0,0,0,0,0));
						}
						rs.close();
						prest.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return albumsPurchased;

			}

		/**
		 * Fetch the genreId and the count of the album shared
		 * @param userID
		 */

		public static List<Object>  fetchAlbumsGenreShared(String userID) {
					List<Object> albumsPurchased = new ArrayList<Object>();
					String genre_id = null;
					int album_shared_count =0;
					Connection conn = MySql.conn;
					Statement s1;
					String stm;
					ResultSet rs;
					java.sql.PreparedStatement prest = null;
					try {
						s1 = conn.createStatement();
						stm = "select count(song_album_id), song_genre_id from song_data where song_album_id in (select distinct(album_id) from albums_shared where user_id =? ) group by song_genre_id ";

						prest = conn.prepareStatement(stm);
						prest.setString(1,userID);
						rs = prest.executeQuery();
						System.out.println("in here");
						while(rs.next())
						{
							//album_shared_count = rs.getInt(1);
							album_shared_count = rs.getInt(1);
							genre_id = rs.getString(2);
							System.out.println(" The number of purchase count is : " + album_shared_count + "for genere id :" + genre_id );
							albumsPurchased.add( new GenreBean( genre_id,0,0,album_shared_count,0,0,0,0));
						}
						rs.close();
						prest.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return albumsPurchased;
				}


			}




