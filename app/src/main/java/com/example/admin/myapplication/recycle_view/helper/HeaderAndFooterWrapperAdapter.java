package com.example.admin.myapplication.recycle_view.helper;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.myapplication.adapter.oneAdapter;
import com.example.admin.myapplication.recycle_view.callback.ItemTouchHelperAdapter;


/**
 * Created by Administrator on 2017/1/6.
 */

public class HeaderAndFooterWrapperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter, oneAdapter.oneAdapterCallBack {
    private static final int BASE_ITEM_TYPE_HEADER = 1000000;//headerview的viewtype基准值
    private static final int BASE_ITEM_TYPE_FOOTER = 2000000;//footerView的ViewType基准值

    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();//存放HeaderViews,key是viewType
    private SparseArrayCompat<View> mFooterViews = new SparseArrayCompat<>();//存放FooterViews,key是viewType

    protected BaseRecycleAdapter mInnerAdapter;//内部的的普通Adapter

    public HeaderAndFooterWrapperAdapter(BaseRecycleAdapter mInnerAdapter) {
        this.mInnerAdapter = mInnerAdapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null) {//不为空，说明是headerview
            return new BaseViewHolder(mHeaderViews.get(viewType));
        } else if (mFooterViews.get(viewType) != null) {//不为空，说明是footerview
            return new BaseViewHolder(mFooterViews.get(viewType));
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderViewPos(position)) {
            return;
        } else if (isFooterViewPos(position)) {
            return;
        }
        //举例子，2个header，0 1是头，2是开始，2-2 = 0
        mInnerAdapter.onBindViewHolder(holder, position - getHeaderViewCount());
    }

    @Override
    public int getItemCount() {
        return getInnerItemCount() + getHeaderViewCount() + getFooterViewCount();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mInnerAdapter.onAttachedToRecyclerView(recyclerView);
        //为了兼容GridLayout
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int viewType = getItemViewType(position);
                    if (isHeaderViewPos(position)) {
                        return gridLayoutManager.getSpanCount();
                    } else if (mFooterViews.get(viewType) != null) {
                        return gridLayoutManager.getSpanCount();
                    }
                    if (spanSizeLookup != null)
                        return spanSizeLookup.getSpanSize(position);
                    return 1;
                }
            });
            gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
        }

    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isHeaderViewPos(position) || isFooterViewPos(position)) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

            if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {

                StaggeredGridLayoutManager.LayoutParams p =
                        (StaggeredGridLayoutManager.LayoutParams) lp;

                p.setFullSpan(true);
            }
        }
    }

    public int getHeaderViewCount() {
        return mHeaderViews.size();
    }

    public int getFooterViewCount() {
        return mFooterViews.size();
    }

    public int getInnerItemCount() {
        return mInnerAdapter == null ? 0 : mInnerAdapter.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderViewPos(position)) {
            return mHeaderViews.keyAt(position);
        } else if (isFooterViewPos(position)) {//举例：header 2， innter 2， 0123都不是，4才是，4-2-2 = 0，ok。
            return mFooterViews.keyAt(position - getHeaderViewCount() - getInnerItemCount());
        }
        return super.getItemViewType(position - getHeaderViewCount());
    }

    /**
     * 传入position 判断是否是headerview
     *
     * @param position
     * @return
     */
    public boolean isHeaderViewPos(int position) {// 举例， 2 个头，pos 0 1，true， 2+ false
        return getHeaderViewCount() > position;
    }

    /**
     * 传入postion判断是否是footerview
     *
     * @param position
     * @return
     */
    public boolean isFooterViewPos(int position) {//举例， 2个头，2个inner，pos 0 1 2 3 ,false,4+true
        return position >= getHeaderViewCount() + getInnerItemCount();
    }

    /**
     * 添加HeaderView
     *
     * @param v
     */
    public void addHeaderView(View v) {
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, v);
    }

    /**
     * 添加FooterView
     *
     * @param v
     */
    public void addFooterView(View v) {
        mFooterViews.put(mFooterViews.size() + BASE_ITEM_TYPE_FOOTER, v);
    }

    /**
     * 清空HeaderView数据
     */
    public void clearHeaderView() {
        mHeaderViews.clear();
    }

    public void clearFooterView() {
        mFooterViews.clear();
    }

    /**
     * 设置HeaderView（会先清空Headerview）
     *
     * @param v
     */
    public void setHeaderView(View v) {
        clearHeaderView();
        addHeaderView(v);
    }

    public void setFooterView(View v) {
        clearFooterView();
        addFooterView(v);
    }

    public View getHeaderView(int pos) {
        if (pos < mHeaderViews.size()) {
            return mHeaderViews.get(mHeaderViews.keyAt(pos));
        }
        return null;
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition <= getHeaderViewCount() - 1 || fromPosition > mInnerAdapter.getItemCount() + getHeaderViewCount() - 1) {
            return;
        } else {
            mInnerAdapter.onItemMove(fromPosition - getHeaderViewCount(), toPosition - getHeaderViewCount());
        }
    }

    @Override
    public void onItemDismiss(int position) {
        if (position <= getHeaderViewCount() - 1 || position > mInnerAdapter.getItemCount() + getHeaderViewCount() - 1) {
            return;
        } else {
            mInnerAdapter.onItemDismiss(position - getHeaderViewCount());
        }
    }

    @Override
    public void onNotifyDataSetChanged() {
        mInnerAdapter.onNotifyDataSetChanged();
    }


    @Override
    public int getHeadCount() {
        return getHeaderViewCount();
    }
}
