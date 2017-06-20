package com.water.roll.request;

public class SessionManager {
	
	private String mSid;
	
	private static SessionManager instance = new SessionManager();
	
	private static SessionManager getInstance(){
		return instance;
	}
	
	private SessionManager(){}
	
	public static void setSid(String sid){
		getInstance().mSid = sid;
	}
	
	public static String getSid(){
		return getInstance().mSid;
	}
}
