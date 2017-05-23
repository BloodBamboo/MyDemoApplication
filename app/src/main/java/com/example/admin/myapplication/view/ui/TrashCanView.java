package com.example.admin.myapplication.view.ui;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017/5/23.
 */

public class TrashCanView extends View {

    private int minWidth = 500;
    private int minHeight = 500;
    private Path mPath;
    private Paint mPaint;
    private float ratio = 0;

    public TrashCanView(Context context) {
        this(context, null);
    }

    public TrashCanView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setStrokeWidth(5);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.DKGRAY);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(minWidth, minHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(50 * ratio, -80 * ratio);
        canvas.rotate(10 * ratio);
        //画桶盖
        mPath.moveTo(50, 120);
        mPath.lineTo(450, 120);
        mPath.moveTo(200, 120);
        mPath.lineTo(225, 80);
        mPath.lineTo(275, 80);
        mPath.lineTo(300, 120);
        canvas.drawPath(mPath, mPaint);
        mPath.reset();
        canvas.restore();

        //画桶身
        mPath.moveTo(80, 120);
        mPath.lineTo(120, 450);
        mPath.lineTo(380, 450);
        mPath.lineTo(420, 120);

        //画桶条纹
        mPath.moveTo(120, 150);
        mPath.lineTo(150, 400);
        mPath.lineTo(170, 400);
        mPath.lineTo(140, 150);
        mPath.lineTo(120, 150);

        RectF rect = new RectF(240, 150, 260, 400);
        mPath.addRect(rect, Path.Direction.CCW);


        mPath.moveTo(380, 150);
        mPath.lineTo(350, 400);
        mPath.lineTo(330, 400);
        mPath.lineTo(360, 150);
        mPath.lineTo(380, 150);

        canvas.drawPath(mPath, mPaint);
        mPath.reset();
    }

    public void setRatio(float ratio) {
        if (ratio > 1) {
            ratio = 1;
        } else if (ratio < 0){
            ratio = 0;
        }
        this.ratio = ratio;
        invalidate();
    }

    public void startAnim() {
        ObjectAnimator o = ObjectAnimator.ofFloat(this, "ratio", 0, 1);
        o.setDuration(1000);
        o.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ObjectAnimator o = ObjectAnimator.ofFloat(TrashCanView.this, "ratio", 1, 0);
                o.setDuration(1000);
                o.start();
            }
        });
        o.start();
    }
}
