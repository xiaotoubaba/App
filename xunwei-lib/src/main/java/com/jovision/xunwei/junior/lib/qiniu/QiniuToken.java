package com.jovision.xunwei.junior.lib.qiniu;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by Administrator on 2016/4/20.
 */
public class QiniuToken {
    private int mid;
    private int rt;
    private String errmsg;
    private String method;
    private JSONObject param;

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getRt() {
        return rt;
    }

    public void setRt(int rt) {
        this.rt = rt;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public JSONObject getParam() {
        return param;
    }

    public void setParam(JSONObject param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return "QiniuToken{" +
                "mid=" + mid +
                ", rt=" + rt +
                ", errmsg='" + errmsg + '\'' +
                ", method='" + method + '\'' +
                ", param=" + param +
                '}';
    }
}
