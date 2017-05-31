package com.example.admin.myapplication.activity.Scene;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.myapplication.R;

/**
 * Created by Administrator on 2017/5/31.
 */

public abstract class BaseSceneActivity extends AppCompatActivity {
    protected Scene scene1;
    protected Scene scene2;
    private boolean isScene2;
//
//    abstract Transition getTransition();
//
//    protected void initScene(@IdRes int scene_root, @LayoutRes int scene_1_changebounds, @LayoutRes int scene_2_changebounds) {
//        ViewGroup viewRoot = (ViewGroup) findViewById(R.id.scene_root);
//        scene1 = Scene.getSceneForLayout(viewRoot, scene_1_changebounds, this);
//        scene2 = Scene.getSceneForLayout(viewRoot, scene_2_changebounds, this);
//        TransitionManager.go(scene1);
//    }
//
//    protected void ChangeScene() {
//        TransitionManager.go(isScene2 ? scene1 : scene2, getTransition());
//        isScene2 = !isScene2;
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    abstract Transition getTransition();
    protected void switchScene(Transition transition){
        TransitionManager.go(isScene2?scene1:scene2,transition);
        isScene2=!isScene2;
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    protected void initScene(@IdRes int rootView, @LayoutRes int scene1_layout,@LayoutRes int scene2_layout) {
        ViewGroup sceneRoot= (ViewGroup) findViewById(rootView);
        scene1= Scene.getSceneForLayout(sceneRoot,scene1_layout,this);
        scene2= Scene.getSceneForLayout(sceneRoot,scene2_layout,this);
        TransitionManager.go(scene1);
    }

    public void change(View view){
        switchScene(getTransition());
    }
}
