package com.example.admin.myapplication.activity.startModel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.admin.myapplication.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class A extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);
        ButterKnife.bind(this);
        Log.e("====","A被启动");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e("====","onNewIntent_A被启动");
    }

    @OnClick(R.id.button1)
    public void start() {
        startActivity(new Intent(this,  B.class));
    }

    @OnClick(R.id.button2)
    public void start2() {
        startActivity(new Intent(this,  D.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("====","A_onResume");
    }
}
