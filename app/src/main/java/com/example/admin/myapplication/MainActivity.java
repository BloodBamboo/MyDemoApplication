package com.example.admin.myapplication;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.example.admin.myapplication.activity.LeadPagerActivity;
import com.example.admin.myapplication.activity.ListPopAnimationActivity;
import com.example.admin.myapplication.activity.MapActivity;
import com.example.admin.myapplication.activity.RevealSearchViewActivity;
import com.example.admin.myapplication.activity.ScrollingActivity;
import com.example.admin.myapplication.activity.SearchViewActivity;
import com.example.admin.myapplication.activity.ServiceGuardActivity;
import com.example.admin.myapplication.activity.ShaderViewActivity;
import com.example.admin.myapplication.activity.TouchEventTransmitActivity;
import com.example.admin.myapplication.activity.TransitionAnimationActivity;
import com.example.admin.myapplication.activity.WaterActivity;
import com.example.admin.myapplication.activity.old.OldScrollingActivity;
import com.example.admin.myapplication.activity.AnimationActivity;

import java.util.Map;

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

    @RequiresPermission(Manifest.permission.WRITE_CONTACTS)
    @OnClick(R.id.button_old_demo)
    public void onClickOldDemo() {
        startActivity(new Intent(this, OldScrollingActivity.class));
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

    @OnClick(R.id.button_view_reveal_search)
    public void onClickRevealSearch() {
        startActivity(new Intent(this, RevealSearchViewActivity.class));
    }
    @OnClick(R.id.button_view_water)
    public void onClickWater() {
        startActivity(new Intent(this, WaterActivity.class));
    }

    @OnClick(R.id.button_search_view)
    public void onClickSearchView() {
        startActivity(new Intent(this, SearchViewActivity.class));
    }

    @OnClick(R.id.button_transition_anim)
    public void onClickTransitionAnim() {
        startActivity(new Intent(this, TransitionAnimationActivity.class));
    }

    @OnClick(R.id.button_map)
    public void onClickMap() {
        startActivity(new Intent(this, MapActivity.class));
    }
}
