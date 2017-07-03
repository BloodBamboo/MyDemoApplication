package com.example.admin.myapplication.view.ui.drawer_layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.example.admin.myapplication.R;

/**
 * Created by Administrator on 2017/7/3.
 */

public class DrawSlideBarLayout extends LinearLayout{

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
        init();
    }

    private void init() {

    }
}
