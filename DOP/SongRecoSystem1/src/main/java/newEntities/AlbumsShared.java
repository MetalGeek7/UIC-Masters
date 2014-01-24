package newEntities;

// Generated Oct 20, 2012 3:07:20 AM by Hibernate Tools 3.4.0.CR1

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * AlbumsShared generated by hbm2java
 */
@Entity
@Table(name = "albums_shared", catalog = "songrecosystem")
public class AlbumsShared implements java.io.Serializable {

	private AlbumsSharedId id;
	private UserData userData;
	private SongAlbums songAlbums;

	public AlbumsShared() {
	}

	public AlbumsShared(AlbumsSharedId id) {
		this.id = id;
	}

	public AlbumsShared(AlbumsSharedId id, UserData userData,
			SongAlbums songAlbums) {
		this.id = id;
		this.userData = userData;
		this.songAlbums = songAlbums;
	}

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "userId", column = @Column(name = "user_id", length = 15)),
			@AttributeOverride(name = "albumId", column = @Column(name = "album_id", length = 15)) })
	public AlbumsSharedId getId() {
		return this.id;
	}

	public void setId(AlbumsSharedId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	public UserData getUserData() {
		return this.userData;
	}

	public void setUserData(UserData userData) {
		this.userData = userData;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "album_id", insertable = false, updatable = false)
	public SongAlbums getSongAlbums() {
		return this.songAlbums;
	}

	public void setSongAlbums(SongAlbums songAlbums) {
		this.songAlbums = songAlbums;
	}

}