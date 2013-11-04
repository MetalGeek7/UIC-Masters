package newEntities;

// Generated Oct 20, 2012 3:07:20 AM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * SongRatingId generated by hbm2java
 */
@Embeddable
public class SongRatingId implements java.io.Serializable {

	private String userId;
	private String songId;
	private Integer songRating;

	public SongRatingId() {
	}

	public SongRatingId(String userId, String songId, Integer songRating) {
		this.userId = userId;
		this.songId = songId;
		this.songRating = songRating;
	}

	@Column(name = "user_id", length = 15)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "song_id", length = 15)
	public String getSongId() {
		return this.songId;
	}

	public void setSongId(String songId) {
		this.songId = songId;
	}

	@Column(name = "song_rating")
	public Integer getSongRating() {
		return this.songRating;
	}

	public void setSongRating(Integer songRating) {
		this.songRating = songRating;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof SongRatingId))
			return false;
		SongRatingId castOther = (SongRatingId) other;

		return ((this.getUserId() == castOther.getUserId()) || (this
				.getUserId() != null && castOther.getUserId() != null && this
				.getUserId().equals(castOther.getUserId())))
				&& ((this.getSongId() == castOther.getSongId()) || (this
						.getSongId() != null && castOther.getSongId() != null && this
						.getSongId().equals(castOther.getSongId())))
				&& ((this.getSongRating() == castOther.getSongRating()) || (this
						.getSongRating() != null
						&& castOther.getSongRating() != null && this
						.getSongRating().equals(castOther.getSongRating())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getUserId() == null ? 0 : this.getUserId().hashCode());
		result = 37 * result
				+ (getSongId() == null ? 0 : this.getSongId().hashCode());
		result = 37
				* result
				+ (getSongRating() == null ? 0 : this.getSongRating()
						.hashCode());
		return result;
	}

}
