package com.jovision.xunwei.junior.lib.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.util.Log;

import com.jovision.xunwei.junior.lib.BaseApplication;

import java.util.HashMap;
import java.util.Map;


public class SpUtil {
	
	private final static String TAG = SpUtil.class.getSimpleName();

	private static volatile Map<String, SpDecorator> instanceHolder = new HashMap<String, SpDecorator>();
	// private
	private SpUtil(){}

	public static SpDecorator getSp() {
		return getSp(null);
	}

	public static SpDecorator getSp(String name) {
		if(name == null || name.length() <= 0){
			name = getDefaultSpName();
		}
		SpDecorator sp = instanceHolder.get(name);
		if(sp == null){
			synchronized(SpUtil.class){
				sp = instanceHolder.get(name);
				if(sp == null){
					String xmlFileName = buildSharedPreferencesFileName(name);
					SharedPreferences s = BaseApplication.getContext().getSharedPreferences(xmlFileName, Context.MODE_PRIVATE);
					sp = new SpDecorator(s, xmlFileName);
					instanceHolder.put(name, sp);
				}
			}
		}
		return sp;
	}

	private static String getDefaultSpName(){
		String pName = getCurrentProcessName();
		if(pName != null && pName.length() > 0){
			int pos = pName.indexOf(":");
			if(pos>0){
				pName = pName.substring(0, pos);
			}
			pName=pName.replace(".", "_");
			return pName;
		}else{
			return "default_sp";
		}
	}

	/**
	 * 构造存储在硬盘的文件名<br/>
	 * 因为我们的项目中会把service注册成运行在单独的进程，因此Application的onCreate()会被调用多次，要保证不同的进程绝对不能操作同一个文件！
	 * 
	 * */
	private static String buildSharedPreferencesFileName(String xmlFileName){
		String pName = getCurrentProcessName();
        if(pName != null){
        	pName = pName.replaceAll("[^\\w]+", "_");
        }
        return xmlFileName + "_" + pName;
	}
	
