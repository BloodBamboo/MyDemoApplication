package com.example.admin.myapplication.view.ui.drawer_layout;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2017/7/3.
 */

public class DrawWaveFrBgLayout extends FrameLayout {
    private DrawSlideBarLayout slideBarLayout;
    private DrawWaveBgView bgView;

    public DrawWaveFrBgLayout(Context context, DrawSlideBarLayout slideBarLayout) {
        super(context);
        init(slideBarLayout);
    }

    private void init(DrawSlideBarLayout slideBarLayout) {
        this.slideBarLayout = slideBarLayout;
        setLayoutParams(slideBarLayout.getLayoutParams());
        bgView = new DrawWaveBgView(getContext());

        bgView.setColor(slideBarLayout.getBackground());
        slideBarLayout.setBackgroundColor(Color.TRANSPARENT);

        addView(bgView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(slideBarLayout, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void setTouchY(float y, float slideOffset) {
        slideBarLayout.setTouchY(y, slideOffset);
        bgView.setTouchY(y, slideOffset);
    }
}
