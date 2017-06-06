package com.example.admin.myapplication.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.admin.myapplication.R;

/**
 * Created by yangxin on 2017/6/1.
 */

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        findViewById(R.id.view1).setOnClickListener((View view) -> {
                Toast.makeText(MapActivity.this, "==================", Toast.LENGTH_LONG).show();
        });
    }
}
