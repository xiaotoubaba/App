package in.srain.cube.request;

import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import java.util.Date;
import java.util.List;
import java.util.Map;

import in.srain.cube.util.DateUtil;


public class CubeCookieManager {
	
	private static String JSESSIONID_KEY = "JSESSIONID";
	private static String JSESSIONID_VALUE = "";
			
	private CubeCookieManager(){}
	
	/**
	 * 应用启动的时候调用，参考：{@link CubeCookieManager#getInstance CookieManager.getInstance()}
	 * */
	public static void init(Context context){
		CookieSyncManager.createInstance(context);
	}
	
	/**
	 * 获取cookie
	 * */
	public static String getCookie(String url){
		CookieManager cookieManager = CookieManager.getInstance();
		return cookieManager.getCookie(url);
	}
	
	/**
	 * http://stackoverflow.com/questions/16007084/does-android-webkit-cookiemanager-works-on-android-2-3-6
	 * */
	public static void setCookies(String url, Map<String, List<String>> headerFields) {
		if (null == headerFields) {
			return;
		}
		List<String> cookies = headerFields.get("Set-Cookie");
		if (null == cookies) {
			return;
		}
		CookieSyncManager.getInstance().startSync();
		for (String cookie : cookies) {
			setCookie(url, cookie);
		}
		CookieSyncManager.getInstance().sync();
	}
	
	private static void setCookie(String url, String cookie) {
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);
		if(cookie.indexOf(JSESSIONID_KEY) >= 0){
			JSESSIONID_VALUE = getSessionId(cookie);
		}
		if(cookie.indexOf("Expires") < 0){
			cookie = addExpireToCookie(cookie);
		}
		cookieManager.setCookie(url, cookie);
	}
	
	/**
	 * http://stackoverflow.com/questions/8547620/what-is-a-session-cookie
	 * */
	private static String addExpireToCookie(String cookie) {
		Date expireDate = new Date(new Date().getTime() + 20L*60*1000);
		String datestr =DateUtil.format(DateUtil.east8ToGmt(expireDate), DateUtil.FORMAT_GMT);
		String arr[] = cookie.split(";");
		StringBuilder sb = new StringBuilder();
		sb.append(arr[0]);
		sb.append("; ").append("Expires=").append(datestr);
		if(arr.length > 1){
			for(int i=1; i<arr.length; i++){
				sb.append(";").append(arr[i]);
			}
		}
		return sb.toString();
	}
	
	private static String getSessionId(String rawCookie){
		String[] rawCookieParams = rawCookie.split(";");
	    String[] rawCookieNameAndValue = rawCookieParams[0].split("=");
	    return rawCookieNameAndValue[1].trim();
	}
	
}
