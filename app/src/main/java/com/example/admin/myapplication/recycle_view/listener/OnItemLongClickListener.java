package com.example.admin.myapplication.recycle_view.listener;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2016/9/25.
 */

public interface OnItemLongClickListener {
    void onItemLongClick(int position, View view, RecyclerView.ViewHolder vh);
}
