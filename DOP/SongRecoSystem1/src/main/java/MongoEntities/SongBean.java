package MongoEntities;

public class SongBean {
	private String songID,albumID,artistID,genreID;
	private int releaseYear;
	private double likedCount,sharedCount,purchaseCount;
	private double averageRating,songHotness,songEnergy,songDanceability,songTempo;

	public SongBean()
	{

	}

	public String getGenreID() {
		return genreID;
	}
	public void setGenreID(String genreID) {
		this.genreID = genreID;
	}

	public String getSongID() {
		return songID;
	}
	public void setSongID(String songID) {
		this.songID = songID;
	}
	public double getLikedCount() {
		return likedCount;
	}
	public void setLikedCount(int likedCount) {
		this.likedCount = likedCount;
	}
	public double getSharedCount() {
		return sharedCount;
	}
	public void setSharedCount(int sharedCount) {
		this.sharedCount = sharedCount;
	}
	public double getPurchaseCount() {
		return purchaseCount;
	}
	public SongBean(String songID, int likedCount, int sharedCount,
			int purchaseCount, double averageRating, double songHotness,
			double songEnergy, double songDanceability, double songTempo) {
		super();
		this.songID = songID;
		this.likedCount = likedCount;
		this.sharedCount = sharedCount;
		this.purchaseCount = purchaseCount;
		this.averageRating = averageRating;
		this.songHotness = songHotness;
		this.songEnergy = songEnergy;
		this.songDanceability = songDanceability;
		this.songTempo = songTempo;
	}
	public void setPurchaseCount(int purchaseCount) {
		this.purchaseCount = purchaseCount;
	}
	public double getAverageRating() {
		return averageRating;
	}
	public void setAverageRating(double averageRating) {
		this.averageRating = averageRating;
	}
	public double getSongHotness() {
		return songHotness;
	}
	public void setSongHotness(double songHotness) {
		this.songHotness = songHotness;
	}
	public double getSongEnergy() {
		return songEnergy;
	}
	public void setSongEnergy(double songEnergy) {
		this.songEnergy = songEnergy;
	}
	public double getSongDanceability() {
		return songDanceability;
	}
	public void setSongDanceability(double songDanceability) {
		this.songDanceability = songDanceability;
	}
	public double getSongTempo() {
		return songTempo;
	}
	public void setSongTempo(double songTempo) {
		this.songTempo = songTempo;
	}

	public SongBean(String songID, String albumID, String artistID,String genreID,
			double likedCount, double sharedCount, double purchaseCount,
			int releaseYear, double averageRating, double songHotness,
			double songEnergy, double songDanceability, double songTempo) {
		super();
		this.songID = songID;
		this.albumID = albumID;
		this.artistID = artistID;
		this.genreID=genreID;
		this.likedCount = likedCount;
		this.sharedCount = sharedCount;
		this.purchaseCount = purchaseCount;
		this.releaseYear = releaseYear;
		this.averageRating = averageRating;
		this.songHotness = songHotness;
		this.songEnergy = songEnergy;
		this.songDanceability = songDanceability;
		this.songTempo = songTempo;
	}
	public String getAlbumID() {
		return albumID;
	}
	public void setAlbumID(String albumID) {
		this.albumID = albumID;
	}
	public String getArtistID() {
		return artistID;
	}
	public void setArtistID(String artistID) {
		this.artistID = artistID;
	}
	public int getReleaseYear() {
		return releaseYear;
	}
	public void setReleaseYear(int releaseYear) {
		this.releaseYear = releaseYear;
	}

}
