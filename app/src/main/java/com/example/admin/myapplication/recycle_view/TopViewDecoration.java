package com.example.admin.myapplication.recycle_view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by yx on 2016/12/22.
 * 本例中没有padding值,所以当存在时可能会有些许便宜,请自行调整
 */

public class TopViewDecoration extends RecyclerView.ItemDecoration {
//    private BaseRecycleAdapter<RecycleTestBean> mAdapter;
    public interface TopViewDecorationCallback {

    boolean isTopView(int position);

    boolean isDrawTitleArea(int position);

    boolean isDrawOver(int position);

    String getText(int position);
}

    private TopViewDecorationCallback mCallback;
    private int mHeight;
    private Paint mPaint;
    private static int COLOR_TITLE_BG = Color.parseColor("#FFDFDFDF");
    private static int COLOR_TITLE_FONT = Color.parseColor("#FF000000");

    public TopViewDecoration(TopViewDecorationCallback callback, int height, int sizeFont) {
        mCallback = callback;
        mHeight = height;
        mPaint = new Paint();
        mPaint.setTextSize(sizeFont);
        mPaint.setAntiAlias(true);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = ((RecyclerView.LayoutParams)view.getLayoutParams()).getViewLayoutPosition();

        if (position > -1) {
            if (mCallback.isTopView(position)) //position < mAdapter.getItemCount() && mAdapter.getDatas().get(position).groupName != null && !mAdapter.getDatas().get(position).groupName.equals(mAdapter.getDatas().get(position - 1).groupName)) {
            {
                outRect.set(0, mHeight, 0, 0);
            } else {
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int width = parent.getWidth() - parent.getPaddingRight();
        for (int i = 0, count = parent.getChildCount(); i < count; i++) {
            View view = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
            int position = params.getViewLayoutPosition();
            if (position > -1) {
                if (mCallback.isDrawTitleArea(position))//position < mAdapter.getItemCount() && mAdapter.getDatas().get(position).groupName != null && !mAdapter.getDatas().get(position).groupName.equals(mAdapter.getDatas().get(position - 1).groupName)) {
                {
                    drawTitleArea(c, parent.getPaddingLeft(), width, view.getTop() - params.topMargin - mHeight, view.getTop() - params.topMargin, view, position);
                } else {
                }
            }
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int position = ((LinearLayoutManager)parent.getLayoutManager()).findFirstVisibleItemPosition();
        if (position > -1) {
            View view = parent.findViewHolderForLayoutPosition(position).itemView;//出现一个奇怪的bug，有时候child为空，所以将 child = parent.getChildAt(i)。-》 parent.findViewHolderForLayoutPosition(pos).itemView
            boolean flag = false;//定义一个flag，Canvas是否位移过的标志
            if (mCallback.isDrawOver(position) && (view.getTop() + view.getHeight()) < mHeight)
//                    (position + 1) < mAdapter.getDatas().size()
//                    && mAdapter.getDatas().get(position).groupName != null
//                    && !mAdapter.getDatas().get(position).groupName.equals(mAdapter.getDatas().get(position + 1).groupName)
//                    && (view.getTop() + view.getHeight()) < mHeight)
            {
                c.save();//每次绘制前 保存当前Canvas状态
                flag = true;
                //一种头部折叠起来的视效，个人觉得也还不错~
                //可与123行 c.drawRect 比较，只有bottom参数不一样，由于 child.getHeight() + child.getTop() < mTitleHeight，所以绘制区域是在不断的减小，有种折叠起来的感觉
                c.clipRect(0, view.getPaddingTop(), view.getRight() - view.getPaddingRight(), view.getHeight() + view.getTop());

                //类似饿了么点餐时,商品列表的悬停头部切换“动画效果”
                //上滑时，将canvas上移 （y为负数） ,所以后面canvas 画出来的Rect和Text都上移了，有种切换的“动画”感觉
//                c.translate(0, view.getHeight() + view.getTop() - mHeight);

            }

            drawTitleArea(c, parent.getPaddingLeft(), view.getWidth() - view.getPaddingRight(), view.getPaddingTop(), view.getPaddingTop() + mHeight, view, position);
            if (flag)
                c.restore();//恢复画布到之前保存的状态
        }
    }

    private void drawTitleArea(Canvas c, int paddingLeft, int width, int top, int height, View view, int position) {
        String text = mCallback.getText(position);// mAdapter.getDatas().get(position).groupName;
        if (text == null) {
            return;
        }
        mPaint.setColor(COLOR_TITLE_BG);
        c.drawRect(paddingLeft, top, width, height, mPaint);
        mPaint.setColor(COLOR_TITLE_FONT);
        Rect mBounds = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), mBounds);
        c.drawText(text, view.getPaddingLeft(), height - (mHeight / 2 - mBounds.height() / 2), mPaint);
    }
}
