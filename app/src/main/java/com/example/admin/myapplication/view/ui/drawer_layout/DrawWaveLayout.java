package com.example.admin.myapplication.view.ui.drawer_layout;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2017/7/3.
 */

public class DrawWaveLayout extends DrawerLayout implements DrawerLayout.DrawerListener {
    private DrawSlideBarLayout slideBarLayout;
    private View content;
    private DrawWaveFrBgLayout reBgLayout;


    public DrawWaveLayout(Context context) {
        this(context,null);
    }

    public DrawWaveLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    private void init() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof DrawSlideBarLayout) {
                slideBarLayout = (DrawSlideBarLayout) view;
            } else {
                content = view;
            }
        }
        removeView(slideBarLayout);
        reBgLayout = new DrawWaveFrBgLayout(getContext(), slideBarLayout);
        addView(reBgLayout, 1);
        addDrawerListener(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:

                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {

    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}
