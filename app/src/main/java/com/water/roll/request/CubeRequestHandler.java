package com.water.roll.request;

import com.alibaba.fastjson.JSON;
import com.jovision.xunwei.junior.lib.util.LogUtil;
import com.water.roll.BaseActivity;

import org.json.JSONObject;

import in.srain.cube.concurrent.SimpleTask;
import in.srain.cube.request.CacheAbleRequest;
import in.srain.cube.request.CacheAbleRequestJsonHandler;
import in.srain.cube.request.CubeError;
import in.srain.cube.request.ErrorListener;
import in.srain.cube.request.FailData;
import in.srain.cube.request.JsonData;
import in.srain.cube.request.RequestAble;
import in.srain.cube.request.SuccListener;

public class CubeRequestHandler<T> extends CacheAbleRequestJsonHandler {

    private static final String TAG = CubeRequestHandler.class.getSimpleName();

    private Class<T> mClazz;
    private SuccListener<T> mSuccListener;
    private ErrorListener mErrorListener;
    private CubeRequest mRequest;
    private RequestAble mRequestAble;
    private boolean mShowRetry;

    public CubeRequestHandler(CubeRequest request, Class<T> clazz, RequestAble requestAble, boolean showRetry,
                              SuccListener<T> succListener, ErrorListener errorListener) {
    	this.mRequest = request;
        this.mClazz = clazz;
        this.mRequestAble = requestAble;
        this.mShowRetry = showRetry;
        this.mSuccListener = succListener;
        this.mErrorListener = errorListener;
    }

    @Override
    public void onCacheAbleRequestFinish(final JsonData data, CacheAbleRequest.ResultType type,
                                         boolean outOfDate) {
        mRequest.endTimeout();
        Request.removeRequest(mRequestAble, mRequest);
        SimpleTask.post(new Runnable() {
            public void run() {
                onRequestFinish(data);
            }
        });
    }

    /**
     * 访问服务端成功的回调<br/>
     * 数据格式是正确的，但是，有可能是一个error的响应
     */
    public void onRequestFinish(JsonData data) {
        mRequest.endTimeout();
        Request.removeRequest(mRequestAble, mRequest);
        CubeError error = getError(data);
        if (error == null) {
            T t = convert(data);
            if (t != null) {
                if (mSuccListener != null) {
                	LogUtil.d("onRequestFinish--->" + JSON.toJSONString(t));
                    mSuccListener.onSuccess(mRequest, t);
                }
            }else{
            	 if (mClazz == JSONObject.class){
            		 if (mSuccListener != null) {
            			 LogUtil.d("onRequestFinish--->"+ JSON.toJSONString(t));
                         mSuccListener.onSuccess(mRequest, t);
                     }
            	 }
            }
        } else {
            intercept(error);
            if (mErrorListener != null) {
            	LogUtil.d("onRequestFinish--->"+ JSON.toJSONString(error));
                mErrorListener.onError(mRequest, error);
            }
        }
    }

    /**
     * 访问服务端发生异常的回调
     */
    @Override
    public void onRequestFail(FailData failData) {
        mRequest.endTimeout();
        Request.removeRequest(mRequestAble, mRequest);
        CubeError error = convert(failData);
        intercept(error);
        if (mErrorListener != null) {
        	LogUtil.d("onRequestFail--->"+ JSON.toJSONString(error));
            mErrorListener.onError(mRequest, error);
        }
    }

    private void intercept(CubeError error) {
        if (error == null) {
            return;
        }
        BaseActivity context = mRequest.getContext();
        if (error.errcode == CubeError.ERROR_CODE_NO_NETWORK) {
            //context.openNetErrorDialog();
        } else if (error.errcode == CubeError.ERROR_CODE_SESSION_INVALID || error.errmsg.contains("请登陆")) {// TODO
//            Request.showLoginActivity(context, mRequestAble);
        } else {
            if (mShowRetry) {
                //context.openRetryDialog(mRequestAble, mRequest, error.errmsg, true);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private T convert(JsonData data) {
        Object obj = data.getRawData();
        JSONObject jo = (JSONObject) obj;
        try {
           return convert(jo, mClazz);
        } catch (Exception e) {
            LogUtil.e(TAG, e);
            mErrorListener.onError(mRequest, new CubeError(CubeError.ERROR_CODE_RESPONSE_FORMAT_ERROR));
            return null;
        }
    }


    public static <T> T convert(JSONObject jo, Class<T> clazz) throws Exception {
        JSONObject root = jo.optJSONObject("param");
        if (clazz == JSONObject.class) {
            return (T) root;
        } else if (clazz == com.alibaba.fastjson.JSONObject.class) {
            String str = root.toString();
            return (T) JSON.parseObject(str);
        } else {
            String str = root.toString();
            return JSON.parseObject(str, clazz);
        }
    }

    private CubeError getError(JsonData data) {
        Object obj = data.getRawData();
        if (!(obj instanceof JSONObject)) {
            return new CubeError(CubeError.ERROR_CODE_RESPONSE_FORMAT_ERROR);
        }
        JSONObject jo = (JSONObject) obj;
        int rt = jo.optInt("rt");
        if (rt == 0) {
            return null;
        } else {
            return new CubeError(rt, jo.optString("errmsg"));
        }
    }

    private CubeError convert(FailData failData) {
        if (failData.mErrorType == FailData.ERROR_DATA_FORMAT) {
            return new CubeError(CubeError.ERROR_CODE_RESPONSE_FORMAT_ERROR);
        } else if (failData.mErrorType == FailData.ERROR_INPUT) {
            return new CubeError(CubeError.ERROR_CODE_URL_ERROR);
        } else if (failData.mErrorType == FailData.ERROR_NETWORK) {
            Object data = failData.mData;
            if (data != null && (data instanceof Integer)
                    && Integer.valueOf(data.toString()) == CubeError.ERROR_CODE_NO_NETWORK) {
                return new CubeError(CubeError.ERROR_CODE_NO_NETWORK);
            } else {
                return new CubeError(CubeError.ERROR_CODE_SERVER_ERROR);
            }
        } else {
            return new CubeError(CubeError.ERROR_CODE_SERVER_ERROR);
        }

    }

    public CubeRequest getRequest() {
        return mRequest;
    }
}
