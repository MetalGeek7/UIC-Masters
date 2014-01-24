package newEntities;

// Generated Oct 20, 2012 3:07:20 AM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ArtistRatingId generated by hbm2java
 */
@Embeddable
public class ArtistRatingId implements java.io.Serializable {

	private String userId;
	private String artistId;
	private Integer artistRating;

	public ArtistRatingId() {
	}

	public ArtistRatingId(String userId, String artistId, Integer artistRating) {
		this.userId = userId;
		this.artistId = artistId;
		this.artistRating = artistRating;
	}

	@Column(name = "user_id", length = 15)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "artist_id", length = 15)
	public String getArtistId() {
		return this.artistId;
	}

	public void setArtistId(String artistId) {
		this.artistId = artistId;
	}

	@Column(name = "artist_rating")
	public Integer getArtistRating() {
		return this.artistRating;
	}

	public void setArtistRating(Integer artistRating) {
		this.artistRating = artistRating;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ArtistRatingId))
			return false;
		ArtistRatingId castOther = (ArtistRatingId) other;

		return ((this.getUserId() == castOther.getUserId()) || (this
				.getUserId() != null && castOther.getUserId() != null && this
				.getUserId().equals(castOther.getUserId())))
				&& ((this.getArtistId() == castOther.getArtistId()) || (this
						.getArtistId() != null
						&& castOther.getArtistId() != null && this
						.getArtistId().equals(castOther.getArtistId())))
				&& ((this.getArtistRating() == castOther.getArtistRating()) || (this
						.getArtistRating() != null
						&& castOther.getArtistRating() != null && this
						.getArtistRating().equals(castOther.getArtistRating())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getUserId() == null ? 0 : this.getUserId().hashCode());
		result = 37 * result
				+ (getArtistId() == null ? 0 : this.getArtistId().hashCode());
		result = 37
				* result
				+ (getArtistRating() == null ? 0 : this.getArtistRating()
						.hashCode());
		return result;
	}

}