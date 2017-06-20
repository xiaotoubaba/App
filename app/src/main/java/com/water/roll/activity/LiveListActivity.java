package com.water.roll.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;

import com.jovision.xunwei.junior.lib.util.LogUtil;
import com.jovision.xunwei.junior.lib.util.SpUtil;
import com.water.roll.BaseActivity;
import com.water.roll.R;
import com.water.roll.request.API;
import com.water.roll.request.res.LoginResult;
import com.water.roll.util.Contants;
import com.water.roll.util.LoginUtil;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import in.srain.cube.request.CubeError;

public class LiveListActivity extends BaseActivity {

    private static final String TAG = LiveListActivity.class.getSimpleName();

    private CountDownLatch mLacth = new CountDownLatch(1);
    private boolean mLoginSucc = false;
    private LoginResult mLoginResult;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //做自动登录
        mSuperHandler.postDelayed(new Runnable(){
            public void run() {
                autoLogin();
            }
        }, 2300);
        //2秒以后跳转
        mSuperHandler.postDelayed(new JumpTask(), 5000);
    }

    private void autoLogin() {

        final String username = SpUtil.getSp().read(SpUtil.SpKey.LAST_LOGIN_NAME, "");
        final String passwordEncoded = SpUtil.getSp().read(SpUtil.SpKey.LAST_LOGIN_PWD, "");
        boolean manualLogout = SpUtil.getSp().read(SpUtil.SpKey.MANUAL_LOGOUT, false);

        if (manualLogout || TextUtils.isEmpty(username)) {
            mLoginSucc = false;
            mLacth.countDown();
            return;
        }

        LoginUtil.doLogin(this, API.LOGIN_URL, username, passwordEncoded, new LoginUtil.OnLoginListener() {
            @Override
            public void onLoginSuccess(String username, String passwordEncoded, LoginResult result) {
                mLacth.countDown();
                mLoginSucc = true;
                mLoginResult = result;
                LogUtil.d("autoLogin, onLoginSuccess");
            }

            @Override
            public void onLoginFailure(int errcode, String ermsg) {
                mLacth.countDown();
                mLoginSucc = false;
                LogUtil.d("autoLogin, onLoginFailure");
                if (errcode == CubeError.ERROR_CODE_NO_NETWORK) {
                    LiveListActivity.this.dismissNetErrorDialog();
                }
            }
        });

    }

    private class JumpTask implements Runnable {
        @Override
        public void run() {
            if (mLoginSucc) {
                jump(TabMainActivity.class, true, new Bundle());
            } else {
                if (mLacth.getCount() > 0) {//正在登录中
                    laterJump();
                } else {//登陆动作已经完成，但是登录失败了，也有可能是还没激活
                    Bundle bundle = new Bundle();
                    bundle.putString(Contants.BundleKey.MOBILE, SpUtil.getSp().read(SpUtil.SpKey.LAST_LOGIN_NAME, ""));
                    jump(LoginActivity.class, true, bundle);

                }
            }
        }
    }

    private void laterJump() {
        LiveListActivity.this.openLoadingDialog(null, false);
        new AsyncTask<Integer, Integer, Integer>() {
            @Override
            protected Integer doInBackground(Integer... params) {
                try {
                    mLacth.await(2, TimeUnit.SECONDS);
                } catch (Exception e) {
                    LogUtil.e(TAG, e);
                }
                return 0;
            }

            @Override
            protected void onPostExecute(Integer reult) {
                if (mLoginSucc) {
                    jump(TabMainActivity.class, true, new Bundle());
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString(Contants.BundleKey.MOBILE, SpUtil.getSp().read(SpUtil.SpKey.LAST_LOGIN_NAME, ""));
                    jump(LoginActivity.class, true, bundle);
                }
                LiveListActivity.this.dismissLoadingDialog();
            }
        }.execute(0);
    }

    protected boolean showTitleBar() {
        return false;
    }

}
