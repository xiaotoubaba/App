package com.jovision.xunwei.junior.lib.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateUtil {
	
	public static boolean isValidMobile(String mobile){
		if(TextUtils.isEmpty(mobile)){
			return false;
		}
		Pattern p = Pattern.compile("1\\d{10}");
		Matcher m = p.matcher(mobile);
		return m.matches();
	}
	
	public static void main(String[] args) {
		System.out.println(isValidMobile("1234"));
	}
}

