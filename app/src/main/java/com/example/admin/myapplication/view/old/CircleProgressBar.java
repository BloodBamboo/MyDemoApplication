package com.example.admin.myapplication.view.old;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.afollestad.appthemeengine.Config;

/**
 * Created by Administrator on 2017/5/10.
 */

public class CircleProgressBar extends View {
    int progressColor;
    int textColor;
    int textSize;
    int progressWidth;
    float radius;
    int background;
    float progress;
    float max;

    Paint paint;

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        progressColor = Config.accentColor(context, null);
        textColor = Config.primaryColor(context, null);
        background = Color.LTGRAY;
        textSize = 55;
        progressWidth = 8;

        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float center = getMeasuredWidth() / 2;
        radius = center - progressWidth / 2 ;
        paint.setColor(background);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(progressWidth); // 圆环的宽度
        canvas.drawCircle(center, center, radius, paint);


        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setStrokeWidth(3);
        paint.setTypeface(Typeface.DEFAULT_BOLD);

        int percent = (int) (progress / max * 100);
        String content = percent + "%";
        Paint.FontMetrics fm = paint.getFontMetrics();

        canvas.drawText(content,
                (getMeasuredWidth() - paint.measureText(content)) / 2,
                getWidth() / 2  + (fm.bottom - fm.top)/2 - fm.bottom,
                paint);

        // 画圆弧
        paint.setColor(progressColor);
        paint.setStrokeWidth(progressWidth);
        paint.setStrokeCap(Paint.Cap.ROUND);
        RectF rect = new RectF(center - radius, center - radius, center + radius, center + radius);
        canvas.drawArc(rect, 0, 360 * progress / max, false, paint);
    }

    public void setProgress(float progress){
        if(progress < 0 ){
            throw new IllegalArgumentException("进度Progress不能小于0");
        }
        if(progress > max){
            progress = max;
        }
        if(progress <= max){
            this.progress = progress;
            postInvalidate();
        }
    }

    public void setMax(float max) {
        this.max = max;
    }
}
