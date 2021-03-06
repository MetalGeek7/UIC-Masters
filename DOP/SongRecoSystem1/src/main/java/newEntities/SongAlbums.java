package newEntities;

// Generated Oct 20, 2012 3:07:20 AM by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * SongAlbums generated by hbm2java
 */
@Entity
@Table(name = "song_albums", catalog = "songrecosystem")
public class SongAlbums implements java.io.Serializable {

	private String albumId;
	private SongArtists songArtists;
	private Genre genre;
	private String albumName;
	private String albumReleaseYear;
	private BigDecimal albumCost;
	private Set<AlbumsShared> albumsShareds = new HashSet<AlbumsShared>(0);
	private Set<AlbumsPurchased> albumsPurchaseds = new HashSet<AlbumsPurchased>(
			0);
	private Set<SongData> songDatas = new HashSet<SongData>(0);
	private Set<AlbumsLiked> albumsLikeds = new HashSet<AlbumsLiked>(0);
	private Set<AlbumRating> albumRatings = new HashSet<AlbumRating>(0);

	public SongAlbums() {
	}

	public SongAlbums(String albumId) {
		this.albumId = albumId;
	}

	public SongAlbums(String albumId, SongArtists songArtists, Genre genre,
			String albumName, String albumReleaseYear, BigDecimal albumCost,
			Set<AlbumsShared> albumsShareds,
			Set<AlbumsPurchased> albumsPurchaseds, Set<SongData> songDatas,
			Set<AlbumsLiked> albumsLikeds, Set<AlbumRating> albumRatings) {
		this.albumId = albumId;
		this.songArtists = songArtists;
		this.genre = genre;
		this.albumName = albumName;
		this.albumReleaseYear = albumReleaseYear;
		this.albumCost = albumCost;
		this.albumsShareds = albumsShareds;
		this.albumsPurchaseds = albumsPurchaseds;
		this.songDatas = songDatas;
		this.albumsLikeds = albumsLikeds;
		this.albumRatings = albumRatings;
	}

	@Id
	@Column(name = "album_id", unique = true, nullable = false, length = 15)
	public String getAlbumId() {
		return this.albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "album_artist_id")
	public SongArtists getSongArtists() {
		return this.songArtists;
	}

	public void setSongArtists(SongArtists songArtists) {
		this.songArtists = songArtists;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "album_genre_id")
	public Genre getGenre() {
		return this.genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}

	@Column(name = "album_name", length = 20)
	public String getAlbumName() {
		return this.albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	@Column(name = "album_release_year", length = 4)
	public String getAlbumReleaseYear() {
		return this.albumReleaseYear;
	}

	public void setAlbumReleaseYear(String albumReleaseYear) {
		this.albumReleaseYear = albumReleaseYear;
	}

	@Column(name = "album_cost", precision = 5)
	public BigDecimal getAlbumCost() {
		return this.albumCost;
	}

	public void setAlbumCost(BigDecimal albumCost) {
		this.albumCost = albumCost;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "songAlbums")
	public Set<AlbumsShared> getAlbumsShareds() {
		return this.albumsShareds;
	}

	public void setAlbumsShareds(Set<AlbumsShared> albumsShareds) {
		this.albumsShareds = albumsShareds;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "songAlbums")
	public Set<AlbumsPurchased> getAlbumsPurchaseds() {
		return this.albumsPurchaseds;
	}

	public void setAlbumsPurchaseds(Set<AlbumsPurchased> albumsPurchaseds) {
		this.albumsPurchaseds = albumsPurchaseds;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "songAlbums")
	public Set<SongData> getSongDatas() {
		return this.songDatas;
	}

	public void setSongDatas(Set<SongData> songDatas) {
		this.songDatas = songDatas;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "songAlbums")
	public Set<AlbumsLiked> getAlbumsLikeds() {
		return this.albumsLikeds;
	}

	public void setAlbumsLikeds(Set<AlbumsLiked> albumsLikeds) {
		this.albumsLikeds = albumsLikeds;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "songAlbums")
	public Set<AlbumRating> getAlbumRatings() {
		return this.albumRatings;
	}

	public void setAlbumRatings(Set<AlbumRating> albumRatings) {
		this.albumRatings = albumRatings;
	}

}
