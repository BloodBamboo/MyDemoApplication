package com.example.admin.myapplication.view.ui.drawer_layout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by Administrator on 2017/7/3.
 */

public class DrawWaveBgView extends View {
    private Paint paint;
    private Path path;

    public DrawWaveBgView(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint = new Paint();
        path = new Path();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
    }

    public void setColor(Drawable color) {
        if (color instanceof ColorDrawable) {
            ColorDrawable colorDrawable = (ColorDrawable) color;
            paint.setColor(colorDrawable.getColor());
        } else {

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
    }

    public void setTouchY(float y, float percent) {
        path.reset();
        float width = getWidth() * percent;
        float height = getHeight();
        float offsetY = height / 8;
        float x = width / 2;
        path.moveTo(0, 0);
        path.lineTo(x, -offsetY);
        path.quadTo(width, y, x, height + offsetY);
        path.lineTo(0, height);
        path.close();
//        float width=getWidth()*percent;
//        float height=getHeight();
//        float offsetY=height/8;
//        float x=width/2;
//        path.lineTo(x,-offsetY);
//        path.quadTo(width*3/2,y,x,height+offsetY);
//        path.lineTo(0,height);
//        path.close();
        invalidate();
    }
}
