package com.example.admin.myapplication.recycle_view.helper;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/1/9.
 */

public class LoadMoreView extends RelativeLayout {
    private int loadMoreType = -1; //-1空数据 , 0 加载更多 1 加载完成
    private String loadEndContent = "全部加载";
    private String loadMoreContent = "加载更多";
    private String loadEmptyContent = "";
    private TextView mtextView;


    public void setLoadEnd()
    {
        loadMoreType = 1;
        mtextView.setText(loadEndContent);
    }

    public void setLoadMore()
    {
        loadMoreType = 0;
        mtextView.setText(loadMoreContent);
    }

    public void setLoadEmpty()
    {
        loadMoreType = -1;
        mtextView.setText(loadEmptyContent);
    }

    public int getLoadType(){
        return loadMoreType;
    }

    public void setLoadEndContent(String loadEndContent) {
        this.loadEndContent = loadEndContent;
    }

    public void setLoadMoreContent(String loadMoreContent) {
        this.loadMoreContent = loadMoreContent;
    }

    public void setLoadEmptyContent(String loadEmptyContent) {
        this.loadEmptyContent = loadEmptyContent;
    }


    public LoadMoreView(Context context) {
        this(context, null);
    }

    public LoadMoreView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public LoadMoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
    }

    private void initView(Context context) {
        setLayoutParams(new RecyclerView.LayoutParams(LayoutParams.MATCH_PARENT, 200));

        mtextView = new TextView(context);
        mtextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        mtextView.setTextColor(Color.parseColor("#727272"));
        mtextView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mtextView.setGravity(Gravity.CENTER);
        addView(mtextView);
    }


}
