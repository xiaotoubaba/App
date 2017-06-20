
package com.water.roll.request;


import com.water.roll.BaseActivity;

import in.srain.cube.concurrent.SimpleTask;
import in.srain.cube.request.CacheAbleRequest;
import in.srain.cube.request.CacheAbleRequestHandler;
import in.srain.cube.request.CubeError;
import in.srain.cube.request.DefaultRequestProxy;
import in.srain.cube.request.IRequest;
import in.srain.cube.request.JsonData;
import in.srain.cube.request.RequestCacheManager;
import in.srain.cube.request.RequestManager;

public class CubeRequest extends CacheAbleRequest<JsonData> {

   
    private String mRequestIdKey;
    private int mRequestIdValue;
    
    private int mTimeout;
    private BaseActivity mContext;
    private CacheAbleRequestHandler<JsonData> mRequestHandler;

    private SimpleTask mTimeoutTask = new SimpleTask() {
        @Override
        public void doInBackground() {
        }

        @Override
        public void onFinish(boolean canceled) {
            if (!canceled) {
                doTimeout();
            }
        }
    };

    private Runnable mAsyncTask = new Runnable() {
        @Override
        public void run() {
            // 1. 先查缓存,如果缓存有效，回调onCacheData，但是是在子线程
            JsonData data = RequestCacheManager.getInstance().requestCacheSync(CubeRequest.this);
            // 假如缓存为空，要么不存在，要么失效并且不是总使用缓存
            if (data == null) {
                // 2. 再访问网络
                data = RequestManager.getInstance().getRequestProxy(CubeRequest.this).requestSync(CubeRequest.this);
            } else {// 如果缓存不空，回调onCacheData了，这个分支不应该再有回调，只是后台更新
                if (isOutOfDate()) {// 3.后台异步更新缓存，不回调
                    asyncUpdateCache(CubeRequest.this);
                }
            }
        }
    };

    protected JsonData doRequestFakeAsync() {
        // 1. 发请求
        new Thread(mAsyncTask).start();
        // 2. 计时
        beginTimeout();
        return null;
    }

    @Override
    public void send() {
        beginTimeout();
        super.send();
    }

    @Override
    public void cancelRequest() {

        super.cancelRequest();

        if (mTimeoutTask != null) {
            mTimeoutTask.cancel();
        }
    }

    private void beginTimeout() {
        if (mTimeout > 0) {
            SimpleTask.postDelay(mTimeoutTask, mTimeout);
        }
    }

    private void asyncUpdateCache(final IRequest<JsonData> request) {
        new Thread(new Runnable() {
            public void run() {
                // 只后台更新，不回调UI
            	DefaultRequestProxy.doSyncRequest(CubeRequest.this);
            }
        }).start();
    }

    private void doTimeout() {
        endTimeout();

        if (hasBeenCanceled()) {
            return;
        }
        // 把请求cancel掉
        this.cancelRequest();
        // 提示超时
        JsonData data = JsonData.create(jsonTimeout());
        mRequestHandler.onRequestFinish(data);
    }

    public void endTimeout() {
        mTimeoutTask.cancel();
    }

    public void setTimeout(int timeout) {
        super.setTimeout(timeout);
        this.mTimeout = timeout;
    }

    @Override
    public void setCacheAbleRequestHandler(CacheAbleRequestHandler<JsonData> handler) {
        super.setCacheAbleRequestHandler(handler);
        this.mRequestHandler = handler;
    }

    public BaseActivity getContext() {
        return mContext;
    }

    public void setContext(BaseActivity context) {
        this.mContext = context;
    }

	public void setRequestIdValue(int mRequestId) {
		this.mRequestIdValue = mRequestId;
	}
	
	public int getRequestIdValue(){
		return this.mRequestIdValue;
	}
	
	public void setRequestIdKey(String mRequestIdKey) {
		this.mRequestIdKey = mRequestIdKey;
	}
	
	public String getRequestIdKey() {
		return mRequestIdKey;
	}

	/**
     * taskId、taskKey、sid不作为cache key
     */
    @Override
    public String getCacheKey() {
    	String cacheKey = getRequestData().getCacheKey();
    	if(cacheKey != null && cacheKey.length() > 0){
    		return cacheKey;
    	}
        String key = getRequestData().getRequestUrl();
        if (key == null) {
            return null;
        }
        key = removeHostPort(key);
        key = removeQueryString(key, mRequestIdKey);
        
        String post = getRequestData().getPostString();
        if(post != null && post.length() > 0){
        	post = removeJsonKey(post, mRequestIdKey);
        	key = key + post;
        }
        key = key.replace("/", "-");
        key = key.replace("?", "-");
        key = key.replace("=", "_");
        key = key.replace("&", "-");
        key = key.replace("{", "");
        key = key.replace("}", "");
        key = key.replace("\"", "");
        key = key.replace(",", "-");
        key = key.replace(":", "-");
        getRequestData().setCacheKey(key);
        return key;
    }
    
    private static String removeJsonKey(final String src, final String key){
    	 String result = "";
    	 int pos = src.indexOf("\""+key+"\"");
         if (pos < 0) {
             return src;
         }
         result = src.substring(0, pos);
         int comma = src.indexOf(",", pos);
         if (comma == -1) {
             return result;
         }
         result += src.substring(comma + 1);
         return result;
    }
    
    private static String removeQueryString(final String src, final String paramName) {
        String result = "";
        int pos = src.indexOf(paramName);
        if (pos < 0) {
            return src;
        }
        result = src.substring(0, pos);
        int and = src.indexOf("&", pos);
        if (and == -1) {
            return result;
        }
        result += src.substring(and + 1);
        return result;
    }

    private static String removeHostPort(String src) {
        int pos = src.indexOf("://");
        int slash = src.indexOf("/", pos + 3);
        return src.substring(slash + 1);
    }

    private String jsonTimeout() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("    \"root\": {");
        sb.append("        \"msg\": \"请求超时\",");
        sb.append("        \"result\": \"fasle\",");
        sb.append("        \"taskData\": {");
        sb.append("            \"taskId\": " + mRequestIdValue + ",");
        sb.append("            \"errorCode\": \"" + CubeError.ERROR_CODE_TIMEOUT + "\",");
        sb.append("            \"taskKey\":0");
        sb.append("        }");
        sb.append("    }");
        sb.append("}");
        return sb.toString();
    }
}
