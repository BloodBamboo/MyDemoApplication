package com.example.admin.myapplication.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.example.admin.myapplication.R;
import com.example.admin.myapplication.fragment.ui.OneFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/6/6.
 */

public class TransitionActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_transition2);

        Slide slide = new Slide();
        slide.setDuration(700);
        slide.setSlideEdge(Gravity.LEFT);
        getWindow().setEnterTransition(slide);
        getWindow().setSharedElementEnterTransition(new ChangeBounds().setDuration(700));
        OneFragment oneFragment = new OneFragment();
        oneFragment.setExitTransition(new Slide(Gravity.LEFT));
        getSupportFragmentManager().beginTransaction().add(R.id.content, oneFragment).commit();

    }
}
