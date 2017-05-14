package com.example.admin.myapplication.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by yangxin on 2017/5/14.
 */

public class RippleButton extends android.support.v7.widget.AppCompatButton {

    private float mx, my;
    private int defult_radius = 50;
    private ObjectAnimator mAnimator;
    private int center_color;
    private int edge_color;
    private int mCurRaduis;
    private Paint mPaint;
    private RadialGradient mRadialGradient;

    public RippleButton(Context context) {
        this(context, null);
    }

    public RippleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        center_color = Color.parseColor("#00FFFFFF");
        edge_color= Color.BLUE;
        mPaint = new Paint();
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mx != event.getX() || my != event.getY()) {
            mx = event.getX();
            my = event.getY();
            setRadius(defult_radius);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_UP:
                if (mAnimator != null && mAnimator.isRunning()) {
                    mAnimator.cancel();
                }
                if (mAnimator == null) {
                    mAnimator = ObjectAnimator.ofInt(this, "radius", defult_radius, getWidth());
                }

                mAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        setRadius(0);
                    }
                });

                mAnimator.start();
        }

        return super.onTouchEvent(event);
    }

    public void setRadius(int raduis) {
        mCurRaduis = raduis;
        if (mCurRaduis > 0) {
            mRadialGradient = new RadialGradient(mx, my, mCurRaduis, center_color, edge_color, Shader.TileMode.CLAMP);
            mPaint.setShader(mRadialGradient);
        }
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(mx, my, mCurRaduis, mPaint);
    }
}
