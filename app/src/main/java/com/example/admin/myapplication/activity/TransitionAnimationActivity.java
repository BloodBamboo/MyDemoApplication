package com.example.admin.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.admin.myapplication.R;
import com.example.admin.myapplication.activity.Scene.BeginDelayedActivity;
import com.example.admin.myapplication.activity.Scene.SceneActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/5/31.
 */

public class TransitionAnimationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button1)
    public void OnClick1() {
        startActivity(new Intent(this, SceneActivity.class));
    }

    @OnClick(R.id.button2)
    public void OnClick2() {
        startActivity(new Intent(this, BeginDelayedActivity.class));
    }
}
