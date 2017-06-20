package com.jovision.xunwei.junior.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.GridView;

import com.jovision.xunwei.junior.lib.R;

/**
 * Created by zhangzz on 15/11/20.
 */
public class DividerGridView extends GridView {

    private int mDividerColor;
    public DividerGridView(Context context) {
        super(context);
    }

    public DividerGridView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DividerGridView);

        mDividerColor = typedArray.getColor(R.styleable.DividerGridView_dividerLineColor, getResources().getColor(R.color.transparent));
    }

    public DividerGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

//    /**
//     * 设置上下不滚动
//     */
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if(ev.getAction() == MotionEvent.ACTION_MOVE){
//            return true;//true:禁止滚动
//        }
//
//        return super.dispatchTouchEvent(ev);
//    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
//        int column = getNumColumns();
//        int childCount = getChildCount();
//        int lines = childCount / column;
//        int lines2 = childCount % column;
//        if(lines2 > 0){
//            lines = lines + 1;
//        }
//
//        Paint localPaint;
//        localPaint = new Paint();
//        localPaint.setStyle(Paint.Style.STROKE);
//        localPaint.setColor(mDividerColor);
//        for (int i = 0; i < childCount; i++) {
//            View cellView = getChildAt(i);
//            if(i / column == 0 ){
//                canvas.drawLine(cellView.getLeft(), cellView.getTop(), cellView.getRight(), cellView.getTop(), localPaint);
//            }
//            if(i / column == lines - 1){
//                canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), localPaint);
//            }
//            if ((i + 1) % column == 0) {
//                if(((i/column) + 1) < lines){
//                    canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), localPaint);
//                }
//            } else if ((i + 1) > (childCount - (childCount % column))) {
//                canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(), cellView.getBottom(), localPaint);
//            } else {
//                canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(), cellView.getBottom(), localPaint);
//
//                if(((i/column) + 1) < lines){
//                    canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), localPaint);
//                }
//            }
//        }

        //是否需要补足最后的方格
//        if (childCount % column != 0) {
//            for (int j = 0; j < (column - childCount % column); j++) {
//                View lastView = getChildAt(childCount - 1);
//                canvas.drawLine(lastView.getRight() + lastView.getWidth() * j, lastView.getTop(), lastView.getRight() + lastView.getWidth() * j, lastView.getBottom(), localPaint);
//            }
//        }
    }
}
