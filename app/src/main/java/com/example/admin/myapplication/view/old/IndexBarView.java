package com.example.admin.myapplication.view.old;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


import com.example.admin.myapplication.R;
import com.example.admin.myapplication.bean.RecycleTestBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.admin.myapplication.R.styleable.IndexBar;


/**
 * Created by yx on 2016/12/26.
 */

public class IndexBarView extends View {
    /**
     * 当前被按下的index的监听器
     */
    public interface onIndexPressedListener {
        void onIndexPressed(int index, String text);//当某个Index被按下

        void onMotionEventEnd();//当触摸事件结束（UP CANCEL）
    }


    private boolean isNeedRealIndex = false;
    private TextView textHint;
    private LinearLayoutManager mLayoutManager;
    private int mTextSize;
    private int mTextColor;
    private int mPressBackground;
    private static final String TAG = "zxt/IndexBar";
    public static String[] INDEX_STRING = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};//#在最后面（默认的数据源）
    private List<String> mIndexDatas;
    private ArrayMap<String, Integer> indexMap;
    private int mGapHeight;//每个index区域的高度
    private int mWidth;
    private int mHeight;
    private Paint mPaint;

    private onIndexPressedListener mOnIndexPressedListener;



    public IndexBarView(Context context) {
        this(context, null);
    }

    public IndexBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndexBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics());
        mTextColor = Color.BLACK;
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, IndexBar, defStyleAttr, 0);
        for (int i = 0, count = typedArray.getIndexCount(); i < count; i++) {
            int index = typedArray.getIndex(i);
            switch (typedArray.getIndex(i)) {
                case R.styleable.IndexBar_textSize:
                    mTextSize = typedArray.getDimensionPixelSize(index, mTextSize);
                    break;
                case R.styleable.IndexBar_pressBackground:
                    mPressBackground = typedArray.getColor(index, Color.LTGRAY);
                    break;
                case R.styleable.IndexBar_textColor:
                    mTextColor = typedArray.getColor(index, mTextColor);
                    break;
                default:
                    break;
            }
        }
        typedArray.recycle();

        if (!isNeedRealIndex) {//不需要真实的索引数据源
            mIndexDatas = Arrays.asList(INDEX_STRING);
        }
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(Color.BLACK);

        //设置index触摸监听器
        setmOnIndexPressedListener(new IndexBarView.onIndexPressedListener() {
            @Override
            public void onIndexPressed(int index, String text) {
                if (textHint != null) { //显示hintTexView
                    textHint.setVisibility(View.VISIBLE);
                    textHint.setText(text);
                }
                //滑动Rv
                if (mLayoutManager != null) {
                    int position = indexMap.get(text);
                    if (position != -1) {
                        mLayoutManager.scrollToPositionWithOffset(position, 0);
                    }
                }
            }

            @Override
            public void onMotionEventEnd() {
                //隐藏hintTextView
                if (textHint != null) {
                    textHint.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        int measureWidth = 0, measureHeight = 0;//最终测量出来的宽高

        //得到合适宽度：
        Rect indexBounds = new Rect();//存放每个绘制的index的Rect区域
        String index;//每个要绘制的index内容
        for (int i = 0; i < mIndexDatas.size(); i++) {
            index = mIndexDatas.get(i);
            mPaint.getTextBounds(index, 0, index.length(), indexBounds);//测量计算文字所在矩形，可以得到宽高
            measureWidth = Math.max(indexBounds.width(), measureWidth);//循环结束后，得到index的最大宽度
            measureHeight = Math.max(indexBounds.width(), measureHeight);//循环结束后，得到index的最大高度，然后*size
        }

        measureHeight = measureHeight * mIndexDatas.size();
        switch (wMode) {
            case MeasureSpec.EXACTLY:
                measureWidth = wSize;
                break;
            case MeasureSpec.AT_MOST:
                measureWidth = Math.min(measureWidth, wSize);//wSize此时是父控件能给子View分配的最大空间
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }

        switch (hMode) {
            case MeasureSpec.EXACTLY:
                measureHeight = hSize;
                break;
            case MeasureSpec.AT_MOST:
                measureHeight = Math.min(measureHeight, hSize);//wSize此时是父控件能给子View分配的最大空间
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }

        setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mGapHeight = (mHeight - getPaddingTop() - getPaddingBottom()) / mIndexDatas.size();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int t = getPaddingTop();//top的基准点(支持padding)
        Rect indexBounds = new Rect();//存放每个绘制的index的Rect区域
        for (int i = 0, count = mIndexDatas.size(); i < count; i++) {
            String content = mIndexDatas.get(i);
            Rect rect = new Rect();
            mPaint.getTextBounds(content, 0, content.length(), rect);//测量计算文字所在矩形，可以得到宽高
            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();//获得画笔的FontMetrics，用来计算baseLine。因为drawText的y坐标，代表的是绘制的文字的baseLine的位置
            int baseline = (int) ((mGapHeight - fontMetrics.bottom - fontMetrics.top) / 2);//计算出在每格index区域，竖直居中的baseLine值
            canvas.drawText(content, (mWidth - rect.width()) / 2, t + mGapHeight * i + baseline, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setBackgroundColor(mPressBackground);//手指按下时背景变色
                dropPoint(event);
                break;
            case MotionEvent.ACTION_MOVE:
                dropPoint(event);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            default:
                setBackgroundColor(Color.TRANSPARENT);//手指抬起时背景恢复透明
                //回调监听器
                if (null != mOnIndexPressedListener) {
                    mOnIndexPressedListener.onMotionEventEnd();
                }
                break;
        }
        return true;
    }

    public IndexBarView setTextHint(TextView textHint) {
        this.textHint = textHint;
        return this;
    }

    public IndexBarView setLayoutManager(LinearLayoutManager mLayoutManager) {
        this.mLayoutManager = mLayoutManager;
        return this;
    }

    public IndexBarView setNeedRealIndex(boolean needRealIndex) {
        isNeedRealIndex = needRealIndex;
        return this;
    }

    private void dropPoint(MotionEvent event) {
        float y = event.getY();
        //通过计算判断落点在哪个区域：
        int pressI = (int) ((y - getPaddingTop()) / mGapHeight);
        //边界处理（在手指move时，有可能已经移出边界，防止越界）
        if (pressI < 0) {
            pressI = 0;
        } else if (pressI >= mIndexDatas.size()) {
            pressI = mIndexDatas.size() - 1;
        }
        //回调监听器
        if (null != mOnIndexPressedListener) {
            mOnIndexPressedListener.onIndexPressed(pressI, mIndexDatas.get(pressI));
        }
    }

    private void setmOnIndexPressedListener(onIndexPressedListener mOnIndexPressedListener) {
        this.mOnIndexPressedListener = mOnIndexPressedListener;
    }

    public IndexBarView setSourceDatas(List<RecycleTestBean> list) {
        //对数据源进行初始化
        mIndexDatas = new ArrayList<>();
        indexMap = new ArrayMap<>();
        for (RecycleTestBean bean : list) {
            if (!mIndexDatas.contains(bean.groupName)) {
                mIndexDatas.add(bean.groupName);
                indexMap.put(bean.groupName, list.indexOf(bean));
            }
        }
        return this;
    }
}
