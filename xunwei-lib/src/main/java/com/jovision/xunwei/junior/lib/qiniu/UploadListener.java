package com.jovision.xunwei.junior.lib.qiniu;

/**
 * Created by zhangzz on 15-4-10.
 * qiniu upload listener
 */
public interface UploadListener {

    /**
     * callback for upload complete event
     * @param success whether upload is success
     * @param uri target uri
     */
    public void onUploadComplete(boolean success, String uri, String errmsg);

    /**
     * callback for upload progress event
     * @param percent percent of the progress, 0.00 - 1.00
     * @param uri target uri
     */
    public void onUploadProgress(double percent, String uri);

}
