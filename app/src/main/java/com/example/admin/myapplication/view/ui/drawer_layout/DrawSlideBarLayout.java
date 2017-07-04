package com.example.admin.myapplication.view.ui.drawer_layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.admin.myapplication.R;

/**
 * Created by Administrator on 2017/7/3.
 */

public class DrawSlideBarLayout extends LinearLayout {
    private boolean opened = false;
    private float maxTranslationX;

    public DrawSlideBarLayout(Context context) {
        this(context, null);
    }

    public DrawSlideBarLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) {
            TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.SideBar);
            maxTranslationX = t.getDimension(R.styleable.SideBar_maxTranslationX, 0);
            t.recycle();
        }
    }

    public void setTouchY(float y, float slideOffset) {
        //遍历全部子控件  给每一个子控件进行偏移
        //如果slideOffset =1   侧滑菜单全部出来了
        opened = slideOffset == 1;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.setPressed(false);
            //要判断  y坐落在哪一个子控件    松手的那一刻  进行回调  跳转其他页面
            if (opened && y > child.getTop() && y < child.getBottom()) {
                child.setPressed(true);
            }

            //偏移方法
            apply(getParent(), child, y, slideOffset);
        }
    }

    private void apply(ViewParent parent, View chlid, float y, float slideOffset) {
        float translationX = 0;

        int centerY = chlid.getTop() + chlid.getHeight() / 2;

        //控件中心点 局手指的距离
        float distance = Math.abs(y - centerY);
        float scale = distance / getHeight() * 3;//3   放大系数
        translationX = maxTranslationX - scale * maxTranslationX;

        chlid.setTranslationX(translationX < 0 ? 0 : translationX);
    }

    /**
     * 手指 弹起来
     */
    public void onMotionUp() {
        for (int i = 0; opened && i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view.isPressed()) {
                view.performClick();
                Toast.makeText(getContext(), "view : " + i, Toast.LENGTH_SHORT).show();
                //回调操作
            }
        }
    }
}
