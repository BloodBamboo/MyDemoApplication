package com.example.admin.myapplication.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.admin.myapplication.R;
import com.example.admin.myapplication.anime.AnimationPath;
import com.example.admin.myapplication.anime.AnimeTypeEvaluator;
import com.example.admin.myapplication.anime.PathPoint;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 2017/2/6.
 */

public class AnimationActivity extends AppCompatActivity {

    int time = 1700;

    @BindView(R.id.imageView)
    ImageView imageView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation_path_layout);
        ButterKnife.bind(this);

        imageView.setOnClickListener((View view) -> {
            Log.e("=========", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                AnimationPath path = new AnimationPath();

                path.moveTo(50, 200);
//                path.lineTo(100, 200);
//                path.lineTo(250, 350);

                path.curveTo(600, 200, 300, 0, 350, 500);

                ObjectAnimator objectAnimator = ObjectAnimator.ofObject(AnimationActivity.this, "path", new AnimeTypeEvaluator(), path.getList().toArray());
                objectAnimator.setDuration(time);
                objectAnimator.start();
        });
    }

    @Keep
    public void setPath(PathPoint pathPoint) {
        Log.e("=========", "bbbbbbbbbbbbbbbbbbbbbbbbbbb");
        imageView.setTranslationX(pathPoint.getX());
        imageView.setTranslationY(pathPoint.getY());
    }
}
