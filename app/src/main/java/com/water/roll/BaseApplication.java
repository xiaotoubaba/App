package com.water.roll;

import android.content.Context;

import in.srain.cube.Cube;


public class BaseApplication extends com.jovision.xunwei.junior.lib.BaseApplication {

    private static AppSettings appSettings;

    private static Context mContext;

    private static final String MID_NAME = "mid";
    public static final int TIMEOUT = 30 * 1000;
    public static final int UPLOAD_TIMEOUT = 60 * 1000;
    public static final int CACHE_TIME = 60;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
        //mob
//        initMob();

        //cache
        AppCacheManager.init(this);

        //universal image loader
        ImageLoaderManager.initImageLoader(this, AppCacheManager.cache_image_dir);

        // Cube Framework
        Cube.onCreate(this, AppCacheManager.cache_request_dir.getAbsolutePath(), TIMEOUT, UPLOAD_TIMEOUT);

        initAppSettings();
    }

//    private void initMob(){
//        SMSSDK.initSDK(this, Contants.ThirdPartKey.Mob.APP_KEY, Contants.ThirdPartKey.Mob.APP_SECRET);
//    }

    private void initAppSettings() {
        appSettings = new AppSettings();
        appSettings.setMainColorResId(R.color.main_color);
    }

    public static AppSettings getAppSettings() {
        return appSettings;
    }

    public static Context getContext(){
        return mContext;
    }
}
