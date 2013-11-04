package MongoEntities;

public class ArtistDataBean {

	private String artistId ;
	private String artistName;
	private String artistDob;
	private String artistLocation;
	private String artistGenreId;
	private double likedCount;
	private double avgRating;

	public ArtistDataBean()
	{

	}

	public ArtistDataBean(String ArtistId, String ArtistName)
	{
		this.artistId = ArtistId;
		this.artistName = ArtistName;
	}

	public ArtistDataBean(String artistId, String artistName, String artistDob,
			String artistLocation, String artistGenreId, double likedCount,
			double avgRating)
	{
		this.artistId = artistId;
		this.artistName = artistName;
		this.artistDob = artistDob;
		this.artistLocation = artistLocation;
		this.artistGenreId = artistGenreId;
		this.likedCount = likedCount;
		this.avgRating = avgRating;
	}

	public String getArtistId() {
		return artistId;
	}

	public void setArtistId(String artistId) {
		this.artistId = artistId;
	}

	public String getArtistName() {
		return artistName;
	}

	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

	public String getArtistDob() {
		return artistDob;
	}

	public void setArtistDob(String artistDob) {
		this.artistDob = artistDob;
	}

	public String getArtistLocation() {
		return artistLocation;
	}

	public void setArtistLocation(String artistLocation) {
		this.artistLocation = artistLocation;
	}

	public String getArtistGenreId() {
		return artistGenreId;
	}

	public void setArtistGenreId(String artistGenreId) {
		this.artistGenreId = artistGenreId;
	}

	public double getLikedCount() {
		return likedCount;
	}

	public void setLikedCount(int likedCount) {
		this.likedCount = likedCount;
	}

	public double getAvgRating() {
		return avgRating;
	}

	public void setAvgRating(double avgRating) {
		this.avgRating = avgRating;
	}

}
