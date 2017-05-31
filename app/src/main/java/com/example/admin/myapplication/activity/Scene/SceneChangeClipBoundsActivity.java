package com.example.admin.myapplication.activity.Scene;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeClipBounds;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.admin.myapplication.R;

/**
 * Created by Administrator on 2017/5/31.
 */

public class SceneChangeClipBoundsActivity extends BaseSceneActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_change_bounds);
        ViewGroup sceneRoot= (ViewGroup) findViewById(R.id.scene_root);
        ImageView imageView1 = new ImageView(this);
        ImageView imageView2 = new ImageView(this);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(600, 600);
        lp.gravity= Gravity.CENTER;
        imageView1.setLayoutParams(lp);
        imageView2.setLayoutParams(lp);

        imageView1.setId(R.id.image1);
        imageView2.setId(R.id.image1);

        imageView1.setImageResource(R.mipmap.cutegirl);
        imageView2.setImageResource(R.mipmap.cutegirl);

        scene1 = new Scene(sceneRoot, imageView1);
        scene2 = new Scene(sceneRoot, imageView2);

        imageView1.setClipBounds(new Rect(0,0,200,200));
        imageView2.setClipBounds(new Rect(200,200,400,400));

        TransitionManager.go(scene1);
    }

    @Override
    Transition getTransition() {
        return new ChangeClipBounds();
    }
}
