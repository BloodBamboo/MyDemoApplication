package com.example.admin.myapplication.view.ui;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.admin.myapplication.R;

/**
 * Created by admin on 2017/5/18.
 */

public class ColorView extends View {
    private Bitmap bitmap1, bitmap2, bitmap3, bitmap4, bitmap5, bitmap6, bitmap7;
    private Paint paint;
    private int width;
    private int height;
    private int x = 0, offx = 0;

    public ColorView(Context context) {
        this(context, null);
    }

    public ColorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        bitmap1 = BitmapFactory.decodeResource(getResources(), R.mipmap.avft_active);
        bitmap2 = BitmapFactory.decodeResource(getResources(), R.mipmap.box_stack_active);
        bitmap3 = BitmapFactory.decodeResource(getResources(), R.mipmap.bubble_frame_active);
        bitmap4 = BitmapFactory.decodeResource(getResources(), R.mipmap.bubbles_active);
        bitmap5 = BitmapFactory.decodeResource(getResources(), R.mipmap.circle_filled_active);
        bitmap6 = BitmapFactory.decodeResource(getResources(), R.mipmap.circle_outline_active);
        bitmap7 = BitmapFactory.decodeResource(getResources(), R.mipmap.bullseye_active);
        width = bitmap1.getWidth();
        height = bitmap1.getHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width * 7, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.clipRect(x, 0, width + x, height);
        RectF rectF = new RectF(0, 0, width, height);
        canvas.drawBitmap(bitmap1, null, rectF, paint);
        rectF = new RectF(width, 0, width * 2, height);
        canvas.drawBitmap(bitmap2, null, rectF, paint);
        rectF = new RectF(width * 2, 0, width * 3, height);
        canvas.drawBitmap(bitmap3, null, rectF, paint);
        rectF = new RectF(width * 3, 0, width * 4, height);
        canvas.drawBitmap(bitmap4, null, rectF, paint);
        rectF = new RectF(width * 4, 0, width * 5, height);
        canvas.drawBitmap(bitmap5, null, rectF, paint);
        rectF = new RectF(width * 5, 0, width * 6, height);
        canvas.drawBitmap(bitmap6, null, rectF, paint);
        rectF = new RectF(width * 6, 0, width * 7, height);
        canvas.drawBitmap(bitmap7, null, rectF, paint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() > x && event.getX() < width + x) {
                    offx = (int) (event.getX() - x);
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                x = (int) event.getX() - offx;
                if (x < 0) {
                    x = 0;
                }
                if (x > width * 6) {
                    x = width * 6;
                }
                break;
        }
        postInvalidate();
        return super.onTouchEvent(event);
    }
}
