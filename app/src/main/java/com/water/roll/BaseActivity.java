package com.water.roll;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.jovision.xunwei.junior.lib.view.TitleBar;
import com.water.roll.request.CubeRequest;
import com.water.roll.request.Request;
import com.water.roll.util.Contants;
import com.water.roll.util.DialogManager;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import in.srain.cube.request.RequestAble;


public class BaseActivity extends FragmentActivity implements RequestAble {

    private TitleBar mTitleBar;
    private DialogManager mDialogManager;
    public SupperHandler mSuperHandler = new SupperHandler(this);
    public static Map<String, Bitmap> map = new HashMap<String, Bitmap>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.black));

        // 添加到栈
        ActivityTaskManager.getInstance().putActivity(this.getClass().getSimpleName(), this);
        mDialogManager = new DialogManager(this);
    }

    @Override
    protected void onDestroy() {
        Request.cancel(this);
        mSuperHandler.removeCallbacksAndMessages(null);
        mDialogManager.destroy();
        // 从栈删除
        ActivityTaskManager.getInstance().removeActivity(this.getClass().getSimpleName());
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void setContentView(int resId) {
        if (showTitleBar()) {
            LinearLayout container = new LinearLayout(this);
            container.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            container.setOrientation(LinearLayout.VERTICAL);
            // 添加titlebar
            mTitleBar = new TitleBar(this);
            mTitleBar.setBackgroundResource(BaseApplication.getAppSettings().getMainColorResId());
            container.addView(mTitleBar);
            View rootView = LayoutInflater.from(this).inflate(resId, container, false);
            container.addView(rootView);
            setContentView(container);
        } else {
            super.setContentView(resId);
        }
    }

    public TitleBar getTitleBar() {
        return this.mTitleBar;
    }

    @SuppressWarnings("unchecked")
    public <T> T $(int id) {
        return (T) this.findViewById(id);
    }

    protected boolean showTitleBar() {
        return true;
    }

    public static class SupperHandler extends Handler {

        private WeakReference<BaseActivity> mActivity;

        public SupperHandler(BaseActivity activity) {
            mActivity = new WeakReference<BaseActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            BaseActivity activity = mActivity.get();
            if (activity == null) {
                return;
            }
            activity.onSuperHandler(msg.what, msg.arg1, msg.arg2, msg.obj);
        }
    }

    ;

    public void onSuperHandler(int what, int arg1, int arg2, Object obj) {
        //do nothing
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        hideKeyboard(view);
    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        }
    }

    public void showKeyboard() {
        View view = this.getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        showKeyboard(view);
    }

    public void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.showSoftInputFromInputMethod(view.getApplicationWindowToken(), 0);
        }
    }

    public <T extends Serializable> void jump(Class<? extends BaseActivity> clazz, boolean finishMyself, T param) {
        Bundle bundle = null;
        if (param != null) {
            bundle = new Bundle();
            bundle.putSerializable(Contants.BundleKey.PARAM, param);
        }
        jump(clazz, finishMyself, bundle);
    }

    public void jump(Class<? extends BaseActivity> clazz, boolean finishMyself, Bundle bundle) {
        Intent i = new Intent(this, clazz);
        if (bundle != null) {
            i.putExtras(bundle);
        }
        //隐藏掉软键盘
        hideKeyboard();
        this.startActivity(i);
        if (finishMyself) {
            this.finish();
        }
    }


    public void openLoadingDialog(String msg, boolean cancel) {
        mDialogManager.openLoadingDialog(msg, cancel);
    }

    public void dismissLoadingDialog() {
        mDialogManager.dismissLoadingDialog();
    }

    public void openNetErrorDialog() {
        mDialogManager.openNetErrorDialog();
    }

    public void dismissNetErrorDialog() {
        mDialogManager.dismissNetErrorDialog();
    }

    public void openConfirmDialog(String tiltle, String msg, String positive,
                                  String negative, boolean cancel,
                                  final DialogManager.OnConfirmDialogClickListener listener) {
        mDialogManager.openConfirmDialog(tiltle, msg, positive, negative,
                cancel, listener);
    }

    public void dismissConfirmDialog() {
        mDialogManager.dismissConfirmDialog();
    }

    public void openRetryDialog(final RequestAble requestAble,
                                final CubeRequest request, String msg, final boolean cancel) {
        mDialogManager.openRetryDialog(requestAble, request, msg, cancel);
    }

    public void dismissRetryDialog() {
        mDialogManager.dismissRetryDialog();
    }

    public void openETDialog(String tiltle, String msg, String positive,
                             String negative, boolean cancel,
                             final DialogManager.OnETConfirmDialogClickListener listener) {
        mDialogManager.openETDialog(tiltle, msg, positive, negative,
                cancel, listener);
    }

    public void dismissETDialog() {
        mDialogManager.dismissETDialog();
    }

}
