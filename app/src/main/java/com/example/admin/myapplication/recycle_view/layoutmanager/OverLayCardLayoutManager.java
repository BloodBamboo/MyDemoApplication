package com.example.admin.myapplication.recycle_view.layoutmanager;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.admin.myapplication.R;

import static com.example.admin.myapplication.recycle_view.layoutmanager.CardConfig.MAX_SHOW_COUNT;
import static com.example.admin.myapplication.recycle_view.layoutmanager.CardConfig.SCALE_GAP;
import static com.example.admin.myapplication.recycle_view.layoutmanager.CardConfig.TRANS_Y_GAP;

/**
 * Created by Administrator on 2016/12/28.
 */

public class OverLayCardLayoutManager extends RecyclerView.LayoutManager {

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);//解绑所有的itemView
        int itemCount = getItemCount();
        if (itemCount >= MAX_SHOW_COUNT) {
            //从可见的最底层View开始layout，依次层叠上去
            for (int position = itemCount - MAX_SHOW_COUNT; position < itemCount; position++) {
                View view = recycler.getViewForPosition(position);
                view.setRotation(0);
                view.findViewById(R.id.iv_love).setAlpha(0);
                view.findViewById(R.id.iv_del).setAlpha(0);
                addView(view);
                measureChildWithMargins(view, 0, 0);
                int widthSpace  = getWidth() - getDecoratedMeasuredWidth(view);
                int heightSpace = getHeight() -getDecoratedMeasuredHeight(view);
                //我们在布局时，将childView居中处理，这里也可以改为只水平居中
                layoutDecoratedWithMargins(view,
                        widthSpace / 2,
                        heightSpace / 2,
                        widthSpace / 2 + getDecoratedMeasuredWidth(view),
                        heightSpace / 2 + getDecoratedMeasuredHeight(view));
                /**
                 * TopView的Scale 为1，translationY 0
                 * 每一级Scale相差0.05f，translationY相差7dp左右
                 *
                 * 观察人人影视的UI，拖动时，topView被拖动，Scale不变，一直为1.
                 * top-1View 的Scale慢慢变化至1，translation也慢慢恢复0
                 * top-2View的Scale慢慢变化至 top-1View的Scale，translation 也慢慢变化只top-1View的translation
                 * top-3View的Scale要变化，translation岿然不动
                 */
                //第几层,举例子，count =7， 最后一个TopView（6）是第0层，
                int level = itemCount - position - 1;
                //除了顶层不需要缩小和位移
                if (level > 0/*&& level < mShowCount - 1*/) {
                    //每一层都需要X方向的缩小
                    view.setScaleX(1 - SCALE_GAP * level);
                    //前N层，依次向下位移和Y方向的缩小
                    if (level < MAX_SHOW_COUNT - 1) {
                        view.setTranslationY(TRANS_Y_GAP * level);
                        view.setScaleY(1 - SCALE_GAP * level);
                    } else {//第N层在 向下位移和Y方向的缩小的成都与 N-1层保持一致
                        view.setTranslationY(TRANS_Y_GAP * (level - 1));
                        view.setScaleY(1 - SCALE_GAP * (level - 1)
                        );
                    }
                }
            }
        }
    }
}