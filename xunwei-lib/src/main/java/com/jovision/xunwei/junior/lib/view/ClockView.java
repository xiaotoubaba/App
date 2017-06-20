package com.jovision.xunwei.junior.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import com.jovision.xunwei.junior.lib.R;
import com.jovision.xunwei.junior.lib.util.LogUtil;

import java.util.Calendar;

public class ClockView extends View implements Runnable{
	
	private Drawable mDrawable;
	
	private int mWidth;
	private float mCenterX;

	private float mHourLength;
	private float mMinuteLength;
	private float mSecondLength;
	
	private Paint mPaint;
	
	private Handler handler = new Handler();
	
	
	public ClockView(Context context) {
		this(context, null);
	}
	
	public ClockView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public ClockView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}
	
	private void init(final Context context, final AttributeSet attrs){
		final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ClockView);
		mDrawable = array.getDrawable(R.styleable.ClockView_src);
		array.recycle();
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(getResources().getColor(R.color.white));
		handler.postDelayed(this, 1000);
	}
	
	@Override
	public void run() {
		// 重新绘制View
		invalidate();
		// 重新设置定时器，在60秒后调用run方法
		handler.postDelayed(this, 1000);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = getMeasuredWidth(); 
		int height = getMeasuredHeight();
		mWidth = Math.min(width, height);
		setMeasuredDimension(mWidth, mWidth);
		LogUtil.d("mWidth:"+mWidth);
		mHourLength = mWidth/5;
		mMinuteLength = mWidth/4;
		mSecondLength = mWidth/3;
		mCenterX = mWidth/2;
	}
	
	@Override
    protected void onDraw(Canvas canvas) {
		
		Calendar calendar = Calendar.getInstance();
		int currentMinute = calendar.get(Calendar.MINUTE);
		int currentHour = calendar.get(Calendar.HOUR);
		int currentSecond = calendar.get(Calendar.SECOND);
		
		// 计算分针和时间的角度
		double secondRadian = Math.toRadians((360 - ((currentSecond * 6) - 90)) % 360);
		double minuteRadian = Math.toRadians((360 - ((currentMinute * 6) - 90)) % 360);
		double hourRadian = Math.toRadians((360 - ((currentHour * 30) - 90))% 360 - (30 * currentMinute / 60));
		
        // 设置bounds，相当于缩放图片了
		mDrawable.setBounds(0, 0, mWidth, mWidth);
		// 1. 先把图绘制上
		mDrawable.draw(canvas);
        // 2. 表盘中心点画一个半径为5的实心圆圈 
		canvas.drawCircle(mWidth/2, mWidth/2, 5, mPaint);
		// 3.
		// 设置实针为4个象素粗
		mPaint.setStrokeWidth(4);
		// 在表盘上画时针
		canvas.drawLine(mCenterX, mCenterX,
				(int) (mCenterX + mHourLength * Math.cos(hourRadian)),
				(int) (mCenterX - mHourLength * Math.sin(hourRadian)), mPaint);
				
		// 设置分针为3个象素粗
		mPaint.setStrokeWidth(3);
		// 在表盘上画分针
		canvas.drawLine(mCenterX, mCenterX, 
				(int) (mCenterX + mMinuteLength* Math.cos(minuteRadian)), 
				(int) (mCenterX - mMinuteLength* Math.sin(minuteRadian)), 
				mPaint);
		
		// 设置分针为2个象素粗
		mPaint.setStrokeWidth(2);
		// 在表盘上画秒针
		canvas.drawLine(mCenterX, mCenterX, 
				(int) (mCenterX + mSecondLength* Math.cos(secondRadian)), 
				(int) (mCenterX - mSecondLength* Math.sin(secondRadian)), 
				mPaint);
	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		//  删除回调类
		handler.removeCallbacks(this);
	}
}
