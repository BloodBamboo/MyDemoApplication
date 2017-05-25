package com.example.admin.myapplication.activity.old;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.afollestad.appthemeengine.ATE;
import com.afollestad.appthemeengine.Config;
import com.example.admin.myapplication.R;
import com.example.admin.myapplication.view.old.CircleProgressBar;

public class OLdScrollingActivity extends AppCompatActivity {

    private long updateTime = -1;
    float progress = 0;

    // Again, this method will become useful later
    @Nullable
    public String getATEKey() {
        return null;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ATE.preApply(this, getATEKey());
        super.onCreate(savedInstanceState);
        // Sets the startup time to check for value changes later
        updateTime = System.currentTimeMillis();
        ATE.config(this, "light_theme")
                .activityTheme(R.style.AppTheme)
                .primaryColorRes(R.color.colorPrimaryLightDefault)
                .accentColorRes(R.color.colorAccentLightDefault)
                .coloredNavigationBar(false)
                .navigationViewSelectedIconRes(R.color.colorAccentLightDefault)
                .navigationViewSelectedTextRes(R.color.colorAccentLightDefault)
                .commit();

        setContentView(R.layout.activity_old_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.appbar_toolbar);
        toolbar.setBackgroundColor(Config.accentColor(this, null));
//        setSupportActionBar(toolbar);

//        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
//
//        collapsingToolbarLayout.setTitle("abc123");
//        collapsingToolbarLayout.setExpandedTitleGravity(Gravity.BOTTOM|Gravity.RIGHT);
//        collapsingToolbarLayout.setExpandedTitleColor(Color.DKGRAY);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            Window window = getWindow();//获取window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }

        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener((v) -> {
//让新的Activity从一个小的范围扩大到全屏
            ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(OLdScrollingActivity.this, R.anim.slide_bottom_in, R.anim.slide_bottom_out);
            Intent intent = new Intent(OLdScrollingActivity.this, LayoutManagerActivity.class);
            ActivityCompat.startActivity(OLdScrollingActivity.this, intent, options.toBundle());
//            startActivity(new Intent(OLdScrollingActivity.this, LayoutManagerActivity.class));
        });

        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener((v) -> scaleUpAnimation(oneActivity.class, v));

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener((v) -> scaleUpAnimation(CoordinatorLayoutActivity.class, v));

        Button button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener((v) -> scaleUpAnimation(ConstraintActivity.class, v));
        OnClick(R.id.button5, OldLeadPagerActivity.class);
        OnClick(R.id.button6, SettingsActivity.class);
        OnClick(R.id.button7, DataBindingActivity.class);

        CircleProgressBar bar = (CircleProgressBar) findViewById(R.id.progressBar);
        bar.setMax(100);
        bar.setProgress(50);

        bar.setOnClickListener((View v) ->
            new Thread(() -> {
                while (progress <= 100) {
                    progress += 2;
                    bar.setProgress(progress);

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start()
        );
    }


    private void OnClick(int res, final Class clazz) {
        Button button = (Button) findViewById(res);
        button.setBackgroundColor(Config.accentColor(this, null));
        button.setOnClickListener((v) -> scaleUpAnimation(clazz, v));
    }

    private void scaleUpAnimation(Class clasz, View view) {
        //让新的Activity从一个小的范围扩大到全屏
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeScaleUpAnimation(view, //The View that the new activity is animating from
                        (int) view.getWidth() / 2,
                        (int) view.getHeight() / 2, //拉伸开始的坐标
                        0,
                        0);//拉伸开始的区域大小，这里用（0，0）表示从无到全屏
        startNewAcitivity(clasz, options);
    }

    private void startNewAcitivity(Class clasz, ActivityOptionsCompat options) {
        Intent intent = new Intent(this, clasz);
        ActivityCompat.startActivity(this, intent, options.toBundle());
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Performs post-inflation theming
        ATE.postApply(this, getATEKey());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Checks if values have changed since the Activity was previously paused.
        // Causes Activity recreation if necessary.
        ATE.invalidateActivity(this, updateTime, getATEKey());
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Cleans up resources if the Activity is finishing
        if (isFinishing())
            ATE.cleanup();
    }
}
