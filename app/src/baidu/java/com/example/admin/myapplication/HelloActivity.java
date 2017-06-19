package com.example.admin.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Administrator on 2017/6/19.
 */

public class HelloActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hello_baidu_activity);
    }

    @Keep
    public void onClick(View v) {
        startActivity(new Intent(this, MainActivity.class).putExtra("pass", true));
    }
}
