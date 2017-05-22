package com.example.admin.myapplication.view.ui;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;

/**
 * Created by Administrator on 2017/5/22.
 */

public class RevealDrawable extends Drawable {
    public static final int HORIZONTAL = 1;
    public static final int VERTICAL = 2;

    private final Rect mTmpRect = new Rect();
    private Drawable mUnselectedDrawable;
    private Drawable mSelectedDrawable;
    private int mOrientation;


    public RevealDrawable(Drawable unselected, Drawable selected, int orientation) {
        mUnselectedDrawable = unselected;
        mSelectedDrawable = selected;
        mOrientation = orientation;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        int level = getLevel();
        if (level == 0 || level == 10000) {
            mUnselectedDrawable.draw(canvas);
        } else if (level == 5000) {
            mSelectedDrawable.draw(canvas);
        } else {
            //混合效果的Drawable
            /**
             * 将画板切割成两块-左边和右边
             */
            final Rect r = mTmpRect;
            //得到当前自身Drawable的矩形区域
            Rect bounds = getBounds();
            {
                //1.先绘制灰色部分
                //level 0~5000~10000
                //比例
                float radio = (level / 5000f) -1f;
                int w = getBounds().width();
                if (mOrientation == HORIZONTAL) {
                    w = (int) (w * Math.abs(radio));
                }
                int h = getBounds().height();
                if (mOrientation == VERTICAL) {
                    h = (int) (h * Math.abs(radio));
                }
                int gravity = radio < 0 ? Gravity.LEFT : Gravity.RIGHT;
                //从一个已有的bounds矩形边界范围中抠出一个矩形r
                Gravity.apply(
                        gravity,//从左边还是右边开始抠
                        w,//目标矩形的宽
                        h, //目标矩形的高
                        bounds, //被抠出来的rect
                        r);//目标rect
                canvas.save();
                canvas.clipRect(r);
                mUnselectedDrawable.draw(canvas);
                canvas.restore();
            }

            {
                //2.再绘制彩色部分
                //level 0~5000~10000
                //比例
                float ratio = (level/5000f) - 1f;
                int w = bounds.width();
                if(mOrientation==HORIZONTAL){
                    w -= (int) (w* Math.abs(ratio));
                }
                int h = bounds.height();
                if(mOrientation==VERTICAL){
                    h -= (int) (h* Math.abs(ratio));
                }

                int gravity = ratio < 0 ? Gravity.RIGHT : Gravity.LEFT;
                //从一个已有的bounds矩形边界范围中抠出一个矩形r
                Gravity.apply(
                        gravity,//从左边还是右边开始抠
                        w,//目标矩形的宽
                        h, //目标矩形的高
                        bounds, //被抠出来的rect
                        r);//目标rect

                canvas.save();//保存画布
                canvas.clipRect(r);//切割
                mSelectedDrawable.draw(canvas);//画
                canvas.restore();//恢复之前保存的画布
            }
        }
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        // 定好两个Drawable图片的宽高---边界bounds
        mUnselectedDrawable.setBounds(bounds);
        mSelectedDrawable.setBounds(bounds);
    }

    @Override
    public int getIntrinsicWidth() {
        //得到Drawable的实际宽度
        return Math.max(mSelectedDrawable.getIntrinsicWidth(),
                mUnselectedDrawable.getIntrinsicWidth());
    }

    @Override
    public int getIntrinsicHeight() {
        //得到Drawable的实际高度
        return Math.max(mSelectedDrawable.getIntrinsicHeight(),
                mUnselectedDrawable.getIntrinsicHeight());
    }

    @Override
    protected boolean onLevelChange(int level) {
        // 当设置level的时候回调---提醒自己重新绘制
        invalidateSelf();
        return true;
    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }
}
