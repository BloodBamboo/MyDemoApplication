package com.example.admin.myapplication.view.ui;


import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017/5/26.
 */

public class SearchView extends View {

    private Paint paint;
    private int width = 400;
    private int height = 400;
    private Path path;
    private PathMeasure pathMeasure;
    private int radius = 80;

    private float p;
    private boolean b;


    public SearchView(Context context) {
        this(context, null);
    }

    public SearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(Color.LTGRAY);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path.reset();
        int x = width / 2 + radius;
        int y = height / 3 + radius;

//        canvas.save();
//        canvas.translate(width / 2, 0);
//        canvas.rotate(45);
//        path.addCircle(width / 2, height / 3, radius, Path.Direction.CW);
//        path.lineTo(x + radius, y - radius);

        //不能画360度一个满圆，否则会改变path的最后落点，不是45度的点
        path.addArc(new RectF(width / 2 - radius, height / 3 - radius, width / 2 + radius, height / 3 + radius), 45, 359);
        path.lineTo(x + radius, y + radius);

        if (!b) {
            canvas.drawPath(path, paint);
        } else {
            pathMeasure = new PathMeasure();
            pathMeasure.setPath(path, false);
            path.reset();
            float length = pathMeasure.getLength();
            float stop = length * p;
//            float start = (float) (stop - ((0.5 - Math.abs(p - 0.5)) * length));
            float start = stop - 50;
            pathMeasure.getSegment(start, stop, path, true);
//            pathMeasure.nextContour();
//            pathMeasure.getSegment(start, stop, path, true);
            canvas.drawPath(path, paint);
        }
        path.close();
//        canvas.restore();
    }

    public void start() {
        b = true;
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(1000);
        animator.addUpdateListener((ValueAnimator animation) -> {
            p = (float) animation.getAnimatedValue();
            invalidate();
        });
        animator.setInterpolator(null);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.start();
    }
}
