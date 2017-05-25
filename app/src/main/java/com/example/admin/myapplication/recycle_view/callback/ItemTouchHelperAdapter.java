package com.example.admin.myapplication.recycle_view.callback;

/**
 * Created by Administrator on 2016/12/20.
 */

public interface ItemTouchHelperAdapter {
    void onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
    void onNotifyDataSetChanged();
}
