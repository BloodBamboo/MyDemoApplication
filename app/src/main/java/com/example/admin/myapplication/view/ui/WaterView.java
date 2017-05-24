package com.example.admin.myapplication.view.ui;


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
    private Path mPath;
    private Paint mPaint;
    private int colorDark;
    private int colorLight;
    private int radiusDark;
    private int radiusLight;
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
        radiusLight = (int) typedArray.getDimension(R.styleable.WaterView_radiusLight, 50);
        percent = typedArray.getFloat(R.styleable.WaterView_percent, 0.5f);
        typedArray.recycle();
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

        float showHeight = height - height * percent;
        float startDot = 0;
        canvas.translate(-x, 0);

        mPath.reset();
        mPaint.setColor(colorDark);
        mPath.moveTo(startDot, showHeight);
        mPath.quadTo(width * 0.25f, showHeight - radiusDark, width * 0.5f, showHeight);
        mPath.quadTo(width * 0.75f, showHeight + radiusDark, width, showHeight);
        mPath.quadTo(width * 1.25f, showHeight - radiusDark, width * 1.5f, showHeight);
        mPath.quadTo(width * 1.75f, showHeight + radiusDark, width * 2, showHeight);
//        mPath.quadTo(width * 2.25f, showHeight - radiusDark, width * 2.5f, showHeight);
//        mPath.quadTo(width * 2.75f, showHeight + radiusDark, width * 3, showHeight);
        mPath.lineTo(width * 2, height);
        mPath.lineTo(startDot, height);
        mPath.lineTo(startDot, showHeight);

        canvas.drawPath(mPath, mPaint);
    }

    public void setX() {
        if (percent > 1) {
            percent = 0.1f;
        } else {
            percent += 0.001f;
        }


        if (x > (getWidth() - 10)) {
            x = 0;
        } else {
            x += 10;
        }
        invalidate();
    }
}
