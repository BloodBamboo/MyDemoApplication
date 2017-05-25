package com.example.admin.myapplication.adapter;

import android.content.Context;
import android.util.Log;


import com.example.admin.myapplication.R;
import com.example.admin.myapplication.bean.RecycleTestBean;
import com.example.admin.myapplication.recycle_view.TopViewDecoration;
import com.example.admin.myapplication.recycle_view.helper.BaseRecycleAdapter;
import com.example.admin.myapplication.recycle_view.helper.BaseViewHolder;

import java.util.Collections;

/**
 * Created by Administrator on 2016/12/13.
 */

public class oneAdapter extends BaseRecycleAdapter<RecycleTestBean> implements TopViewDecoration.TopViewDecorationCallback {
    @Override
    public boolean isTopView(int position) {
        if (mCallback != null) {
            position -= mCallback.getHeadCount();
        }
        if (position == 0) {
            return true;
        }
        if (position < 0) {
            return false;
        }
        return position < getItemCount() && getDatas().get(position).groupName != null && !getDatas().get(position).groupName.equals(getDatas().get(position - 1).groupName);
    }

    @Override
    public boolean isDrawTitleArea(int position) {
        if (mCallback != null) {
            position -= mCallback.getHeadCount();
        }
        if (position == 0) {
            return true;
        }
        if (position < 0) {
            return false;
        }
        return position < getItemCount() && getDatas().get(position).groupName != null && !getDatas().get(position).groupName.equals(getDatas().get(position - 1).groupName);
    }

    @Override
    public boolean isDrawOver(int position) {
        if (mCallback != null) {
            position -= mCallback.getHeadCount();
        }
        if (position < 0) {
            return false;
        }
        return  (position + 1) < getDatas().size()
                && getDatas().get(position).groupName != null
                && !getDatas().get(position).groupName.equals(getDatas().get(position + 1).groupName);
    }

    @Override
    public String getText(int position) {
        if (mCallback != null) {
            position -= mCallback.getHeadCount();
        }
        if (position < 0) {
            return null;
        }
        return getDatas().get(position).groupName;
    }

    public interface oneAdapterCallBack {
        public int getHeadCount();
        public void notifyDataSetChanged();
        public void notifyItemRemoved(int position);
        public void notifyItemMoved(int fromPosition, int toPosition);
    }

    private oneAdapterCallBack mCallback;

    public oneAdapter(Context context, int layoutId) {
        super(context, layoutId, NULL_VALUE, NULL_VALUE);
    }

    @Override
    protected void bindViewHolder(BaseViewHolder holder, RecycleTestBean item) {
        holder.setText(R.id.text, item.name);
    }


    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < 0 || toPosition < 0) {
            return;
        }

        if (!getDatas().get(fromPosition).groupName.equals(getDatas().get(toPosition).groupName)) {
            return;
        }
        Collections.swap(getDatas(),fromPosition, toPosition);
        if (mCallback != null) {
            mCallback.notifyItemMoved(fromPosition + mCallback.getHeadCount(), toPosition + mCallback.getHeadCount());
        } else {
            notifyItemMoved(fromPosition, toPosition);
        }

        Log.e("-----------", toPosition+"");
    }

    @Override
    public void onItemDismiss(int position) {
        getDatas().remove(position);
//        notifyDataSetChanged();
        if (mCallback != null) {
            mCallback.notifyItemRemoved(position + mCallback.getHeadCount());
        } else {
            notifyItemRemoved(position + mCallback.getHeadCount());
        }
    }

    @Override
    public void onNotifyDataSetChanged() {
        notifyDataSetChanged();
    }

    public void setCallback(oneAdapterCallBack mCallback) {
        this.mCallback = mCallback;
    }
}
