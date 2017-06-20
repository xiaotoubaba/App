package com.water.roll.request;


public class APIConfig {

	private String api;
	private boolean needLogin;
	
	public String getApi() {
		return api;
	}
	public void setApi(String api) {
		this.api = api;
	}
	public boolean isNeedLogin() {
		return needLogin;
	}
	public void setNeedLogin(boolean needLogin) {
		this.needLogin = needLogin;
	}
	
}
