package com.jovision.xunwei.junior.lib.util;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Log的工具类
 */
public class LogUtil {

	private static final String TAG = "junior";
	private static int level = Level.V;
	
	public static class Level{
		public static final int V = 1;
		public static final int D = 2;
		public static final int I = 3;
		public static final int W = 4;
		public static final int E = 5;
		public static final int NON = Integer.MAX_VALUE;
	}
	
	public static void setLevel(int level){
		LogUtil.level = level;
	}

	public static void v(String msg) {
		v(TAG, msg);
	}
	public static void d(String msg) {
		d(TAG, msg);
	}
	public static void i(String msg) {
		i(TAG, msg);
	}
	public static void w(String msg) {
		w(TAG, msg);
	}
	public static void e(String msg) {
		e(TAG, msg);
	}
	public static void e(Throwable e) {
		e(TAG, e);
	}

	public static void e(String tag, Throwable e) {
		e(tag, getStackTrace(e));
	}
	
	public static void v(String tag, String msg) {
		if (level <= Level.V){
			Log.v(tag, msg);
		}
	}
	
	public static void d(String tag, String msg) {
		if (level <= Level.D){
			Log.d(tag, msg);
		}
	}
	
	public static void i(String tag, String msg) {
		if (level <= Level.I){
			Log.i(tag, msg);
		}
	}
	
	public static void w(String tag, String msg) {
		if (level <= Level.W){
			Log.w(tag, msg);
		}
	}
	
	public static void e(String tag, String msg) {
		if (level <= Level.E){
			Log.e(tag, msg);
		}
	}
	
	private static String getStackTrace(Throwable ex){
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		return writer.toString();
	}
}
