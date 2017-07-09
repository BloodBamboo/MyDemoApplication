package com.example.admin.myapplication.view.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by 12 on 2017/7/9.
 */

public class PaletteRelativeLayout extends RelativeLayout {
    Paint paint;
    int[] colors = new int[2];

    public PaletteRelativeLayout(Context context) {
        this(context, null);
    }

    public PaletteRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        setWillNotDraw(false);
    }

    public void setBgColor(int bgColor) {
        colors[0] = bgColor;
        colors[1] =  colorBurn(bgColor);
        LinearGradient mSweepGradient = new LinearGradient(10, 10, getWidth() - 10, getHeight() - 10, colors, null, Shader.TileMode.CLAMP);
        paint.setShader(mSweepGradient);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (colors.length < 2) {
            return;
        }
        canvas.drawRoundRect(10, 10, getWidth() - 10, getHeight() - 10, 10, 10, paint);
    }

    private int colorBurn(int rgb) {
        //加深颜色
//        int all= (int) (rgb*1.1f);
//        红色
        int  red=rgb>>16&0xFF;
        int gree=rgb>>8&0xFF;
        int blue=rgb&0xFF;

        //
        red = (int) Math.floor(red * (1 - 0.2));
        gree = (int) Math.floor(gree * (1 - 0.2));
        blue = (int) Math.floor(blue * (1 - 0.15));
        return Color.rgb(red, gree, blue);

    }
}
