package com.example.admin.myapplication.view.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import com.example.admin.myapplication.R;

/**
 * Created by Administrator on 2017/6/26.
 */

public class SplashView extends View {
    private SplashState mState = null;

    //策略模式:State---三种动画状态
    private abstract class SplashState {
        public abstract void drawState(Canvas canvas);

        public abstract void cancel();
    }

    // 大圆(里面包含很多小圆的)的半径
    private float mRotationRadius = 90;
    // 每一个小圆的半径
    private float mCircleRadius = 20;
    //当前大圆旋转角度
    private float mCurrentRotationAngle = 0;
    // 小圆圈的颜色列表，在initialize方法里面初始化
    private int[] mCircleColors;
    // 大圆和小圆旋转的时间
    private long mRotationDuration = 1200; //ms
    // 第二部分动画的执行总时间(包括二个动画时间，各占1/2)
    private long mSplashDuration = 2400; //ms
    // 整体的背景颜色
    private int mSplashBgColor = Color.WHITE;
    // 绘制圆的画笔
    private Paint mPaint = new Paint();

    // 屏幕正中心点坐标
    private float mCenterX;
    private float mCenterY;
    //屏幕对角线一半
    private float mDiagonalDist;

    /**
     * 参数，保存了一些绘制状态，会被动态地改变*
     */
    //空心圆初始半径
    private float mHoleRadius = 0F;
    //当前大圆的半径
    private float mCurrentRotationRadius = mRotationRadius;
    // 绘制背景的画笔
    private Paint mPaintBackground = new Paint();

    public SplashView(Context context) {
        this(context, null);
    }

    public SplashView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mCircleColors = getContext().getResources().getIntArray(R.array.splash_circle_colors);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        mPaintBackground = new Paint();
        mPaintBackground.setStyle(Paint.Style.STROKE);
        mPaintBackground.setAntiAlias(true);
        mPaintBackground.setColor(mSplashBgColor);
        if (mState == null) {
            mState = new RotateState();
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mState.drawState(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mCenterX = getMeasuredWidth() / 2;
        mCenterY = getMeasuredHeight() / 2;
        mDiagonalDist = (float) Math.sqrt((getMeasuredWidth() * getMeasuredWidth() + getMeasuredHeight() * getMeasuredHeight())) / 2f;//勾股定律
    }

    private void drawCircles(Canvas canvas) {
        //每个小圆之间的间隔角度 = 2π/小圆的个数
        float rotationAngle = (float) (2 * Math.PI / mCircleColors.length);
        for (int i = 0, count = mCircleColors.length; i < count; i++) {
            float angle = rotationAngle * i + mCurrentRotationAngle;
            float cx = (float) (Math.sin(angle) * mCurrentRotationRadius + mCenterX);
            float cy = (float) (Math.cos(angle) * mCurrentRotationRadius + mCenterY);
            mPaint.setColor(mCircleColors[i]);
            canvas.drawCircle(cx, cy, mCircleRadius, mPaint);
        }
    }

    private void drawBackGround(Canvas canvas) {
        if (mHoleRadius > 0) {
            //得到画笔的宽度 = 对角线/2 - 空心圆的半径
            float strokeWidth = mDiagonalDist - mHoleRadius;
            mPaintBackground.setStrokeWidth(strokeWidth);
            //画圆的半径 = 空心圆的半径 + 画笔的宽度/2
            float radius = mHoleRadius + strokeWidth / 2;
            canvas.drawCircle(mCenterX, mCenterY, radius, mPaintBackground);
        } else {
            canvas.drawColor(mSplashBgColor);
        }
    }

    public void splashDisappear() {
        if (mState != null && mState instanceof RotateState) {
            mState.cancel();
            mState = new MergingState();
        }
    }

    /**
     * 1.旋转动画
     * 控制各个小圆的坐标---控制小圆的角度变化----属性动画ValueAnimator
     */
    private class RotateState extends SplashState {
        private ValueAnimator mAnimator;

        public RotateState() {
            mAnimator = ValueAnimator.ofFloat(0f, (float) Math.PI * 2);
            mAnimator.setDuration(mRotationDuration);
            mAnimator.addUpdateListener((ValueAnimator animation) -> {
                mCurrentRotationAngle = (float) animation.getAnimatedValue();
                invalidate();
            });
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.setRepeatCount(Animation.INFINITE);
            mAnimator.start();
        }

        @Override
        public void drawState(Canvas canvas) {
            drawBackGround(canvas);
            drawCircles(canvas);
        }

        @Override
        public void cancel() {
            mAnimator.cancel();
        }
    }

    /**
     * 2.聚合动画
     * 要素：大圆的半径不断地变大--变小----》小圆的坐标
     */
    private class MergingState extends SplashState {
        private ValueAnimator mAnimator;

        public MergingState() {
            mAnimator = ValueAnimator.ofFloat(mRotationRadius, 0);
            mAnimator.setDuration(mSplashDuration / 2);
            mAnimator.addUpdateListener((ValueAnimator animation) -> {
                mCurrentRotationRadius = (float) animation.getAnimatedValue();
                invalidate();
            });
            mAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mState = new ExpandState();
                }
            });
            mAnimator.setInterpolator(new OvershootInterpolator(10));
            mAnimator.start();
        }

        @Override
        public void drawState(Canvas canvas) {
            drawBackGround(canvas);
            drawCircles(canvas);
        }

        @Override
        public void cancel() {
            mAnimator.cancel();
        }
    }

    /**
     * 3.水波纹扩散动画
     * 画一个空心圆----画一个圆，让它的画笔的粗细变成很大---不断地减小画笔的粗细。
     * 空心圆变化的范围：0~ 对角线/2
     */

    private class ExpandState extends SplashState {
        private ValueAnimator mAnimator;

        public ExpandState() {
            mAnimator = ValueAnimator.ofFloat(mCircleRadius, mDiagonalDist);
            mAnimator.setDuration(mSplashDuration / 2);
            mAnimator.addUpdateListener((ValueAnimator animation) -> {
                mHoleRadius = (float) animation.getAnimatedValue();
                invalidate();
            });
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.start();
        }

        @Override
        public void drawState(Canvas canvas) {
            drawBackGround(canvas);
        }

        @Override
        public void cancel() {
            mAnimator.cancel();
        }
    }
}
