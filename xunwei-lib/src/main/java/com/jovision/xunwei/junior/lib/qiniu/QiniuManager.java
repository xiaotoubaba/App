package com.jovision.xunwei.junior.lib.qiniu;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.jovision.xunwei.junior.lib.BaseApplication;
import com.jovision.xunwei.junior.lib.R;
import com.jovision.xunwei.junior.lib.util.FileUtils;
import com.jovision.xunwei.junior.lib.util.HttpUtil;
import com.jovision.xunwei.junior.lib.util.MD5Util;
import com.jovision.xunwei.junior.lib.util.SpUtil;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONObject;

import java.io.File;

/**
 * Created by Administrator on 2016/4/20.
 */
public class QiniuManager {

//    public static final String HTTP_QINIU_BUCKET_HOST = "http://7xt6an.com1.z0.glb.clouddn.com/";
    public static final String HTTP_QINIU_BUCKET_HOST = "http://o7tc68gzm.bkt.clouddn.com/";
    private static final String DEFAULT_KEY_TAG = "upload/suyuan/img/avatar/";
    private String mKeyTag = DEFAULT_KEY_TAG;
    private String mKeySuffix = "";
    private UploadManager mUploadManager;
    private String mQiniuTokenUrl;

    private static QiniuManager instance = new QiniuManager(null);

    public static QiniuManager getInstnce(){
        return instance;
    }

    private QiniuManager(String keyTag) {
        this(keyTag, null);
    }

    private QiniuManager(String keyTag, String keySuffix) {
        mUploadManager = new UploadManager();
        if (!TextUtils.isEmpty(keyTag)) {
            mKeyTag = keyTag;
        }
        if (!TextUtils.isEmpty(keySuffix)) {
            mKeySuffix = keySuffix;
        }
    }

    public String upload(final String path, final String qiniuTokenUrl, final UploadListener listener) {
        mQiniuTokenUrl = qiniuTokenUrl;
        File file = new File(path);
        final String key = generateQiniuKey(file);
        final String uri = getQiniuUrlByKey(key);
        final UpCompletionHandler handler = new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo responseInfo, JSONObject jsonObject) {
                if (listener == null) {
                    return;
                }
                if (responseInfo.isOK()) {
                    listener.onUploadComplete(true, uri, null);
                } else {
                    listener.onUploadComplete(false, uri, responseInfo.error );
                }
            }
        };
        final UploadOptions options = new UploadOptions(null, null, false, new UpProgressHandler() {
            @Override
            public void progress(String key, double percent) {
                if (listener != null) {
                    listener.onUploadProgress(percent, uri);
                }
            }
        }, null);

        new AsyncTask<Integer, Integer, Integer>() {
            @Override
            protected Integer doInBackground(Integer... params) {
                String token = getQiniuToken(key);
                if(TextUtils.isEmpty(token)){
                    if (listener != null) {
                        listener.onUploadComplete(false, uri, BaseApplication.getContext().getResources().getString(R.string.toast_get_qiniu_token_err));
                        return null;
                    }
                }
                mUploadManager.put(path, key, token, handler, options);
                return null;
            }
        }.execute(0);
        return uri;
    }

    private String getQiniuToken(String key) {
        HttpUtil.Response<QiniuToken> res = HttpUtil.postJson(mQiniuTokenUrl,getRequestData(), QiniuToken.class);
        if(res != null){
            QiniuToken qiniuToken = res.getContent();
            if(qiniuToken != null){
                return qiniuToken.getParam().getString("token");
            }
        }
        return null;
    }

    public static String getQiniuUrlByKey(String key) {
        if (key.startsWith("http://") || key.startsWith("file://")) {
            return key;
        }
        return HTTP_QINIU_BUCKET_HOST + key;
    }

    /**
     * relative path on qiniu server (key for qiniu). we need this path to save image to qiniu.
     */
    public String generateQiniuKey(File file) {
        String suffix = FileUtils.getFileSuffix(file);
        if (!TextUtils.isEmpty(suffix)) {
            // use origin file suffix
            suffix = "." + suffix;
        } else {
            suffix = mKeySuffix;
        }
        return mKeyTag + MD5Util.toMD5(file) + suffix;
    }

    private String getRequestData(){
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(" \"param\":{ ");
        sb.append("            \"session\":\"").append(SpUtil.getSp().read(SpUtil.SpKey.SESSION, "")).append("\"");
        sb.append("            }");
        sb.append("}");
        return sb.toString();
    }
}
