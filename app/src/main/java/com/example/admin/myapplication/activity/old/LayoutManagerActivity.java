package com.example.admin.myapplication.activity.old;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;


import com.example.admin.myapplication.R;
import com.example.admin.myapplication.adapter.CustomLayoutManagerAdapter;
import com.example.admin.myapplication.bean.Custom;
import com.example.admin.myapplication.recycle_view.callback.CustomItemTouchHelep;
import com.example.admin.myapplication.recycle_view.layoutmanager.CardConfig;
import com.example.admin.myapplication.recycle_view.layoutmanager.OverLayCardLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/1/5.
 */

public class LayoutManagerActivity extends AppCompatActivity {

    int res[] = {R.drawable.pic1,
            R.drawable.pic2,
            R.drawable.pic3,
            R.drawable.pic4,
            R.drawable.pic5,
            R.drawable.pic6,
            R.drawable.pic7,
            R.drawable.pic8,
            R.drawable.pic9,
            R.drawable.pic10
    };

    @BindView(R.id.recycle_view)
    public RecyclerView recyclerView;


    private CustomLayoutManagerAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_layout_manager);
        ButterKnife.bind(this);
        //初始化配置
        CardConfig.initConfig(this);



        recyclerView.setLayoutManager(new OverLayCardLayoutManager());
        adapter = new CustomLayoutManagerAdapter(this, R.layout.item_swipe_card);

        recyclerView.setAdapter(adapter);
        List<Custom> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Custom custom = new Custom();
            custom.name = i + "";
            custom.image_res = res[i];
            list.add(custom);
        }
        adapter.setDatas(list);

        CustomItemTouchHelep temp = new CustomItemTouchHelep(recyclerView, adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(temp);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}
