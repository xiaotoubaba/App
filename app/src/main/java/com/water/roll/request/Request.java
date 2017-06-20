
package com.water.roll.request;

import com.alibaba.fastjson.JSON;
import com.jovision.xunwei.junior.lib.util.LogUtil;
import com.water.roll.BaseActivity;
import com.water.roll.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import in.srain.cube.request.CachePolicy;
import in.srain.cube.request.ErrorListener;
import in.srain.cube.request.RequestAble;
import in.srain.cube.request.RequestCacheManager;
import in.srain.cube.request.SuccListener;

//import com.zhongwei.xunwei.communistparty.activity.LoginActivity;

public class Request {

    private static final String MID_NAME = "mid";
    public static final int TIMEOUT = 30 * 1000;
    public static final int UPLOAD_TIMEOUT = 60 * 1000;
    public static final int CACHE_TIME = 60;

    private static AtomicInteger MID = new AtomicInteger(1);
    /** protected by Request.class */
    private static HashMap<RequestAble, List<CubeRequest>> mRequestHolder = new HashMap<RequestAble, List<CubeRequest>>();

    private RequestAble mRequestAble;
    private BaseActivity mContext;
    
    private Request(RequestAble requestAble) {
        this.mRequestAble = requestAble;
        if (mRequestAble instanceof BaseActivity) {
            mContext = (BaseActivity) mRequestAble;
        } else if (mRequestAble instanceof BaseFragment) {
            BaseFragment fragment = (BaseFragment) mRequestAble;
            mContext = (BaseActivity) fragment.getActivity();
        }
    }

    public static Request getRequest(RequestAble requestAble) {
        return new Request(requestAble);
    }
    
    /***
     * 
     * @param url
     * @param clazz
     * @param succListener
     * @param errorListener
     * @return
     */
    public <Req, Res> CubeRequest post(String url, final Class<Res> clazz, final SuccListener<Res> succListener,
                                       final ErrorListener errorListener) {
        return post(url, clazz, null, CachePolicy.NONE, succListener, errorListener);
    }

    /**
     * 发送post请求
     * 
     * @param url 请求的url
     * @param clazz 响应的class
     * @param params 参数
     * @param succListener 成功的回调
     * @param errorListener 失败的回调
     */
    public <Req, Res> CubeRequest post(String url, final Class<Res> clazz, Req params,
                                       final SuccListener<Res> succListener, final ErrorListener errorListener) {
        return post(url, clazz, params, CachePolicy.NONE, succListener, errorListener);
    }

    /**
     * 发送post请求
     * @param url 请求的url
     * @param clazz 响应的class
     * @param params 参数
     * @param policy 缓存策略
     * @param succListener 成功的回调
     * @param errorListener 失败的回调
     */
    public <Req, Res> CubeRequest post(String url, final Class<Res> clazz, Req params,
                                       final CachePolicy policy, final SuccListener<Res> succListener,
                                       final ErrorListener errorListener) {
        return post(url, clazz, params, false, policy, succListener, errorListener);
    }

    public <Req, Res> CubeRequest post(String url, final Class<Res> clazz, Req params,
                                       boolean showRetry, final CachePolicy policy, final SuccListener<Res> succListener,
                                       final ErrorListener errorListener) {
        return post(url,clazz,params, showRetry,policy,true, succListener,errorListener);
    }
    /**
     * 发送post请求
     * 
     * @param url 请求的url
     * @param clazz 响应的class
     * @param params 参数
     * @param showRetry 是否展示失败重试
     * @param policy 缓存策略
     * @param succListener 成功的回调
     * @param errorListener 失败的回调
     */
    public <Req, Res> CubeRequest post(String url, final Class<Res> clazz, Req params,
                                       boolean showRetry, final CachePolicy policy, final boolean isAsync, final SuccListener<Res> succListener,
                                       final ErrorListener errorListener) {
        if (interceptLogin(url)) {
            return null;
        }
        CubeRequest request = new CubeRequest();
        CubeRequestHandler<Res> requestHandler = new CubeRequestHandler<Res>(request, clazz, mRequestAble, showRetry, succListener, errorListener);
        request.setCacheAbleRequestHandler(requestHandler);
        request.setContext(mContext);
        
        if (policy == CachePolicy.NONE) {
            request.setDisableCache(true);
            request.setCacheTime(0);
        } else if (policy == CachePolicy.ALWAYS) {
            request.setUseCacheAnyway(true);
            request.setCacheTime(CACHE_TIME);
        } else if (policy == CachePolicy.VALID) {
            request.setUseCacheAnyway(false);
            request.setCacheTime(CACHE_TIME);
        }
        request.setTimeout(TIMEOUT);
        request.getRequestData().setRequestUrl(url);
        request.setRequestIdKey(MID_NAME);
        request.setRequestIdValue(MID.incrementAndGet());
        if(params != null){
            request.getRequestData().addHeader("Content-Type", "application/json");
            request.getRequestData().addPostData("_json_", getPostData(url, request.getRequestIdValue(), params));
        }
        String cacheKey = request.getCacheKey();
        request.setCacheKey(cacheKey);
        if (addRequest(mRequestAble, request)) {
            request.doRequestFakeAsync();
        }
        return request;
    }
    public  static <Req> RequestBean<Req> getPostData(String url, int mid, Req params){
    	RequestBean<Req> requestBean = new RequestBean<Req>();
        requestBean.setMethod(getMethod(url));
        requestBean.setMid(mid);
        requestBean.setParam(params);
        LogUtil.d("request--->" + JSON.toJSONString(requestBean));
        return requestBean;
    }
    
