package com.example.admin.myapplication.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.example.admin.myapplication.R;
import com.example.admin.myapplication.adapter.SectionAdapter;
import com.example.admin.myapplication.view.ui.BetterRecyclerView;

/**
 * Created by Administrator on 2017/6/28.\
 * 斜角度滑动识别,子Recycle滑动时父Recycle可以识别滑动
 */

public class RecycleViewActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view_better);
        SectionAdapter adapter = new SectionAdapter();
        BetterRecyclerView betterRecyclerView = (BetterRecyclerView) findViewById(R.id.recycle_view1);
        betterRecyclerView.setAdapter(adapter);
        betterRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }
}
