package com.example.admin.myapplication.view.ui;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.admin.myapplication.R;
import com.example.admin.myapplication.Utils.PathParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangxin on 2017/6/1.
 */

public class MapView extends View {

    private float aFloat;

    class PathBean {
        public Path path;

        public boolean isSelected;

        public PathBean(Path path) {
            this.path = path;
        }

        public void onDraw(Canvas canvas, Paint paint) {
            int color = paint.getColor();
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(4);
            canvas.drawPath(path, paint);

            paint.setColor(color);
            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeWidth(2);
            canvas.drawPath(path, paint);
        }

        public void onDrawLight(Canvas canvas, Paint paint) {
            int color = paint.getColor();
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(4);
            canvas.drawPath(path, paint);

            paint.setColor(color);
            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeWidth(2);
            canvas.drawPath(path, paint);
        }

    }


    private Handler handler;
    private Context context;
    private List<PathBean> list;
    private Paint paint;
    private int Colors[] = {Color.parseColor("#3F51B5"), Color.parseColor("#FF4081"), Color.parseColor("#55FF4081")};

    private float minWidth = 1000;
    private float minHeight = 1000;
    float rate = 0.5f;

    public MapView(Context context) {
        this(context, null);
    }

    public MapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        list = new ArrayList<>();

        handler = new Handler((Message msg) -> {
            if (isAttachedToWindow()) {
                invalidate();
            }
            return true;
        });

        new Thread(() -> {
            XmlResourceParser xrp = getResources().getXml(R.xml.taiwan_map);
            try {
                while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
                    if (xrp.getEventType() == XmlPullParser.START_TAG && xrp.getName().equals("path")) {
                        for (int i = 0, count = xrp.getAttributeCount(); i < count; i++) {
                            if (xrp.getAttributeName(i).equals("pathData")) {
                                list.add(new PathBean(PathParser.createPathFromPathData(xrp.getAttributeValue(i))));
                            }
                        }
                    }
                    xrp.next();
                }
                handler.sendEmptyMessage(0);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).run();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = widthSize;
        int height = heightSize;

        switch (widthMode) {
            case MeasureSpec.EXACTLY :
            case MeasureSpec.AT_MOST:
//                width = widthSize > minWidth ? widthSize : minWidth;
                width = widthSize;
                break;
        }

        switch (heightMode) {
            case MeasureSpec.EXACTLY :
            case MeasureSpec.AT_MOST:
//                height = heightSize > minHeight ? heightSize : minHeight;
                height = heightSize;
                break;
        }
        float temp1 = width / minWidth;
        float temp2 = height / minHeight;
        rate = temp1 > temp2 ? temp1 : temp2;

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.scale(rate, rate);
        for (PathBean bean : list) {
            if (!bean.isSelected) {
                paint.setColor(Colors[list.indexOf(bean) % 3]);
                bean.onDraw(canvas, paint);
            }
        }

        for (PathBean bean : list) {
            if (bean.isSelected) {
                paint.setColor(Colors[list.indexOf(bean) % 3]);
                bean.onDrawLight(canvas, paint);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        list.clear();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (PathBean bean : list) {
                    RectF bounds = new RectF();
                    bean.path.computeBounds(bounds, true);
                    Region region = new Region();
                    region.setPath(bean.path, new Region((int) bounds.left, (int) bounds.top, (int) bounds.right, (int) bounds.bottom));
                    if (region.contains((int) (event.getX() / rate), (int) (event.getY() / rate))) {
                        bean.isSelected = true;
                    } else {
                        bean.isSelected = false;
                    }
                }
                handler.sendEmptyMessage(0);
                return true;
            case MotionEvent.ACTION_MOVE:
                for (PathBean bean : list) {
                    RectF bounds = new RectF();
                    bean.path.computeBounds(bounds, true);
                    Region region = new Region();
                    region.setPath(bean.path, new Region((int) bounds.left, (int) bounds.top, (int) bounds.right, (int) bounds.bottom));
                    if (region.contains((int) (event.getX() / rate), (int) (event.getY() / rate))) {
                        bean.isSelected = true;
                    } else {
                        bean.isSelected = false;
                    }
                }
                handler.sendEmptyMessage(0);
                break;
        }


        return super.onTouchEvent(event);

    }
}
