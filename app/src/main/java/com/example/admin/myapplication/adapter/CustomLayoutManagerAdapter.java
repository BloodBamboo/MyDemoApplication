package com.example.admin.myapplication.adapter;

import android.content.Context;

import com.example.admin.myapplication.R;
import com.example.admin.myapplication.bean.Custom;
import com.example.admin.myapplication.recycle_view.callback.ItemTouchHelperAdapter;
import com.example.admin.myapplication.recycle_view.helper.BaseRecycleAdapter;
import com.example.admin.myapplication.recycle_view.helper.BaseViewHolder;


/**
 * Created by Administrator on 2017/1/5.
 */

public class CustomLayoutManagerAdapter extends BaseRecycleAdapter<Custom> implements ItemTouchHelperAdapter {
    public CustomLayoutManagerAdapter(Context context, int layoutId) {
        super(context, layoutId, NULL_VALUE, NULL_VALUE);
    }

    @Override
    protected void bindViewHolder(BaseViewHolder holder, Custom item) {
        holder.setText(R.id.tvPrecent, item.name + "/" + (getItemCount() - 1))
        .setImage(R.id.iv, item.image_res);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {

    }

    @Override
    public void onItemDismiss(int position) {
        getDatas().add(0, getDatas().remove(position));
        notifyDataSetChanged();
    }

    @Override
    public void onNotifyDataSetChanged() {
        notifyDataSetChanged();
    }
}
