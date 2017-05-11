package com.example.admin.myapplication.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.admin.myapplication.R;

/**
 * Created by yangxin on 2017/5/11.
 */

public class ShaderView extends View {

    Paint mPaint;
    Bitmap mBitmap;
    private int mWidth;
    private int mHeight;

    private int[] mColors = {Color.RED,Color.GREEN,Color.BLUE,Color.YELLOW};

    public ShaderView(Context context) {
        this(context, null);
    }

    public ShaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();

        mBitmap = ((BitmapDrawable)context.getDrawable(R.drawable.xyjy)).getBitmap();

        mWidth = mBitmap.getWidth();
        mHeight = mBitmap.getHeight();

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.WHITE);
        /**
         * TileMode.CLAMP 拉伸最后一个像素去铺满剩下的地方
         * TileMode.MIRROR 通过镜像翻转铺满剩下的地方。
         * TileMode.REPEAT 重复图片平铺整个画面（电脑设置壁纸）
         */
        BitmapShader bitmapShader = new BitmapShader(mBitmap, Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);


        Matrix matrix = new Matrix();

        int scale = Math.max(mHeight, mWidth) / Math.min(mHeight, mWidth);

        matrix.setScale(scale, scale);
        bitmapShader.setLocalMatrix(matrix);


        mPaint.setShader(bitmapShader);
        canvas.drawCircle(mHeight /2 , mHeight /2, mHeight /2, mPaint);
//        canvas.drawRect(new Rect(0,0 , 1000, 1600),mPaint);
    }
}
