package com.example.admin.myapplication.anime;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by admin on 2017/2/9.
 */

public class ListAnimationView extends View {
    public interface ListAnimationViewListener{
        void showContent();
        void downContent();
        void downContentAnimator();
    }


    public int MaxHeightRadian = 80;
    private int mArcHeight = 0;
    private Paint mPaint;
    private Path mPath;
    private int mstatus = -1;
    private final int NONE = -1;
    private final int UP = 0;
    private final int DOWN = 1;
    private final int up_time = 800;
    public static final int down_time = 700;
    private final int up_back_time = 600;

    private ListAnimationViewListener mLisetner;

    public ListAnimationView(Context context) {
        this(context, null);
    }

    public ListAnimationView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ListAnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPath = new Path();
    }

    public void show() {
        mstatus = UP;
        ValueAnimator animator = ValueAnimator.ofInt(0 , MaxHeightRadian);
        animator.setDuration(up_time);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mArcHeight = (int) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                upBackAnimation();
//                if (mLisetner != null) {
//                    mLisetner.showContent();
//                }
            }
        });

        animator.start();
    }

    private void upBackAnimation() {
        mstatus = DOWN;
        ValueAnimator animator = ValueAnimator.ofInt(MaxHeightRadian, 0);
        animator.setDuration(up_back_time);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mArcHeight = (int) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (mLisetner != null) {
                    mLisetner.showContent();
                }
            }
        });
        animator.start();
    }


    public void down() {
        mstatus = NONE;
//        ValueAnimator animator = ValueAnimator.ofInt(0 , getHeight());
//        animator.setDuration(down_time);
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                setTranslationY((int) valueAnimator.getAnimatedValue());
//            }
//        });
        //ofFloat与ofInt 对应的方法接受类型是什么就用哪一个， 如果用错了那就不会生效，与ValueAnimator不同
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "translationY",  0, getHeight());
        animator.setDuration(down_time);
        animator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (mLisetner != null) {
                    mLisetner.downContentAnimator();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (mLisetner != null) {
                    mLisetner.downContent();
                }
            }
        });
        animator.start();
    }

    public void setLisetner(ListAnimationViewListener lisetner) {
        this.mLisetner = lisetner;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int currentHeight = 0;

        switch (mstatus) {
            case NONE:
                currentHeight = 0;
                break;
            case UP:
                currentHeight = (int)(getHeight() * (1 - (float)mArcHeight / MaxHeightRadian) + MaxHeightRadian);
                break;
            case DOWN:
                currentHeight = MaxHeightRadian;
                break;
        }

        mPath.reset();
        mPath.moveTo(0, currentHeight);
        mPath.quadTo(getWidth() / 2, currentHeight - mArcHeight, getWidth(), currentHeight);
        mPath.lineTo(getWidth(), getHeight());
        mPath.lineTo(0, getHeight());
        mPath.close();

        canvas.drawPath(mPath, mPaint);
        super.onDraw(canvas);
    }
}
