package com.water.roll.request;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class APIConfigManager {
	
	private static APIConfigManager instance = new APIConfigManager();
	
	private APIConfigManager(){}
	
	public static APIConfigManager getInstance(){
		return instance;
	}
	
	private Map<String, APIConfig> apiSessionMap = new HashMap<String, APIConfig>();
	
	public APIConfig getAPIConfig(String url){
		if(url == null){
			return null;
		}
		url = format(url);
		APIConfig config = apiSessionMap.get(url);
		if(config == null){
			config = new APIConfig();
			config.setApi(url);
			config.setNeedLogin(needLogin(url));
			apiSessionMap.put(url, config);
		}
		return config;
	}

	private String format(String url) {
		if(url.indexOf("?") >= 0){
			return url.substring(0, url.indexOf("?"));
		}
		return url;
	}
	
	private boolean needLogin(String url){
		Field[] fields = API.class.getFields();
		if(fields == null || fields.length <= 0){
			return false;
		}
		try{
			String sessionFiledName = "";
			for(int i=0; i<fields.length; i++){
				Field field = fields[i];
				String fieldName = field.getName();
				Object fieldValue= field.get(null);
				if(url.equals(fieldValue)){
					sessionFiledName = fieldName +"_NEED_LOGIN";
					break;
				}
			}
			if("".equals(sessionFiledName)){
				return false;
			}
			
			for(int i=0; i<fields.length; i++){
				Field field = fields[i];
				String fieldName = field.getName();
				Object fieldValue= field.get(null);
				if(sessionFiledName.equals(fieldName)){
					return Boolean.valueOf(fieldValue.toString());
				}
			}
			return false;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
	}
}
