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
 * UserPlaycount generated by hbm2java
 */
@Entity
@Table(name = "user_playcount", catalog = "songrecosystem")
public class UserPlaycount implements java.io.Serializable {

	private UserPlaycountId id;
	private SongData songData;
	private UserData userData;

	public UserPlaycount() {
	}

	public UserPlaycount(UserPlaycountId id) {
		this.id = id;
	}

	public UserPlaycount(UserPlaycountId id, SongData songData,
			UserData userData) {
		this.id = id;
		this.songData = songData;
		this.userData = userData;
	}

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "userId", column = @Column(name = "user_id", length = 15)),
			@AttributeOverride(name = "songId", column = @Column(name = "song_id", length = 15)),
			@AttributeOverride(name = "songPlayCount", column = @Column(name = "song_playCount")) })
	public UserPlaycountId getId() {
		return this.id;
	}

	public void setId(UserPlaycountId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "song_id", insertable = false, updatable = false)
	public SongData getSongData() {
		return this.songData;
	}

	public void setSongData(SongData songData) {
		this.songData = songData;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	public UserData getUserData() {
		return this.userData;
	}

	public void setUserData(UserData userData) {
		this.userData = userData;
	}

}