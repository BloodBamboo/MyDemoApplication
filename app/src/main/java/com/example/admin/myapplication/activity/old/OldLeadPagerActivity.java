package com.example.admin.myapplication.activity.old;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;


import com.example.admin.myapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/14.
 */
public class OldLeadPagerActivity extends AppCompatActivity {

    @BindView(R.id.line1)
    LinearLayout line1;
    @BindView(R.id.frame_layout)
    FrameLayout frame_layout;
    @BindView(R.id.h_scroll_view)
    HorizontalScrollView h_scroll_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_pager);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button1)
    public void onclickMove() {
        if (line1.getScrollX() >= frame_layout.getWidth() * 2) {
            ValueAnimator animator = ValueAnimator.ofInt(line1.getScrollX(),0);
            animator.setDuration(500)
                    .addUpdateListener((animation) -> {
                            line1.scrollTo((int)animation.getAnimatedValue(), 0);
                            h_scroll_view.scrollTo((int)animation.getAnimatedValue(), 0);
                    });
            animator.start();
        } else {
            ValueAnimator animator = ValueAnimator.ofInt(line1.getScrollX(),line1.getScrollX() + frame_layout.getWidth());
            animator.setDuration(1000)
                    .addUpdateListener((animation) -> {
                            line1.scrollTo((int)animation.getAnimatedValue(), 0);
                            h_scroll_view.scrollTo((int)animation.getAnimatedValue(), 0);
                    });
            animator.start();
        }
    }
}
