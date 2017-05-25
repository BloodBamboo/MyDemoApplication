package com.example.admin.myapplication.recycle_view.callback;

import android.graphics.Canvas;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.animation.AlphaAnimation;

import com.example.admin.myapplication.R;

import static com.example.admin.myapplication.recycle_view.layoutmanager.CardConfig.MAX_SHOW_COUNT;
import static com.example.admin.myapplication.recycle_view.layoutmanager.CardConfig.SCALE_GAP;
import static com.example.admin.myapplication.recycle_view.layoutmanager.CardConfig.TRANS_Y_GAP;


/**
 * Created by Administrator on 2017/1/5.
 */

public class CustomItemTouchHelep extends ItemTouchHelper.Callback {
    private static final int MAX_ROTATION = 15;

    private ItemTouchHelperAdapter mAdapter;
    private RecyclerView mRecyclerView;

    public CustomItemTouchHelep(RecyclerView recyclerView, ItemTouchHelperAdapter adapter) {
        this.mAdapter = adapter;
        mRecyclerView = recyclerView;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        viewHolder.itemView.setRotation(0);
        //自己感受一下吧 Alpha
        setAlpha(viewHolder, R.id.iv_love, 0);
        setAlpha(viewHolder, R.id.iv_del, 0);
        mAdapter.onItemDismiss(viewHolder.getLayoutPosition());
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        //先根据滑动的dxdy 算出现在动画的比例系数fraction
        double swipValue = Math.sqrt(dX * dX + dY * dY);
        double fraction = swipValue / getThreshold(viewHolder);
        //边界修正 最大为1
        if (fraction > 1) {
            fraction = 1;
        }

        //对每个ChildView进行缩放 位移
        for (int i = 0, childCount = recyclerView.getChildCount(); i < childCount; i++) {
            View child = mRecyclerView.getChildAt(i);
            //第几层,举例子，count =7， 最后一个TopView（6）是第0层，
            int level = childCount - i - 1;
            if (level > 0) {
                child.setRotation(0);
                child.findViewById(R.id.iv_love).setAlpha(0);
                child.findViewById(R.id.iv_del).setAlpha(0);
                child.setScaleX((float) (1 - SCALE_GAP * level + fraction * SCALE_GAP));
                if (level < MAX_SHOW_COUNT - 1) {
                    child.setScaleY((float) (1 - SCALE_GAP * level + fraction * SCALE_GAP));
                    child.setTranslationY((float) (TRANS_Y_GAP * level - fraction * TRANS_Y_GAP));
                }
            } else {
                //探探只是第一层加了rotate & alpha的操作
                //不过他区分左右
                float xFraction = dX / getThreshold(viewHolder);
                //边界修正 最大为1
                if (xFraction > 1) {
                    xFraction = 1;
                } else if (xFraction < -1) {
                    xFraction = -1;
                }
                //rotate
                child.setRotation(xFraction * MAX_ROTATION);

                //自己感受一下吧 Alpha
                if (viewHolder instanceof RecyclerView.ViewHolder) {
                    if (dX > 0) {
                        //露出左边，比心
                        setAlpha(viewHolder, R.id.iv_love, xFraction);
                    } else {
                        //露出右边，滚犊子
                        setAlpha(viewHolder, R.id.iv_del, -xFraction);
                    }
                }
            }
        }
    }

    //水平方向是否可以被回收掉的阈值
    public float getThreshold(RecyclerView.ViewHolder viewHolder) {
        return mRecyclerView.getWidth() * getSwipeThreshold(viewHolder);
    }

    public void setAlpha(RecyclerView.ViewHolder viewHolder, int viewId, float value) {
        if (Build.VERSION.SDK_INT >= 11) {
            viewHolder.itemView.findViewById(viewId).setAlpha(value);
        } else {
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0L);
            alpha.setFillAfter(true);
            viewHolder.itemView.findViewById(viewId).startAnimation(alpha);
        }
    }
}
