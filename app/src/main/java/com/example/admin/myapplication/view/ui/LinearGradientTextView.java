package com.example.admin.myapplication.view.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by yangxin on 2017/5/14.
 */

public class LinearGradientTextView extends android.support.v7.widget.AppCompatTextView {

    private Paint mPaint;
    private LinearGradient mLinearGradient;

    private float mTranslate;
    private float DELTAX = 20;
    float width;


    public LinearGradientTextView(Context context) {
        this(context, null);
    }

    public LinearGradientTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPaint = getPaint();

        String text = getText().toString();

        width = mPaint.measureText(text);

        int widthThree = (int)(width / text.length() * 3);


        mLinearGradient = new LinearGradient(widthThree,0,-widthThree,0,new int[]{
                0x22ffaaff, 0xffffaaff, 0x22ffaaff},null, Shader.TileMode.CLAMP);

        mPaint.setShader(mLinearGradient);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mTranslate += DELTAX;
        if (mTranslate > width || mTranslate < 0) {
            DELTAX = - DELTAX;
        }

        Matrix matrix = new Matrix();
        matrix.setTranslate(mTranslate, 0);

        mLinearGradient.setLocalMatrix(matrix);
        postInvalidateDelayed(50);
    }
}