    public static void deleteCache(CubeRequest request) {
        if (request == null) {
            return;
        }
        deleteCache(request.getCacheKey());
    }

    public static void deleteCache(String cacheKey) {
        if (cacheKey == null || cacheKey.length() <= 0) {
            return;
        }
        RequestCacheManager.getInstance().invalidateCache(cacheKey);
    }

    /**
     * 重试
     * 
     * @param requestAble
     * @param request
     */
    public static void redo(RequestAble requestAble, CubeRequest request) {
        if (request == null) {
            return;
        }
        request.setHasBeenCanceled(false);
        request.doRequestFakeAsync();
        addRequest(requestAble, request);
    }

    /**
     * 取消请求
     * 
     * @param mRequestAble
     */
    public static void cancel(RequestAble mRequestAble) {
        synchronized (Request.class) {
            List<CubeRequest> list = mRequestHolder.get(mRequestAble);
            if (list == null || list.size() <= 0) {
                return;
            }
            for (CubeRequest request : list) {
                request.cancelRequest();
            }
            mRequestHolder.remove(mRequestAble);
        }
    }

    private static boolean addRequest(RequestAble requestAble, CubeRequest request) {
        synchronized (Request.class) {
            if (request == null) {
                return false;
            }
            List<CubeRequest> list = mRequestHolder.get(requestAble);
            if (list == null) {
                list = new ArrayList<CubeRequest>();
                mRequestHolder.put(requestAble, list);
            }
            // 防止重复添加,快速多次下拉刷新，实际上对应的是同一个请求
            for (int i = 0; i < list.size(); i++) {
                CubeRequest runningRequest = list.get(i);
                if (equal(runningRequest, request)) {
                    return false;
                }
            }
            list.add(request);
            return true;
        }
    }

    private static boolean equal(CubeRequest runningRequest, CubeRequest request) {
        if (runningRequest == null || request == null) {
            return false;
        }
        if (runningRequest.getCacheKey() != null) {
            return runningRequest.getCacheKey().equals(request.getCacheKey());
        }
        return false;
    }

    public static void removeRequest(RequestAble requestAble, CubeRequest request) {
        synchronized (Request.class) {
            if (request == null) {
                return;
            }
            List<CubeRequest> list = mRequestHolder.get(requestAble);
            if (list == null || list.size() <= 0) {
                return;
            }
            for (Iterator<CubeRequest> it = list.iterator(); it.hasNext();) {
                CubeRequest r = it.next();
                if (r == request) {
                    it.remove();
                }
            }
            if (list.size() <= 0) {
                mRequestHolder.remove(requestAble);
            }
        }
    }

//    public static void showLoginActivity(Context mContext, RequestAble mRequestAble) {
//        Intent i = new Intent(mContext, LoginActivity.class);
//        i.putExtra(Contants.BundleKey.IS_GO_BACK, true);
//        if (mRequestAble instanceof BaseActivity) {
//            i.putExtra(Contants.BundleKey.IS_ACTIVITY, true);
//            ((BaseActivity) mRequestAble).startActivityForResult(i, Contants.RequestCode.REQUEST_LOGIN);
//        } else {
//            i.putExtra(Contants.BundleKey.IS_ACTIVITY, false);
//            ((BaseFragment) mRequestAble).startActivityForResult(i, Contants.RequestCode.REQUEST_LOGIN);
//        }
//    }

    /**
     * 检测是否需要登陆
     * 
     * @param url
     * @return
     */
    private boolean interceptLogin(String url) {
        if (needShowLoginActivity(url)) {
            //showLoginActivity(mContext, mRequestAble);
            return true;
        }
        return false;
    }

    private static boolean needShowLoginActivity(String url) {
        String sid = getSessionId();
        if (sid != null && sid.length() > 0) {
            return false;
        }
        APIConfig config = APIConfigManager.getInstance().getAPIConfig(url);
        if (config == null) {
            return false;
        }
        return config.isNeedLogin();
    }

    private static String getSessionId(){
    	return "";
    } 
    
    private static String getMethod(String url){
    	//http://localhost:8080/netalarm-rs/rsapi/userauth/login
    	if(url == null || url.length() <= 0){
    		return "";
    	}
    	if(url.indexOf("?") > 0){
    		url = url.substring(0, url.indexOf("?"));
    	}
    	if(url.endsWith("/")){
    		url = url.substring(0, url.length() - 1);
    	}
    	int index = url.lastIndexOf("/");
    	if(index < 0){
    		return "";
    	}
    	return url.substring(index+1);
    	
    }
}
