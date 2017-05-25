package com.example.admin.myapplication.recycle_view.helper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.admin.myapplication.recycle_view.callback.ItemTouchHelperAdapter;
import com.example.admin.myapplication.recycle_view.listener.OnItemClickListener;
import com.example.admin.myapplication.recycle_view.listener.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Created by Administrator on 2016/7/20.
 */
public abstract class BaseRecycleAdapter<T> extends RecyclerView.Adapter implements ItemTouchHelperAdapter {

    public static final int NULL_VALUE = -1;

    protected Context context;
    protected int layoutId = -1;

    private LayoutInflater mInflater;
    private OnItemClickListener mClickListener;
    private OnItemLongClickListener mLongClickListener;
    private List<T> mList = new ArrayList<T>();
    private int mViewWidth;
    private int mViewHeight;

    /**
     * @param context    context
     * @param layoutId   item布局ID
     * @param viewWidth  itemView 宽 如果不指定传0, 默认使用布局配置
     * @param viewHeight itemView 高 如果不指定传0, 默认使用布局配置
     */
    public BaseRecycleAdapter(Context context, int layoutId, int viewWidth, int viewHeight) {
        this.context = context;
        this.layoutId = layoutId;
        this.mViewWidth = viewWidth;
        this.mViewHeight = viewHeight;
        mInflater = LayoutInflater.from(context);
    }

    public BaseRecycleAdapter(Context context, int layoutId) {
        this.context = context;
        this.layoutId = layoutId;
        this.mViewWidth = NULL_VALUE;
        this.mViewHeight = NULL_VALUE;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        final BaseViewHolder holder;

        view = mInflater.inflate(layoutId, parent, false);
        if (mViewWidth > NULL_VALUE) {
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
            lp.width = mViewWidth;
            view.setLayoutParams(lp);
        }

        if (mViewHeight > NULL_VALUE) {
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
            lp.height = mViewHeight;
            view.setLayoutParams(lp);
        }

        holder = new BaseViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (mClickListener != null && position >= 0 && position < getDatas().size()) {
                    mClickListener.onItemClick(position, v, holder);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getAdapterPosition();
                if (mLongClickListener != null) {
                    mLongClickListener.onItemLongClick(position, v, holder);
                }
                return true;
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindViewHolder((BaseViewHolder) holder, mList.get(position));
    }

    protected abstract void bindViewHolder(BaseViewHolder holder, T item);

    @Override
    public int getItemCount() {
        return (mList == null ? 0 : mList.size());
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        this.mLongClickListener = itemLongClickListener;
    }

    public List<T> getDatas() {
        return mList;
    }

    public void setDatas(List<T> list) {
        if (this.mList == null) {
            return;
        }
        this.mList.clear();
        if (list == null) {
            notifyDataSetChanged();
            return;
        }
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addAll(Collection<T> list) {
        if (list == null || this.mList == null) {
            return;
        }
        int lastIndex = this.mList.size();
        if (this.mList.addAll(list)) {
            notifyItemRangeInserted(lastIndex, list.size());
        }
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    /**
     * 移除某一条记录
     *
     * @param position 移除数据的position
     */
    public void removeItem(int position) {
        if (this.mList == null) {
            return;
        }
        if (position < mList.size()) {
            mList.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * 在特定位置添加一条记录
     *
     * @param data     需要加入的数据结构
     * @param position 插入位置
     */
    public void addItem(T data, int position) {
        if (this.mList == null) {
            return;
        }
        if (position <= mList.size()) {
            mList.add(position, data);
            notifyItemInserted(position);
        }
    }

    /**
     * 在末尾添加一条记录
     *
     * @param data 需要加入的数据结构
     */
    public void addItem(T data) {
        if (this.mList == null) {
            return;
        }
        addItem(data, mList.size());
    }

    /**
     * 在某处批量添加记录
     *
     * @param data     需要加入的数据结构
     * @param position 插入位置
     */
    public void addItems(List<T> data, int position) {
        if (this.mList == null) {
            return;
        }
        if (position <= mList.size() && data != null && data.size() > 0) {
            mList.addAll(position, data);
            notifyItemRangeChanged(position, data.size());
        }
    }

    public T getItemData(int position) {
        if (this.mList == null || getDatas() == null || position >= getDatas().size()) {
            return null;
        }
        return position < getDatas().size() ? getDatas().get(position) : null;
    }

    public void onDestroy() {
        if (mList != null) {
            mList.clear();
            mList = null;
        }
    }
}
