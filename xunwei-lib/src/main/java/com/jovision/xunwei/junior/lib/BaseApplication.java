package com.jovision.xunwei.junior.lib;

import android.app.Application;
import android.content.Context;

import com.jovision.xunwei.junior.lib.exception.CrashHanlderExcetpion;
import com.jovision.xunwei.junior.lib.util.LogUtil;

public class BaseApplication extends Application{
	
	private static Context mContext;
	 
	@Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();
        
        CrashHanlderExcetpion.getInstance().init(this);
        
        //log
        LogUtil.setLevel(LogUtil.Level.V);

	}
	
	public static Context getContext(){
        return mContext;
    }

}
