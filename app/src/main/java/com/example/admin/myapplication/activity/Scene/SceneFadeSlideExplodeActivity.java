package com.example.admin.myapplication.activity.Scene;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.transition.Slide;
import android.transition.Transition;

import com.example.admin.myapplication.R;

/**
 * Created by Administrator on 2017/5/31.
 */

public class SceneFadeSlideExplodeActivity extends BaseSceneActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_change_bounds);
        initScene(R.id.scene_root, R.layout.scene_1_changebounds, R.layout.scene_null);
    }

    @Override
    Transition getTransition() {
        return new Slide();
    }
}
