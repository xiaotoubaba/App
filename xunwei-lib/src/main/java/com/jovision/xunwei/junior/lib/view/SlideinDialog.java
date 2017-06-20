package com.jovision.xunwei.junior.lib.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.jovision.xunwei.junior.lib.R;

public class SlideinDialog extends Dialog{

	private Window mWindow = null;
	private int mWidth;
	private int mHeight;
	private int mGravity;
	
	public SlideinDialog(Context context) {
		super(context);
		this.mGravity = Gravity.LEFT| Gravity.BOTTOM;
	}

	public SlideinDialog(Context context,int theme, int gravity) {
		super(context, theme);
		this.mGravity = gravity;
	}

	public void windowDeploy(){
		mWindow = getWindow();
		if((mGravity&Gravity.LEFT) == Gravity.LEFT){
			mWindow.setWindowAnimations(R.style.AnimationSlideinFromLeft);
		}else if((mGravity&Gravity.RIGHT) == Gravity.RIGHT){
			mWindow.setWindowAnimations(R.style.AnimationSlideinFromRight);
		}
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        int width = getWidth();
        int height = getHeight();
        if(width > 0){
            lp.width = width;
        }
        if(height > 0){
        	lp.height = height;
        }
        lp.gravity = mGravity;
        mWindow.setAttributes(lp);
    }
	
	@Override
	public void show() {
		windowDeploy();
		super.show();
	}

	@Override
	public void hide() {
		super.hide();
	}

	public void destory(){
		mWindow = null;
	}

	public int getWidth() {
		return mWidth;
	}

	public void setWidth(int width) {
		this.mWidth = width;
	}

	public int getHeight() {
		return mHeight;
	}

	public void setHeight(int height) {
		this.mHeight = height;
	}
	
}
