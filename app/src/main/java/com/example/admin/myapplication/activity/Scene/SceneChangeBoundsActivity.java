package com.example.admin.myapplication.activity.Scene;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.transition.ChangeBounds;
import android.support.transition.Transition;

import com.example.admin.myapplication.R;

/**
 * Created by Administrator on 2017/5/31.
 */

public class SceneChangeBoundsActivity extends BaseSceneActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_change_bounds);
        initScene(R.id.scene_root, R.layout.scene_1_changebounds, R.layout.scene_2_changebounds);
    }

    @Override
    Transition getTransition() {
        return new ChangeBounds();
    }
}
