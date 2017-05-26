package com.example.admin.myapplication.view.ui;


import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.admin.myapplication.R;


/**
 * Created by Administrator on 2017/5/23.
 */

public class WaterView extends View {
    private int INT_WAVE_LENGTH = 0;
    private Path mPath;
    private Paint mPaint;
    private int colorDark;
    private int colorLight;
    private int radiusDark;
    private float percent;
    private int x;


    public WaterView(Context context) {
        this(context, null);
    }

    public WaterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaterView);
        colorDark = typedArray.getColor(R.styleable.WaterView_colorDark, Color.BLUE);
        colorLight = typedArray.getColor(R.styleable.WaterView_colorLight, Color.BLUE);
        radiusDark = (int) typedArray.getDimension(R.styleable.WaterView_radiusDark, 70);
        percent = typedArray.getFloat(R.styleable.WaterView_percent, 0.5f);
        typedArray.recycle();
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        INT_WAVE_LENGTH = getWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

        float showHeight = height - height * percent;
        float half_wave_width = INT_WAVE_LENGTH / 2;
        float startDot = -INT_WAVE_LENGTH + x;

        mPath.reset();
        mPaint.setColor(colorDark);
        mPath.moveTo(startDot, showHeight);
        for (float i = -INT_WAVE_LENGTH; i < getWidth() + INT_WAVE_LENGTH; i += INT_WAVE_LENGTH) {
            mPath.rQuadTo(half_wave_width / 2f, -radiusDark, half_wave_width, 0);
            mPath.rQuadTo(half_wave_width / 2f, radiusDark, half_wave_width, 0);
        }

        mPath.lineTo(getWidth(), height);
        mPath.lineTo(startDot, height);
        mPath.lineTo(startDot, showHeight);
        mPath.close();
        canvas.drawPath(mPath, mPaint);

//        mPath.reset();
//        mPaint.setColor(Color.GREEN);
//        mPath.moveTo(startDot - half_wave_width, showHeight);
//        for (float i = -INT_WAVE_LENGTH; i < getWidth() + INT_WAVE_LENGTH; i += INT_WAVE_LENGTH) {
//            mPath.rQuadTo(half_wave_width / 2f, -radiusDark, half_wave_width, 0);
//            mPath.rQuadTo(half_wave_width / 2f, radiusDark, half_wave_width, 0);
//        }
//
//        mPath.lineTo(getWidth(), height);
//        mPath.lineTo(startDot, height);
//        mPath.lineTo(startDot, showHeight);
//        mPath.close();
//        canvas.drawPath(mPath, mPaint);
    }

    public void start() {
//        if (percent > 1) {
//            percent = 0.1f;
//        } else {
//            percent += 0.001f;
//        }
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, INT_WAVE_LENGTH);
        valueAnimator.addUpdateListener((ValueAnimator animation) -> {
            x = (int) animation.getAnimatedValue();
            invalidate();
        });
        valueAnimator.setDuration(3500);
        valueAnimator.setInterpolator(null);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.start();
    }
}
