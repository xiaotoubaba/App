package com.water.roll.request.res;


public class LoginResult {
	private String session;
	private String user;
	private String userGuid;
	private String personName;
	private String photo;
	private String phone;
	private String email;
	private int    appId;
	private String project;

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getUserGuid() {
		return userGuid;
	}

	public void setUserGuid(String userGuid) {
		this.userGuid = userGuid;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}
}
