package com.example.admin.myapplication.activity.Scene;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.transition.ChangeTransform;
import android.transition.Transition;

import com.example.admin.myapplication.R;

/**
 * Created by Administrator on 2017/5/31.
 */

public class SceneChangeTransformActivity extends BaseSceneActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_change_transform);
        initScene(R.id.scene_root,R.layout.scene_1_changetransform,R.layout.scene_2_changetransform);
    }

    @Override
    Transition getTransition() {
        return new ChangeTransform();
    }
}
