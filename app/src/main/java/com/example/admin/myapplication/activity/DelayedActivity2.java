package com.example.admin.myapplication.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.admin.myapplication.R;

/**
 * Created by Administrator on 2017/6/5.
 */

public class DelayedActivity2 extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delay2);
        ImageView circulView1 = (ImageView) findViewById(R.id.image);
        circulView1.setOnClickListener((View v) -> {
            ChangeBounds changeBounds = new ChangeBounds();
//            changeBounds.setDuration(1000);
            changeBounds.setStartDelay(1000);
            //开启延迟动画，在这里会记录当前视图树的状态
            TransitionManager.beginDelayedTransition((ViewGroup) findViewById(R.id.layout), changeBounds);
            //我们直接修改视图树中的View的属性
            ViewGroup.LayoutParams layoutParams = circulView1.getLayoutParams();
            layoutParams.height = 600;
            layoutParams.width = 600;
            circulView1.setLayoutParams(layoutParams);
        });
    }
}
