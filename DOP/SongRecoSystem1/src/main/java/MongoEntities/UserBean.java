package MongoEntities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import newEntities.AlbumRating;
import newEntities.AlbumsLiked;
import newEntities.AlbumsPurchased;
import newEntities.AlbumsShared;
import newEntities.ArtistRating;
import newEntities.ArtistsLiked;
import newEntities.SongRating;
import newEntities.SongsLiked;
import newEntities.SongsPurchased;
import newEntities.SongsShared;
import newEntities.UserPlaycount;
import newEntities.UserSecurity;

public class UserBean {

	private String userId;
	private String userName;
	private String loginName;
	private String userDob;
	private String userLocation;
	private String userPreferredGenre;
	private String userPreferredArtist;
	private Set<SongsShared> songsShareds = new HashSet<SongsShared>(0);
	private Set<UserPlaycount> userPlaycounts = new HashSet<UserPlaycount>(0);
	private Set<SongsLiked> songsLikeds = new HashSet<SongsLiked>(0);
	private Set<ArtistsLiked> artistsLikeds = new HashSet<ArtistsLiked>(0);
	private Set<UserSecurity> userSecurities = new HashSet<UserSecurity>(0);
	private Set<ArtistRating> artistRatings = new HashSet<ArtistRating>(0);
	private Set<AlbumsLiked> albumsLikeds = new HashSet<AlbumsLiked>(0);
	private Set<SongRating> songRatings = new HashSet<SongRating>(0);
	private Set<AlbumsPurchased> albumsPurchaseds = new HashSet<AlbumsPurchased>(
			0);
	private Set<AlbumsShared> albumsShareds = new HashSet<AlbumsShared>(0);
	private Set<AlbumRating> albumRatings = new HashSet<AlbumRating>(0);
	private Set<SongsPurchased> songsPurchaseds = new HashSet<SongsPurchased>(0);

	private List<GenreBean> songgenreliked = new ArrayList<GenreBean>();

	public List<GenreBean> getSonggenreliked() {
		return songgenreliked;
	}

	public void setSonggenreliked(List<GenreBean> songgenreliked) {
		this.songgenreliked = songgenreliked;
	}

	public UserBean() {

	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getUserDob() {
		return userDob;
	}
	public void setUserDob(String userDob) {
		this.userDob = userDob;
	}
	public String getUserLocation() {
		return userLocation;
	}
	public void setUserLocation(String userLocation) {
		this.userLocation = userLocation;
	}
	public String getUserPreferredGenre() {
		return userPreferredGenre;
	}
	public void setUserPreferredGenre(String userPreferredGenre) {
		this.userPreferredGenre = userPreferredGenre;
	}
	public String getUserPreferredArtist() {
		return userPreferredArtist;
	}
	public void setUserPreferredArtist(String userPreferredArtist) {
		this.userPreferredArtist = userPreferredArtist;
	}
	public Set<SongsShared> getSongsShareds() {
		return songsShareds;
	}
	public void setSongsShareds(Set<SongsShared> songsShareds) {
		this.songsShareds = songsShareds;
	}
	public Set<UserPlaycount> getUserPlaycounts() {
		return userPlaycounts;
	}
	public void setUserPlaycounts(Set<UserPlaycount> userPlaycounts) {
		this.userPlaycounts = userPlaycounts;
	}
	public Set<SongsLiked> getSongsLikeds() {
		return songsLikeds;
	}
	public void setSongsLikeds(Set<SongsLiked> songsLikeds) {
		this.songsLikeds = songsLikeds;
	}
	public Set<ArtistsLiked> getArtistsLikeds() {
		return artistsLikeds;
	}
	public void setArtistsLikeds(Set<ArtistsLiked> artistsLikeds) {
		this.artistsLikeds = artistsLikeds;
	}
	public Set<UserSecurity> getUserSecurities() {
		return userSecurities;
	}
	public void setUserSecurities(Set<UserSecurity> userSecurities) {
		this.userSecurities = userSecurities;
	}
	public Set<ArtistRating> getArtistRatings() {
		return artistRatings;
	}
	public void setArtistRatings(Set<ArtistRating> artistRatings) {
		this.artistRatings = artistRatings;
	}
	public Set<AlbumsLiked> getAlbumsLikeds() {
		return albumsLikeds;
	}
	public void setAlbumsLikeds(Set<AlbumsLiked> albumsLikeds) {
		this.albumsLikeds = albumsLikeds;
	}
	public Set<SongRating> getSongRatings() {
		return songRatings;
	}
	public void setSongRatings(Set<SongRating> songRatings) {
		this.songRatings = songRatings;
	}
	public Set<AlbumsPurchased> getAlbumsPurchaseds() {
		return albumsPurchaseds;
	}
	public void setAlbumsPurchaseds(Set<AlbumsPurchased> albumsPurchaseds) {
		this.albumsPurchaseds = albumsPurchaseds;
	}
	public Set<AlbumsShared> getAlbumsShareds() {
		return albumsShareds;
	}
	public void setAlbumsShareds(Set<AlbumsShared> albumsShareds) {
		this.albumsShareds = albumsShareds;
	}
	public Set<AlbumRating> getAlbumRatings() {
		return albumRatings;
	}
	public void setAlbumRatings(Set<AlbumRating> albumRatings) {
		this.albumRatings = albumRatings;
	}
	public Set<SongsPurchased> getSongsPurchaseds() {
		return songsPurchaseds;
	}
	public void setSongsPurchaseds(Set<SongsPurchased> songsPurchaseds) {
		this.songsPurchaseds = songsPurchaseds;
	}


}
