package MongoEntities;

public class ArtistBean{

	private String ArtistId ;
	private String ArtistName;
	private String ArtistDob;
	private String ArtistLocation;
	private String ArtistGenereId;

	private String artistId;
	private Double weight;

	public ArtistBean()
	{
		this(null,null,null,null,null,null,0.0);
	}

	 public ArtistBean(String artistId, String artistName, String artistDob,
			String artistLocation, String artistGenereId, String artistId2,
			Double weight) {
		ArtistId = artistId;
		ArtistName = artistName;
		ArtistDob = artistDob;
		ArtistLocation = artistLocation;
		ArtistGenereId = artistGenereId;
		artistId = artistId2;
		this.weight = weight;
	}

	public ArtistBean(String artistId, String artistName)
	{
			this.artistId=artistId;
			this.ArtistName=artistName;
	}

	public ArtistBean(String artistId, Double weight)
	{
		this.artistId=artistId;
		this.weight=weight;
	}

	public ArtistBean(String artistId, String artistName, String artistDob,
			String artistLocation, String artistGenereId)
	{
		ArtistId = artistId;
		ArtistName = artistName;
		ArtistDob = artistDob;
		ArtistLocation = artistLocation;
		ArtistGenereId = artistGenereId;
	}

	public String getArtistName() {
		return ArtistName;
	}
	public void setArtistName(String artistName) {
		ArtistName = artistName;
	}
	public String getArtistDob() {
		return ArtistDob;
	}
	public void setArtistDob(String artistDob) {
		ArtistDob = artistDob;
	}
	public String getArtistLocation() {
		return ArtistLocation;
	}
	public void setArtistLocation(String artistLocation) {
		ArtistLocation = artistLocation;
	}
	public String getArtistGenereId() {
		return ArtistGenereId;
	}
	public void setArtistGenereId(String artistGenereId) {
		ArtistGenereId = artistGenereId;
	}

	public String getArtistId() {
		return artistId;
	}
	public void setArtistId(String artistId) {
		this.artistId = artistId;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public String toString()
	{
		return artistId+" "+weight;

	}
	//@Override
	/*public int compareTo(Object b) {
		// TODO Auto-generated method stub
		if(this.getWeight()<((Artistbean)b).getWeight())
			return 1;
		else if(this.getWeight()>((Artistbean)b).getWeight())
			return -1;

		return 0;
	}*/


}
