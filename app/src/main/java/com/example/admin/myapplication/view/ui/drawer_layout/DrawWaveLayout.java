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
    private DrawWaveFrBgLayout frBgLayout;
    private float slideOffset;
    private float y;

    public DrawWaveLayout(Context context) {
        this(context, null);
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
        frBgLayout = new DrawWaveFrBgLayout(getContext(), slideBarLayout);
        addView(frBgLayout, 1);
        addDrawerListener(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        y = ev.getY();
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            closeDrawers();
            slideBarLayout.onMotionUp();
            return super.dispatchTouchEvent(ev);
        }

        //没有打开之前 不拦截     打开之后拦不拦截  大于1  后  内容区域不再进行偏移
        if (slideOffset < 1) {
            return super.dispatchTouchEvent(ev);
        }else {
            //等于  1
            frBgLayout.setTouchY(y,slideOffset);
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        this.slideOffset = slideOffset;
        frBgLayout.setTouchY(y, slideOffset);

        //针对内容区域进行破偏移
        float contentViewoffset = drawerView.getWidth() * slideOffset / 2;
        content.setTranslationX(contentViewoffset);
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
