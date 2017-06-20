package com.jovision.xunwei.junior.lib.util;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	private StringUtil(){}
	
	public static boolean isEmpty(String src){
		if(src == null || src.length() <= 0){
			return true;
		}
		return false;
	}
	
	public static boolean isEmpty(Collection<?> c){
		if(c == null || c.size() <= 0){
			return true;
		}
		return false;
	}

	public static boolean isEmail(String email) {
		boolean flag = false;
		try {
			String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	public static boolean isMobile(String mobileNumber) {
		boolean flag = false;
		try {
			Pattern regex = Pattern.compile("^1\\d{10}$");
			Matcher matcher = regex.matcher(mobileNumber);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	public static boolean isValidTime(String time) {
		boolean flag = false;
		if(!time.equals("00:00"))
			flag = true;
		return flag;
	}

}
