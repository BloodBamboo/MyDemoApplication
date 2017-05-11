package com.example.admin.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.example.admin.myapplication.activity.LeadPagerActivity;
import com.example.admin.myapplication.activity.ListPopAnimationActivity;
import com.example.admin.myapplication.activity.ScrollingActivity;
import com.example.admin.myapplication.activity.ServiceGuardActivity;
import com.example.admin.myapplication.activity.ShaderViewActivity;
import com.example.admin.myapplication.activity.TouchEventTransmitActivity;
import com.example.admin.myapplication.anime.AnimationActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by admin on 2017/2/6.
 */

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.nav)
    public NavigationView nav;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainlayout);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nav.setCheckedItem(R.id.call);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @OnClick(R.id.button_retrofit)
    public void onClickRetrofit() {
        startActivity(new Intent(this, ScrollingActivity.class));
    }

    @OnClick(R.id.button_animation_path)
    public void onClickAnimationPath() {
        startActivity(new Intent(this, AnimationActivity.class));
    }

    @OnClick(R.id.button_list_pop_animation)
    public void onClickListPopAnimation() {
        startActivity(new Intent(this, ListPopAnimationActivity.class));
    }

    @OnClick(R.id.button_lead_pager)
    public void onClick() {
        startActivity(new Intent(this, LeadPagerActivity.class));
    }

    @OnClick(R.id.button_service_guard)
    public void onClickServiceGuard() {
        startActivity(new Intent(this, ServiceGuardActivity.class));
    }
    @OnClick(R.id.button_view_touch_event_transmit)
    public void onClickTouchTransmit() {
        startActivity(new Intent(this, TouchEventTransmitActivity.class));
    }

    @OnClick(R.id.button_view_Shader_demo)
    public void onClickShader() {
        startActivity(new Intent(this, ShaderViewActivity.class));
    }
}
