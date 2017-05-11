package com.example.admin.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.admin.myapplication.adapter.LeadPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 2017/2/12.
 */

public class LeadPagerActivity extends AppCompatActivity {

    @BindView(R.id.view_pager)
    ViewPager view_pager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_lead_pager);
        ButterKnife.bind(this);
        view_pager.setAdapter(new LeadPagerAdapter(getSupportFragmentManager()));
    }
}
