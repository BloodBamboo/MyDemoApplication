package com.example.admin.myapplication.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.myapplication.R;
import com.example.admin.myapplication.anime.BoundMenu;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by admin on 2017/2/8.
 */
public class ListPopAnimationActivity extends AppCompatActivity {

    @BindView(R.id.layout1)
    public LinearLayout linearLayout;

    private ArrayList<String> mDatas = new ArrayList<>();
    private HomeAdapter mAdapter;
    private RecyclerView mRecyclerView;
    BoundMenu menu;

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv;

        public MyViewHolder(View view)
        {
            super(view);
            tv = (TextView) view.findViewById(R.id.text);
        }
    }

    public class HomeAdapter extends RecyclerView.Adapter<MyViewHolder>
    {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    ListPopAnimationActivity.this).inflate(R.layout.item_home, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position)
        {
            holder.tv.setText(mDatas.get(position));
        }

        @Override
        public int getItemCount()
        {
            return mDatas.size();
        }

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_pop_animation);
        ButterKnife.bind(this);
        mRecyclerView = new RecyclerView(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setVisibility(View.GONE);
        mRecyclerView.setAdapter(new HomeAdapter());
        Animation animation= AnimationUtils.loadAnimation(this, R.anim.slide_right);

        //得到一个LayoutAnimationController对象；

        LayoutAnimationController lac= new LayoutAnimationController(animation);

        //设置控件显示的顺序；

        lac.setOrder(LayoutAnimationController.ORDER_REVERSE);

        //设置控件显示间隔时间；

        lac.setDelay(1);

        //为ListView设置LayoutAnimationController属性；

        mRecyclerView.setLayoutAnimation(lac);
        for (int i = 0; i < 20; i++) {
            mDatas.add(i + "");
        }
    }

    @OnClick(R.id.button1)
    public void onClick() {

        if (menu == null) {
            menu = new BoundMenu().makeMenu(linearLayout, mRecyclerView, new BoundMenu.BoundMenuListener() {
                @Override
                public void showContent() {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mRecyclerView.scheduleLayoutAnimation();
                }

                @Override
                public void downContent() {
                    mRecyclerView.setVisibility(View.GONE);
                }
            }).show();
        } else {
            menu.down();
            menu = null;
        }
    }
}
