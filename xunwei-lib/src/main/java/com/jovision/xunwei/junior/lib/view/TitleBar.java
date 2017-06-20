package com.jovision.xunwei.junior.lib.view;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jovision.xunwei.junior.lib.R;

/**
 * Created by Administrator on 2016/1/22.
 */
public class TitleBar extends FrameLayout {

    private TextView mTitle;
    private TextView mLeft;
    private TextView mRight;

    public TitleBar(Context context) {
        this(context,null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    private void initViews(Context context){
        View rootView = LayoutInflater.from(context).inflate(R.layout.title_bar, this, true);
        mTitle = (TextView)rootView.findViewById(R.id.title_bar_title);
        mLeft = (TextView)rootView.findViewById(R.id.title_bar_left);
        mRight = (TextView)rootView.findViewById(R.id.title_bar_right);
    }


    @Override
    public void onAttachedToWindow(){
        super.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public TextView getRightView() {
        return mRight;
    }

    public TitleBar setTitle(int resId){
        String res = getContext().getResources().getString(resId);
        return setTitle(res);
    }
    
    public TitleBar setTitle(String title){
        mTitle.setText(title);
        return this;
    }
    
    public TitleBar setLeftText(int resId, OnClickListener listener){
    	mLeft.setText(resId);
    	if(listener != null){
    		mLeft.setOnClickListener(listener);
    	}
    	return this;
    }
    
    public TitleBar setRightText(int resId, OnClickListener listener){
    	mRight.setText(resId);
    	if(listener != null){
    		mRight.setOnClickListener(listener);
    	}
    	return this;
    }
    public TitleBar setRightText(String res, OnClickListener listener){
        mRight.setText(res);
        if(listener != null){
            mRight.setOnClickListener(listener);
        }
        return this;
    }
    
    public TitleBar setLeftImg(int resId, OnClickListener listener){
    	Drawable drawable = getContext().getResources().getDrawable(resId);
    	drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
    	mLeft.setCompoundDrawables(drawable, null, null, null);
    	if(listener != null){
    		mLeft.setOnClickListener(listener);
    	}
    	return this;
    }
    
    public TitleBar setRightImg(int resId, OnClickListener listener){
    	Drawable drawable = getContext().getResources().getDrawable(resId);
    	drawable.setBounds(0, 0, 60, 60);
    	mRight.setCompoundDrawables(null, null, drawable,null);
    	if(listener != null){
    		mRight.setOnClickListener(listener);
    	}
    	return this;
    }
}
