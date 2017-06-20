package in.srain.cube;


import android.app.Application;
import android.content.Context;
import android.provider.Settings;

import in.srain.cube.request.CubeCookieManager;
import in.srain.cube.request.RequestCacheManager;
import in.srain.cube.util.LocalDisplay;
import in.srain.cube.util.NetworkStatusManager;


public class Cube {

    private static Cube instance;

    private Application mApplication;

    private int mTimeout;

    private int mUploadTimeout;


    public static void onCreate(Application app, String requestCachePath, int timeout, int uploadTimeout) {
        instance = new Cube(app, requestCachePath, timeout, uploadTimeout);
    }

    private Cube(Application application, String requestCachePath, int timeout, int uploadTimeout) {
        mApplication = application;
        mTimeout = timeout;
        mUploadTimeout = uploadTimeout;

        // local display
        LocalDisplay.init(application);

        // network status
        NetworkStatusManager.init(application);
        
        // cookie manager
        CubeCookieManager.init(application);
        
        // request cache
        RequestCacheManager.init(application, requestCachePath, 2 * 1024, 10 * 1024);
    }

    public static Cube getInstance() {
        return instance;
    }

    public Context getContext() {
        return mApplication;
    }

    public String getAndroidId() {
        String id = Settings.Secure.getString(mApplication.getContentResolver(), Settings.Secure.ANDROID_ID);
        return id;
    }

    public int getTimeout() {
        return mTimeout;
    }

    public int getUploadTimeout() {
        return mUploadTimeout;
    }
}
