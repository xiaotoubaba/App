package com.jovision.xunwei.junior.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.jovision.xunwei.junior.lib.R;

/**
 * 参考： （1）http://blog.csdn.net/qibin0506/article/details/42046559 <br/>
 * （2）http://blog.csdn.net/lmj623565791/article/details/42160391 <br/>
 * */
public class Indicator extends LinearLayout {

	private Paint mPaint; // 画指示符的paint

	private int mTop; // 指示符的top
	private int mLeft; // 指示符的left
	private int mWidth; // 指示符的width
	private int mHeight = 5; // 指示符的高度，固定了
	private int mColor; // 指示符的颜色
	private View mView;
	private View[] mTabs;

	private IndicatorClickListener mListener;

	public interface IndicatorClickListener {

		public void onItemClick(int itemid);
		public void onItemSelected(View[] tabs, View tab, int itemid);
	}

	/**
	 * tab数量
	 */
	private int mTabVisibleCount;
	/**
	 * 与之绑定的ViewPager
	 */
	public ViewPager mViewPager;

	public Indicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 获取自定义属性 指示符的颜色
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Indicator, 0, 0);
		mColor = ta.getColor(R.styleable.Indicator_indicator_color, 0XFF0000);
		ta.recycle();
		//mColor = Color.parseColor("#00ad80");
		// 初始化paint
		mPaint = new Paint();
		mPaint.setColor(mColor);
		mPaint.setAntiAlias(true);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		int cCount = getChildCount();
		if (cCount == 0) {
			return;
		}
		mTabs = new View[cCount];
		this.mTabVisibleCount = 2;
		this.mWidth = getScreenWidth() / mTabVisibleCount;
		for (int i = 0; i < cCount; i++) {
			View view = getChildAt(i);
			mTabs[i] = view;
			LayoutParams lp = (LayoutParams) view
					.getLayoutParams();
			lp.weight = 0;
			lp.width = mWidth;
			view.setLayoutParams(lp);
		}
		// 设置点击事件
		setItemClickEvent();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mTop = getMeasuredHeight(); // 测量的高度即指示符的顶部位置
		int width = getMeasuredWidth(); // 获取测量的总宽度
		int height = mTop + mHeight; // 重新定义一下测量的高度
		setMeasuredDimension(width, height);
	}

	/**
	 * 指示符滚动
	 * 
	 * @param position
	 *            现在的位置
	 * @param offset
	 *            偏移量 0 ~ 1
	 */
	public void scroll(int position, float offset) {
		mLeft = (int) ((position + offset) * mWidth);
		// 容器滚动，当移动到倒数最后一个的时候，开始滚动
		if (offset > 0 && getChildCount() > mTabVisibleCount
				&& position >= (mTabVisibleCount - 2)) {
			if (mTabVisibleCount == 1) {// 为count为1时 的特殊处理
				this.scrollTo(position * mWidth + (int) (mWidth * offset), 0);
			} else {
				if (position < getChildCount() - 2) {
					this.scrollTo((position - (mTabVisibleCount - 2)) * mWidth
							+ (int) (mWidth * offset), 0);
				}
			}
		}
		invalidate();
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		// 圈出一个矩形
		Rect rect = new Rect(mLeft, mTop, mLeft + mWidth, mTop + mHeight);
		canvas.drawRect(rect, mPaint); // 绘制该矩形
		super.dispatchDraw(canvas);
	}

	public void setClickListener(IndicatorClickListener listener) {
		mListener = listener;
	}

	public void setVisibleItemCount(int count) {
		this.mTabVisibleCount = count;
		this.mWidth = getScreenWidth() / mTabVisibleCount;
	}

	// 设置关联的ViewPager
	public void setViewPager(final ViewPager viewPager, final int pos) {
		this.mViewPager = viewPager;
		this.mViewPager.post(new Runnable() {
			public void run() {
				if (viewPager != null) {
					viewPager.setCurrentItem(pos, true);
				}
			}
		});
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				if(mListener != null){
					mListener.onItemSelected(mTabs,getChildAt(position), position);
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				scroll(position, positionOffset);// 滚动
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
	}

	private void setItemClickEvent() {
		int cCount = getChildCount();
		for (int i = 0; i < cCount; i++) {
			final int j = i;
			View view = getChildAt(i);
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mViewPager.setCurrentItem(j);
					if(mListener != null)
						mListener.onItemClick(j);
				}
			});
		}
	}

	private int getScreenWidth() {
		WindowManager wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.widthPixels;
	}
}