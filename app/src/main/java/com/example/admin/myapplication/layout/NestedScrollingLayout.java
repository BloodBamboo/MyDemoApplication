package com.example.admin.myapplication.layout;

import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2016/12/16.
 */

public class NestedScrollingLayout extends LinearLayout implements NestedScrollingParent {

    public NestedScrollingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public NestedScrollingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public NestedScrollingLayout(Context context) {
        super(context);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed)
    {
//        boolean hiddenTop = dy > 0 && getScrollY() < mTopViewHeight;
//        boolean showTop = dy < 0 && getScrollY() > 0 && !ViewCompat.canScrollVertically(target, -1);
//
//        if (hiddenTop || showTop)
//        {
//            scrollBy(0, dy);
//            consumed[1] = dy;
//        }
    }


}
