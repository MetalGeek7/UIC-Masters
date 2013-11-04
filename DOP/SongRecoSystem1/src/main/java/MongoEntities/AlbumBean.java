package MongoEntities;

public class AlbumBean {

	private String albumId ;
	private String albumName;
	private String albumArtist;
	private String releaseYear;
	private double albumCost;
	private String albumGenre;
	private double likedCount;
	private double sharedCount;
	private double purchaseCount;
	private double avgRating;

	public AlbumBean() {
		// TODO Auto-generated constructor stub
	}

	public AlbumBean(String albumId, String albumName, String albumArtist,
			String releaseYear, double albumCost, String albumGenre,
			double likedCount, double sharedCount, double purchaseCount,
			double avgRating)
	{
		this.albumId = albumId;
		this.albumName = albumName;
		this.albumArtist = albumArtist;
		this.releaseYear = releaseYear;
		this.albumCost = albumCost;
		this.albumGenre = albumGenre;
		this.likedCount = likedCount;
		this.sharedCount = sharedCount;
		this.purchaseCount = purchaseCount;
		this.avgRating = avgRating;
	}

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public String getAlbumArtist() {
		return albumArtist;
	}

	public void setAlbumArtist(String albumArtist) {
		this.albumArtist = albumArtist;
	}

	public String getReleaseYear() {
		return releaseYear;
	}

	public void setReleaseYear(String releaseYear) {
		this.releaseYear = releaseYear;
	}

	public double getAlbumCost() {
		return albumCost;
	}

	public void setAlbumCost(double albumCost) {
		this.albumCost = albumCost;
	}

	public String getAlbumGenre() {
		return albumGenre;
	}

	public void setAlbumGenre(String albumGenre) {
		this.albumGenre = albumGenre;
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

	public void setPurchaseCount(int purchaseCount) {
		this.purchaseCount = purchaseCount;
	}

	public double getAvgRating() {
		return avgRating;
	}

	public void setAvgRating(double avgRating) {
		this.avgRating = avgRating;
	}
}
