package com.example.admin.myapplication.view.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by yangxin on 2017/5/14.
 */

public class GuaGuaVIew extends View {

    private float mx, my;
    private Paint mPaint;
    private int mBgColor;
    private int mTextColor;
    private Bitmap BmpDST,BmpSRC;
    private Path mPath;
    private String content = "谢谢参与!";

    public GuaGuaVIew(Context context) {
        this(context, null);
    }

    public GuaGuaVIew(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mBgColor = Color.YELLOW;
        mTextColor = Color.RED;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mTextColor);
        mPath = new Path();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mx = event.getX();
                my = event.getY();
                mPath.moveTo(event.getX(),event.getY());
                return true;
            case MotionEvent.ACTION_MOVE:
                float endX = (mx+event.getX())/2;
                float endY = (my+event.getY())/2;
                mPath.quadTo(mx,my,endX,endY);
                mx = event.getX();
                my =event.getY();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        postInvalidate();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        BmpSRC = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        BmpSRC.eraseColor(mBgColor);
        BmpDST = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setTextSize(60);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(0);
        float width = mPaint.measureText(content);
        canvas.drawText(content, (getWidth() - width) / 2 , getHeight() / 2, mPaint);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(50);

        int layerId = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);

        Canvas c = new Canvas(BmpDST);
        c.drawPath(mPath, mPaint);

        canvas.drawBitmap(BmpDST,0,0,mPaint);

        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        canvas.drawBitmap(BmpSRC, 0, 0, mPaint);

        mPaint.setXfermode(null);
        canvas.restoreToCount(layerId);
    }
}