	/**
	 * 获取当前的进程名
	 * 
	 * */
	private static String getCurrentProcessName() {
		int pid = android.os.Process.myPid();
		ActivityManager mActivityManager = (ActivityManager)BaseApplication.getContext().getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
			  if (appProcess.pid == pid) {
				  return appProcess.processName;
			  }
		}
		return null;
	}

	@SuppressLint("CommitPrefEdits")
	public static class SpDecorator {

		private SharedPreferences sp;
		private Editor editor;
		private String fileName;
		private boolean useApply;

		private SpDecorator(SharedPreferences sp,String fileName){
			this.sp = sp;
			this.fileName = fileName;
			this.editor = sp.edit();
			this.useApply  = Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;

		}
		/**
		 * 取一个String
		 *
		 * @param key
		 * @param defaultvalue
		 *
		 * @return key对应的String
		 */
		public String read(String key, String defaultvalue){
			if (key == null || key.length() <= 0) {
				return defaultvalue;
			}
			return sp.getString(key, defaultvalue);
		}

		/**
		 * 取一个int
		 *
		 * @param key
		 * @param defaultvalue
		 *
		 * @return key对应的int
		 */
		public int read(String key, int defaultvalue){
			if (key == null || key.length() <= 0) {
				return defaultvalue;
			}
			return sp.getInt(key, defaultvalue);
		}

		/**
		 * 取一个long
		 *
		 * @param key
		 * @param defaultvalue
		 *
		 * @return key对应的long
		 */
		public long read(String key, long defaultvalue){
			if (key == null || key.length() <= 0) {
				return defaultvalue;
			}
			return sp.getLong(key, defaultvalue);
		}

		/**
		 * 取一个boolean
		 *
		 * @param key
		 * @param defaultvalue
		 *
		 * @return key对应的boolean
		 */
		public boolean read(String key, boolean defaultvalue){
			if (key == null || key.length() <= 0) {
				return defaultvalue;
			}
			return sp.getBoolean(key, defaultvalue);
		}

		/**
		 * 取一个float
		 *
		 * @param key
		 * @param defaultvalue
		 *
		 * @return key对应的float
		 */
		public float read(String key, float defaultvalue)  {
			if (key == null || key.length() <= 0) {
				return defaultvalue;
			}
			return sp.getFloat(key, defaultvalue);
		}


		/**
		 * 取出xml中全部内容
		 *
		 * @return Map<String,?>
		 */
		public Map<String, ?> readAllValues()  {
			return sp.getAll();
		}

		/**
		 * 是否存在当前的key
		 *
		 * @param version
		 * @param key
		 *
		 * @return boolean
		 */
		public boolean isContains(String version, String key){
			if (key == null || key.length() <= 0) {
				return false;
			}
			return sp.contains(key);
		}
		/**
		 * 存一个值<br>
		 * value的类型仅支持int/Integer,boolean/Boolean,String,long/Long,float/Float
		 *
		 * @param key
		 * @param value
		 *
		 */
		public void write(String key, Object value) {
			if (key == null || value == null) {
				return;
			}
			try {
				writeToEditor(key, value);
				commit();
			} catch (Throwable e) {
				Log.w(TAG, "write SharedPreferences error,key:" + key + ",value:" + value +",xmlFileName:"+fileName);
			}
		}

		/**
		 * 批量保存<br>
		 * value的类型仅支持int/Integer,boolean/Boolean,String,long/Long,float/Float
		 *
		 * @param map
		 */
		public void batchWrite(HashMap<String,Object> map){
			if(map == null || map.size() <= 0){
				return;
			}
			try{
				for(Map.Entry<String, Object> entry : map.entrySet()){
					String key = entry.getKey();
					Object value = entry.getValue();
					if(key == null || value == null){
						continue;
					}
					writeToEditor(key,value);
				}
				commit();
			}catch(Throwable e){
				Log.w(TAG, "write SharedPreferences error,map:" + map + ",xmlFileName:" + fileName);
			}
		}

		/**
		 * 删除某一key
		 *
		 * @param key
		 */
		public void remove(String key) {
			if (key == null || key.length() <= 0) {
				return;
			}
			editor.remove(key);
			commit();
		}

		/**
		 * 清空xml下所有值
		 */
		public void removeAll()  {
			editor.clear();
			commit();
		}

		private void commit(){
			if (useApply) {
				// Since API Level 9, apply() is provided for asynchronous
				// operations
				editor.apply();
			} else {
				// Fallback to syncrhonous if not available
				editor.commit();
			}
		}

		private void writeToEditor(String key, Object value) {
			Class<?> clazz = value.getClass();
			if(clazz == Integer.class || clazz == int.class){
				editor.putInt(key, (Integer) value);
			}else if(clazz == String.class){
				editor.putString(key, (String)value);
			}else if(clazz == Boolean.class || clazz == boolean.class){
				editor.putBoolean(key, (Boolean)value);
			}else if(clazz == Long.class || clazz == long.class){
				editor.putLong(key, (Long)value);
			}else if(clazz == Float.class || clazz == float.class){
				editor.putFloat(key, (Float) value);
			} else{
				Log.w(TAG, "unsupported class type:" + clazz + ",key:" + key + ",value:" + value + ",xmlFileName:" + fileName);
			}
		}
	}

	public static class SpKey {
		public static final String LAST_LOGIN_NAME="last_login_name";
		public static final String LAST_LOGIN_PWD ="last_login_pwd";
		public static final String MANUAL_LOGOUT ="manual_logout";
		public static final String GESTURE_KEY ="gesture_key";
		public static final String SESSION ="session";
		public static final String APPID ="app_id";
		public static final String USERGUID ="userguid";
		public static final String PERSON_NAME ="person_name";
		public static final String PERSON_SEX ="person_sex";
		public static final String PHOTO ="photo";
		public static final String PHONE ="phone";
		public static final String EMAIL ="email";
		public static final String PROJECT ="project";
		public static final String AREA_LIST ="area_list";
		public static final String USER_ID ="user_id";
		public static final String USER_NAME ="user_name";
		public static final String AREA_ID ="area_id";
		public static final String ROLE_TYPE ="role_type";
		public static final String POLICE_STATION_ID ="police_station_id";
		public static final String ROLE_TYPE_NAME ="role_type_name";
		public static final String PIC_URL ="pic_url";
		public static final String DEBATE_ID ="debateId";
		public static final String PUSH_REGISTER_ID ="push_register_id";
		public static final String AVATAR_FULL_PATH ="avatar_path";
		public static final String MAP_LAT ="lat";
		public static final String MAP_LON ="lon";
	}

}