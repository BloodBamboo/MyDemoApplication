package com.example.admin.myapplication.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.admin.myapplication.R;
import com.example.admin.myapplication.view.ui.LoveLayout;

/**
 * Created by Administrator on 2017/6/15.
 */

public class LoveBezierActivity extends AppCompatActivity{
    LoveLayout loveLayout;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_bezier);
        loveLayout = (LoveLayout) findViewById(R.id.iv_love);
    }

    public void onClick(View view) {
        loveLayout.addLoveIcon();
    }
}
