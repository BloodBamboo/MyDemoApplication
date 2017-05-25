package com.example.admin.myapplication.activity.old;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Button;
import android.widget.TextView;


import com.example.admin.myapplication.R;
import com.example.admin.myapplication.adapter.oneAdapter;
import com.example.admin.myapplication.bean.RecycleTestBean;
import com.example.admin.myapplication.recycle_view.RecycleViewDivider;
import com.example.admin.myapplication.recycle_view.TopViewDecoration;
import com.example.admin.myapplication.recycle_view.callback.SimpleItemTouchHelperCallback;
import com.example.admin.myapplication.recycle_view.helper.HeaderAndFooterWrapperAdapter;
import com.example.admin.myapplication.recycle_view.helper.LoadMoreView;
import com.example.admin.myapplication.recycle_view.layoutmanager.AnimRFLinearLayoutManager;
import com.example.admin.myapplication.view.old.IndexBarView;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/13.
 */

public class oneActivity extends AppCompatActivity {
    @BindView(R.id.button_head)
    public Button button_head;
    @BindView(R.id.button_foot)
    public Button button_foot;

    private HeaderAndFooterWrapperAdapter wrapperAdapter;

    private LoadMoreView head;
    private LoadMoreView foot;

    RecyclerView recycle;
    oneAdapter adapter;
    List<RecycleTestBean> list = new LinkedList<>();
    Button button;
    IndexBarView indexBarView;
    TextView textHint;
    AnimRFLinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.one_layout);
        ButterKnife.bind(this);
        head = new LoadMoreView(this);
        head.setLoadEmptyContent("准备显示");
        head.setLoadEmpty();
        foot = new LoadMoreView(this);
        foot.setLoadMore();

        indexBarView = (IndexBarView) findViewById(R.id.index_bar);
        textHint = (TextView) findViewById(R.id.tvSideBarHint);
        recycle = (RecyclerView) findViewById(R.id.recycle);

        adapter = new oneAdapter(this, R.layout.text);
        mLayoutManager = new AnimRFLinearLayoutManager(this);
        recycle.setLayoutManager(mLayoutManager);
        wrapperAdapter = new HeaderAndFooterWrapperAdapter(adapter);
        wrapperAdapter.setHeaderView(head);
        wrapperAdapter.setFooterView(foot);
        recycle.setAdapter(wrapperAdapter);

        recycle.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL));
        recycle.addItemDecoration(new TopViewDecoration(adapter, 100, 30));
//        recycle.addItemDecoration(new TopVIewTwoDecoration(this));

        for (int i = 0; i < 10; i++) {
            RecycleTestBean recycleTestBean = new RecycleTestBean();
            recycleTestBean.groupName = "第一组";
            recycleTestBean.name = i + "行";
            list.add(recycleTestBean);
        }
        for (int i = 0; i < 10; i++) {
            RecycleTestBean recycleTestBean = new RecycleTestBean();
            recycleTestBean.groupName = "第二组";
            recycleTestBean.name = i + "行";
            list.add(recycleTestBean);
        }
        for (int i = 0; i < 10; i++) {
            RecycleTestBean recycleTestBean = new RecycleTestBean();
            recycleTestBean.groupName = "第三组";
            recycleTestBean.name = i + "行";
            list.add(recycleTestBean);
        }
        for (int i = 0; i < 10; i++) {
            RecycleTestBean recycleTestBean = new RecycleTestBean();
            recycleTestBean.groupName = "第四组";
            recycleTestBean.name = i + "行";
            list.add(recycleTestBean);
        }
        for (int i = 0; i < 10; i++) {
            RecycleTestBean recycleTestBean = new RecycleTestBean();
            recycleTestBean.groupName = "第五组";
            recycleTestBean.name = i + "行";
            list.add(recycleTestBean);
        }
        adapter.setDatas(list);

        adapter.setCallback(wrapperAdapter);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(wrapperAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recycle);


        button = (Button) findViewById(R.id.button);
        button.setOnClickListener((v) -> {
                RecycleTestBean recycleTestBean = new RecycleTestBean();
                recycleTestBean.groupName = "第一组";
                recycleTestBean.name = "新增行";
                adapter.addItem(recycleTestBean, 2);
                wrapperAdapter.notifyItemInserted(2 + wrapperAdapter.getHeaderViewCount());
        });

        indexBarView.setNeedRealIndex(true)
                .setSourceDatas(list)
                .setTextHint(textHint)
                .setLayoutManager(mLayoutManager);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityCompat.finishAfterTransition(this);
    }

    @OnClick(R.id.button_head)
    public void onClickButtonHead() {
        head.setLoadEnd();
    }

    @OnClick(R.id.button_foot)
    public void onClickButtonFoot() {
        foot.setLoadEnd();
    }

    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
        recycle.scheduleLayoutAnimation();
    }
}
