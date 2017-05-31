package com.example.admin.myapplication.activity.Scene;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.admin.myapplication.R;

/**
 * Created by Administrator on 2017/5/31.
 */

public class BeginDelayedActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean isImageBigger;
    private ImageView cuteboy, cutegirl, hxy, lly;
    private int primarySize;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin_delayed);
        initView();
    }

    private void initView() {
        cuteboy = (ImageView) findViewById(R.id.cuteboy);
        cutegirl = (ImageView) findViewById(R.id.cutegirl);
        hxy = (ImageView) findViewById(R.id.hxy);
        lly = (ImageView) findViewById(R.id.lly);
        primarySize = cuteboy.getLayoutParams().width;
        cuteboy.setOnClickListener(this);
        cutegirl.setOnClickListener(this);
        hxy.setOnClickListener(this);
        lly.setOnClickListener(this);
    }

    public void onClick(View v) {
        //start scene 是当前的scene
        TransitionManager.beginDelayedTransition((ViewGroup) findViewById(R.id.scene_root),
                TransitionInflater.from(BeginDelayedActivity.this).inflateTransition(R.transition.explode_and_changebounds));
        //next scene 此时通过代码已改变了scene statue
        changeScene(v);
    }

    private void changeScene(View v) {
        onChangeSize(v);
        changeVisibility(cuteboy, cutegirl, hxy, lly);
        v.setVisibility(View.VISIBLE);
    }

    private void changeVisibility(View... views) {
        for (View view : views) {
            view.setVisibility(view.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
        }
    }

    private void onChangeSize(View view) {
        isImageBigger = !isImageBigger;
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (isImageBigger) {
            layoutParams.width = (int) (1.5 * primarySize);
            layoutParams.height = (int) (1.5 * primarySize);
        } else {
            layoutParams.width = primarySize;
            layoutParams.height = primarySize;
        }
        view.setLayoutParams(layoutParams);
    }
}
