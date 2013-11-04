package Beans;

public class User {

	private int userId;
	private String userName;
	private String loginName;
	private int userDob;
	private String userLocation;
	private String userPreferredGenre;
	private String userPreferredArtist;
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
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
	public String getUserLocation() {
		return userLocation;
	}
	public void setUserLocation(String userLocation) {
		this.userLocation = userLocation;
	}
	public int getUserDob() {
		return userDob;
	}
	public void setUserDob(int userDob) {
		this.userDob = userDob;
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
}