package com.jovision.xunwei.junior.lib.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class HttpUtil {
	
	public static final int TIME_OUT = 10000;
	
	public static ThreadLocal<String> cookieHolder = new ThreadLocal<String>(){

		private HashMap<String, String> cookies = new HashMap<String, String>();
		
		@Override
		protected String initialValue() {
			 cookies = new HashMap<String, String>();
			 return super.initialValue();
		}

		@Override
		public String get() {
			StringBuilder sb = new StringBuilder();
			for(Map.Entry<String, String> entry : cookies.entrySet()){
				String key = entry.getKey();
				String value = entry.getValue();
				sb.append(key).append("=").append(value).append("; ");
			}
			if(sb.length() > 0 ){
				sb.deleteCharAt(sb.length()-1);
				sb.deleteCharAt(sb.length()-1);
			}
			return sb.toString();
		}

		@Override
		public void set(String value) {
			if(value == null || value.length() <= 0){
				return;
			}
			String arr[] = value.split("=", 2);
			if(arr.length != 2){
				return;
			}
			cookies.put(arr[0], arr[1]);
		}
	};
	
	public static class Response<T>{
		private int errcode;
		private String errmsg;
		private T content;
		public int getErrcode() {
			return errcode;
		}
		public void setErrcode(int errcode) {
			this.errcode = errcode;
		}
		public T getContent() {
			return content;
		}
		public void setContent(T content) {
			this.content = content;
		}
		public String getErrmsg() {
			return errmsg;
		}
		public void setErrmsg(String errmsg) {
			this.errmsg = errmsg;
		}
		@Override
		public String toString() {
			return "Response [errcode=" + errcode + ", errmsg=" + errmsg
					+ ", content=" + content + "]";
		}
	}
	
	public static <T> Response<T> get(String urlstr, Class<T> clazz){
		return get(urlstr,"application/x-www-form-urlencoded", clazz);
	}
	
	public static <T> Response<T> getJson(String urlstr, Class<T> clazz){
		return get(urlstr,"application/json", clazz);
	}
	
	public static <T> Response<T> get(String urlstr, String contentType, Class<T> clazz) {
		InputStream inputStream = null;
		Response<T> response = new Response<T>();
		try{
			URL localURL = new URL(urlstr);
			HttpURLConnection conn = (HttpURLConnection) localURL.openConnection();
			conn.setConnectTimeout(TIME_OUT);
			conn.setReadTimeout(TIME_OUT);
			conn.setRequestProperty("Accept-Charset", "utf-8");
			conn.setRequestProperty("Content-Type", contentType);
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:40.0) Gecko/20100101 Firefox/40.0");
			appendCookieHeader(conn);
			
			response.setErrcode(conn.getResponseCode());
			
			parseCookie(conn.getHeaderFields());

			if (conn.getResponseCode() == 200) {
				inputStream = conn.getInputStream();
				byte[] res = readInputStream(inputStream);
				T t = convert(res, clazz);
				response.setContent(t);
			}
		}catch(Exception e){
			e.printStackTrace();
			response.setErrcode(500);
			response.setErrmsg(getErrorStackTrace(e));
		}finally{
			if(inputStream != null){
				IOUtil.closeQuietly(inputStream);
			}
		}
		return response;
	}
	
	public static <T> Response<T> post(String urlstr, String parameterData,Class<T> clazz) {
		return post(urlstr, parameterData, "application/x-www-form-urlencoded;charset=UTF-8", clazz);
    }
	
	public static <T> Response<T> postJson(String urlstr, String parameterData,Class<T> clazz){
		return post(urlstr, parameterData, "application/json;charset=UTF-8", clazz);
    }
	
	public static <T> Response<T> post(String urlstr, String parameterData, String contentType, Class<T> clazz) {
		InputStream in = null;
        OutputStream out = null;
        OutputStreamWriter writer = null;
        Response<T> response = new Response<T>();
		try{
			URL localURL = new URL(urlstr);
	        HttpURLConnection conn = (HttpURLConnection)localURL.openConnection();
	        conn.setConnectTimeout(TIME_OUT);
			conn.setReadTimeout(TIME_OUT);
	        conn.setDoOutput(true);
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Accept-Charset", "UTF-8");
	        conn.setRequestProperty("Content-Type", contentType);
	        conn.setRequestProperty("Content-Length", String.valueOf(parameterData.length()));
	        appendCookieHeader(conn);

            out = conn.getOutputStream();
            writer = new OutputStreamWriter(out, "UTF-8");
            
            writer.write(parameterData.toString());
            writer.flush();
            
            response.setErrcode(conn.getResponseCode());
            
            parseCookie(conn.getHeaderFields());
            
            if (conn.getResponseCode() == 200) {
            	in = conn.getInputStream();
                byte[] res = readInputStream(in);
                LogUtil.d("res:"+toString(res));
        		T t = convert(res, clazz);
        		response.setContent(t);
            }
        }catch(Exception e){
        	e.printStackTrace();
        	response.setErrcode(500);
			response.setErrmsg(getErrorStackTrace(e));
        } finally {
        	IOUtil.closeQuietly(writer);
        	IOUtil.closeQuietly(out);
        	IOUtil.closeQuietly(in);
        }
		return response;
	}
	
	public static Response<String> get302Location(String urlstr){
		Response<String> response = new Response<String>();
		try{
			URL localURL = new URL(urlstr);
			HttpURLConnection conn = (HttpURLConnection) localURL.openConnection();
			conn.setConnectTimeout(TIME_OUT);
			conn.setReadTimeout(TIME_OUT);
			conn.setRequestProperty("Accept-Charset", "utf-8");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setInstanceFollowRedirects(false);
			appendCookieHeader(conn);
			parseCookie(conn.getHeaderFields());
			if(conn.getResponseCode() == 302){
				String location = conn.getHeaderField("location");
				response.setErrcode(302);
				response.setContent(location);
			}else{
				response.setErrcode(conn.getResponseCode());
			}
		}catch(Exception e){
			e.printStackTrace();
			response.setErrcode(500);
			response.setErrmsg(getErrorStackTrace(e));
		}
		return response;
	}
	
	
	public static Response<String> post302Location(String urlstr, String parameterData){
		InputStream in = null;
        OutputStream out = null;
        OutputStreamWriter writer = null;
        Response<String> response = new Response<String>();
		try{
			URL localURL = new URL(urlstr);
	        HttpURLConnection conn = (HttpURLConnection)localURL.openConnection();
	        conn.setConnectTimeout(TIME_OUT);
			conn.setReadTimeout(TIME_OUT);
	        conn.setDoOutput(true);
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Accept-Charset", "utf-8");
	        conn.setRequestProperty("Content-Length", String.valueOf(parameterData.length()));
	        conn.setInstanceFollowRedirects(false);
	        appendCookieHeader(conn);
       
            out = conn.getOutputStream();
            writer = new OutputStreamWriter(out, "UTF-8");
            
            writer.write(parameterData.toString());
            writer.flush();
            
            parseCookie(conn.getHeaderFields());
            
            if (conn.getResponseCode() == 302) {
            	String location = conn.getHeaderField("location");
				response.setErrcode(302);
				response.setContent(location);
            }else{
            	response.setErrcode(conn.getResponseCode());
            }
        } catch(Exception e){
        	e.printStackTrace();
			response.setErrcode(500);
			response.setErrmsg(getErrorStackTrace(e));
        } finally {
        	IOUtil.closeQuietly(writer);
        	IOUtil.closeQuietly(out);
        	IOUtil.closeQuietly(in);
        }
		return response;
	}
	
	private static void parseCookie(Map<String,List<String>> headers){
		for(Map.Entry<String,List<String>> entry : headers.entrySet()){
			String key = entry.getKey();
			List<String> values = entry.getValue();
			if("Set-Cookie".equals(key)){
				parseCookieValues(values);
			}
		}
	}
	
	private static void parseCookieValues(List<String> values){
		for(String str : values){
			String kvs[] = str.split(";");
			if(kvs == null || kvs.length <= 0){
				continue;
			}
			cookieHolder.set(kvs[0]);
			System.out.println("parse cookie:"+kvs[0]);
		}
	}


	private static void appendCookieHeader(HttpURLConnection conn){
		String cookie = cookieHolder.get();
        if(cookie != null && cookie.length() > 0){
        	conn.setRequestProperty("Cookie", cookie);
        	System.out.println("upload cookie:"+cookie);
        }
	}
	
	public static <T> T upload(String filePath, String url, Class<T> clazz) throws IOException {
		File file = new File(filePath);
		if (!file.exists() || !file.isFile()) {
			throw new RuntimeException();
		}
		
		URL urlObj = new URL(url);

		HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
		conn.setConnectTimeout(TIME_OUT);
		conn.setReadTimeout(TIME_OUT);
		conn.setRequestMethod("POST"); 
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false); 

		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setRequestProperty("Charset", "UTF-8");

		String BOUNDARY = "----------" + System.currentTimeMillis();
		conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

		StringBuilder sb = new StringBuilder();
		sb.append("--");
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition: form-data;name=\"media\";filename=\"" + file.getName() + "\"\r\n");
		sb.append("Content-Type:application/octet-stream\r\n\r\n");

		byte[] head = sb.toString().getBytes("utf-8");
		OutputStream out = null;
		InputStream fin = null;
		InputStream in = null;
		try{

			out = conn.getOutputStream();

			out.write(head);

			fin = new FileInputStream(file);
			int bytes = 0;
			byte[] bufferOut = new byte[1024];
			while ((bytes = fin.read(bufferOut)) != -1) {
				out.write(bufferOut, 0, bytes);
			}
			
			byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");//瀹氫箟锟�锟斤拷鏁版嵁鍒嗛殧锟�
			out.write(foot);

			out.flush();

			in = conn.getInputStream();
	        byte[] res = readInputStream(in);
			return convert(res, clazz);
	
		}finally {
        	IOUtil.closeQuietly(out);
        	IOUtil.closeQuietly(fin);
        	IOUtil.closeQuietly(in);
        }
	}
	

	@SuppressWarnings("unchecked")
	private static <T> T convert(byte[] res, Class<T> clazz) {
		if(clazz == null){
			return null;
		}
		if(clazz == String.class){
			return (T)toString(res);
		}else if(clazz == byte[].class){
			return (T)res;
		}else if(clazz == JSONObject.class){
			return (T)JSON.parseObject(toString(res));
		} else if(clazz == org.json.JSONObject.class){
			String retString = toString(res);
			try{
				return (T)new org.json.JSONObject(retString);
			}catch(Exception e){
				LogUtil.e(e);
				return null;
			}
		} else{
			return JSON.toJavaObject(JSON.parseObject(toString(res)), clazz);
		}
	}
	

	public static byte[] readInputStream(InputStream in) throws IOException{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int len = 0;
		byte[] buff = new byte[10*1024];
		while((len = in.read(buff)) != -1){
			out.write(buff, 0 ,len);
		}
		out.close();
		return out.toByteArray();
	}
	
	private static final String toString(byte[] res){
		try{
			return new String(res, "UTF-8");
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	private static String getErrorStackTrace(Throwable ex){
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		return result;
	}
}
