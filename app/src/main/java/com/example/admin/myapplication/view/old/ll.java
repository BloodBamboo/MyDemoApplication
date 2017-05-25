package com.example.admin.myapplication.view.old;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/1/10.
 */

public class ll extends ViewGroup {

    public ll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ll(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获得它的父容器为它设置的测量模式和大小
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        View view1 = getChildAt(0);
        View view2 = getChildAt(1);

        MarginLayoutParams cParams = (MarginLayoutParams) view1.getLayoutParams();
        setMeasuredDimension(sizeWidth, view1.getMeasuredHeight() + view1.getPaddingTop() + view1.getPaddingBottom() +cParams.topMargin + cParams.bottomMargin);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        View view1 = getChildAt(0);
        View view2 = getChildAt(1);


        int childWidth = view1.getMeasuredWidth() + view1.getPaddingLeft() + view1.getPaddingRight();
        int childHeight = view1.getMeasuredHeight()  + view1.getPaddingTop() + view1.getPaddingBottom();

        int childWidth2 = view2.getMeasuredWidth()  + view2.getPaddingLeft() + view2.getPaddingRight();
        int childHeight2 = view2.getMeasuredHeight()  + view2.getPaddingTop() + view2.getPaddingBottom();

        int width = getMeasuredWidth();

        LayoutParams lp = view1.getLayoutParams();
        lp.width = width - childWidth2 - view1.getPaddingLeft() - view1.getPaddingRight();
        view1.setLayoutParams(lp);

        if ((childWidth + childWidth2) < width) {
            //计算childView的left,top,right,bottom
            view1.layout(view1.getPaddingLeft(),view1.getPaddingTop(), childWidth - view1.getPaddingRight(), view1.getPaddingTop() + childHeight + view1.getPaddingBottom());
            view2.layout(childWidth, 0, childWidth + childWidth2, childHeight2);
        } else {
            view2.layout(width - childWidth2, view1.getPaddingTop(), width, childHeight2);
            view1.layout(view1.getPaddingLeft(),view1.getPaddingTop(),width- childWidth2, childHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs)
    {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
