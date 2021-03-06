package com.example.admin.myapplication.activity.Scene;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.admin.myapplication.R;

/**
 * Created by Administrator on 2017/5/31.
 */

public class SceneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene);
    }

    public void changeBounds(View view) {
        startActivity(new Intent(this, SceneChangeBoundsActivity.class));
    }

    public void changeTransform(View view){
        startActivity(new Intent(this, SceneChangeTransformActivity.class));
    }

    public void changeClipBounds(View view){
        startActivity(new Intent(this, SceneChangeClipBoundsActivity.class));
    }
    public void changeImageTransform(View view){
        startActivity(new Intent(this, SceneChangeImageTransformActivity.class));
    }
    public void fade(View view){
        startActivity(new Intent(this, SceneFadeSlideExplodeActivity.class));
    }
}
