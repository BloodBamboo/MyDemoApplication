package com.example.admin.myapplication.activity.theme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.admin.myapplication.R;

/**
 * Created by 12 on 2017/10/12.
 */

public class ThemeActivity extends BaseThemeActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.theme);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        findViewById(R.id.button).setOnClickListener((View v) -> startActivity(new Intent(this, Theme1Activity.class)));
        initToolBar();
    }
}
