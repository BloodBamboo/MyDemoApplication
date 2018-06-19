package com.example.admin.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.example.admin.myapplication.R;
import com.example.admin.myapplication.activity.Scene.BeginDelayedActivity;
import com.example.admin.myapplication.activity.Scene.SceneActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/5/31.
 */

public class TransitionAnimationActivity extends AppCompatActivity {

    @BindView(R.id.image)
    public ImageView image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);
        ButterKnife.bind(this);
        image.setOnClickListener(System.out::print);
    }

    @OnClick(R.id.button1)
    public void OnClick1() {
        startActivity(new Intent(this, SceneActivity.class));
    }

    @OnClick(R.id.button2)
    public void OnClick2() {
        startActivity(new Intent(this, BeginDelayedActivity.class));
    }

    @OnClick(R.id.button3)
    public void OnClick3() {
        startActivity(new Intent(this, DelayedActivity2.class));
    }

    @OnClick(R.id.button4)
    public void OnClick4() {
        Slide slide = new Slide();
        slide.setDuration(500);
        slide.setSlideEdge(Gravity.RIGHT);
        slide.excludeTarget(android.R.id.statusBarBackground, true);
        getWindow().setExitTransition(slide);
        getWindow().setReenterTransition(new Explode().setDuration(600));
        Intent intent = new Intent(this, TransitionActivity2.class);
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                new Pair<View, String>(image,"shared_image"));
        startActivity(intent, activityOptionsCompat.toBundle());
    }
}
