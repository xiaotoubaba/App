package com.water.roll;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jovision.xunwei.junior.lib.view.TitleBar;
import com.water.roll.request.Request;

import in.srain.cube.request.RequestAble;

public abstract class BaseFragment extends Fragment implements RequestAble {

    protected BaseActivity mActivity;

    protected TitleBar mTitleBar;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (BaseActivity)this.getActivity();
        if(showTitleBar()){
            LinearLayout rootView = new LinearLayout(mActivity);
            rootView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            rootView.setOrientation(LinearLayout.VERTICAL);
            // 添加titlebar
            mTitleBar = new TitleBar(mActivity);
            mTitleBar.setBackgroundResource(BaseApplication.getAppSettings().getMainColorResId());
            rootView.addView(mTitleBar);
            mTitleBar.setVisibility(View.GONE);
            View bodyView = createView(inflater, container, savedInstanceState);
            rootView.addView(bodyView);
            return rootView;
        }else{
            return createView(inflater, container, savedInstanceState);
        }
    }

    public void onDestroyView(){
        super.onDestroyView();
        Request.cancel(this);
    }

    protected void hideTitleBar() {
        mTitleBar.setVisibility(View.GONE);
    }

    protected boolean showTitleBar() {
        return true;
    }

    /**
     * 子类不要重写onCreateView，而是重写createView
     * */
    protected abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);



}
