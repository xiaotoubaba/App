package com.water.roll.util;

import com.jovision.xunwei.junior.lib.util.LogUtil;
import com.jovision.xunwei.junior.lib.util.SpUtil;
import com.water.roll.request.Request;
import com.water.roll.request.req.LoginRequest;
import com.water.roll.request.res.LoginResult;

import in.srain.cube.request.CachePolicy;
import in.srain.cube.request.CubeError;
import in.srain.cube.request.ErrorListener;
import in.srain.cube.request.IRequest;
import in.srain.cube.request.RequestAble;
import in.srain.cube.request.SuccListener;

public class LoginUtil {

    public static void doLogin(RequestAble requestAble, final String url, final String username, final String passwordEncoded, final OnLoginListener onLoginListener) {
        LoginRequest request = new LoginRequest();
        request.setUsername(username);
        request.setPassword(passwordEncoded);
        Request.getRequest(requestAble).post(url, LoginResult.class, request, false, CachePolicy.NONE,
                new SuccListener<LoginResult>() {
                    @Override
                    public void onSuccess(IRequest<?> request, LoginResult result) {
                        SpUtil.getSp().write(SpUtil.SpKey.LAST_LOGIN_NAME, username);
                        SpUtil.getSp().write(SpUtil.SpKey.LAST_LOGIN_PWD, passwordEncoded);
                        SpUtil.getSp().write(SpUtil.SpKey.MANUAL_LOGOUT, false);
                        SpUtil.getSp().write(SpUtil.SpKey.SESSION, result.getSession());
                        SpUtil.getSp().write(SpUtil.SpKey.APPID, result.getAppId());
                        SpUtil.getSp().write(SpUtil.SpKey.USERGUID, result.getUserGuid());
                        SpUtil.getSp().write(SpUtil.SpKey.PERSON_NAME, result.getPersonName());
                        SpUtil.getSp().write(SpUtil.SpKey.PHONE, result.getPhone());
                        SpUtil.getSp().write(SpUtil.SpKey.PHOTO, result.getPhoto());
                        SpUtil.getSp().write(SpUtil.SpKey.EMAIL, result.getEmail());
                        SpUtil.getSp().write(SpUtil.SpKey.PROJECT, result.getProject());
                        LogUtil.d("longin success, session:" + result.getSession());
                        onLoginListener.onLoginSuccess(username, passwordEncoded, result);

                    }
                },
                new ErrorListener() {
                    @Override
                    public void onError(IRequest<?> request, CubeError error) {
                        onLoginListener.onLoginFailure(error.getErrcode(), error.getErrmsg());
                    }
                });
    }

    public interface OnLoginListener {
        public void onLoginSuccess(String username, String passwordEncoded, LoginResult result);

        public void onLoginFailure(int errcode, String ermsg);
    }
}
