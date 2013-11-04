package MongoEntities;

public class GenreBean {

	String genre_id;
	int album_liked_count;
	int album_purchased_count;
	int album_shared_count;
	int song_liked_count;
	int song_purchased_count;
	int song_shared_count;
	int artist_liked_count;

	public GenreBean()
	{

	}



	public GenreBean(String genre_id, int album_liked_count,
			int album_purchased_count, int album_shared_count,
			int song_liked_count, int song_purchased_count,
			int song_shared_count, int artist_liked_count) {
		super();
		this.genre_id = genre_id;
		this.album_liked_count = album_liked_count;
		this.album_purchased_count = album_purchased_count;
		this.album_shared_count = album_shared_count;
		this.song_liked_count = song_liked_count;
		this.song_purchased_count = song_purchased_count;
		this.song_shared_count = song_shared_count;
		this.artist_liked_count = artist_liked_count;
	}



	public String getGenre_id() {
		return genre_id;
	}
	public void setGenre_id(String genre_id) {
		this.genre_id = genre_id;
	}
	public int getAlbum_liked_count() {
		return album_liked_count;
	}
	public void setAlbum_liked_count(int album_liked_count) {
		this.album_liked_count = album_liked_count;
	}
	public int getAlbum_purchased_count() {
		return album_purchased_count;
	}
	public void setAlbum_purchased_count(int album_purchased_count) {
		this.album_purchased_count = album_purchased_count;
	}
	public int getAlbum_shared_count() {
		return album_shared_count;
	}
	public void setAlbum_shared_count(int album_shared_count) {
		this.album_shared_count = album_shared_count;
	}
	public int getSong_liked_count() {
		return song_liked_count;
	}
	public void setSong_liked_count(int song_liked_count) {
		this.song_liked_count = song_liked_count;
	}
	public int getSong_purchased_count() {
		return song_purchased_count;
	}
	public void setSong_purchased_count(int song_purchased_count) {
		this.song_purchased_count = song_purchased_count;
	}
	public int getSong_shared_count() {
		return song_shared_count;
	}
	public void setSong_shared_count(int song_shared_count) {
		this.song_shared_count = song_shared_count;
	}
	public int getArtist_liked_count() {
		return artist_liked_count;
	}
	public void setArtist_liked_count(int artist_liked_count) {
		this.artist_liked_count = artist_liked_count;
	}

}
