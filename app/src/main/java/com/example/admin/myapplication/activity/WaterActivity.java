package com.example.admin.myapplication.activity;

import android.os.Bundle;
import android.renderscript.RenderScript;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.admin.myapplication.R;
import com.example.admin.myapplication.view.ui.WaterView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/5/23.
 */

public class WaterActivity extends AppCompatActivity {

    @BindView(R.id.water1)
    public WaterView water1;

    boolean isCancel;
    Thread thread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.water1)
    public void onClcik1() {
        isCancel = false;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!isCancel) {
                        Thread.sleep(50);
                        water1.post(new Runnable() {
                            @Override
                            public void run() {
                                water1.setX();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {

                }
            }
        });
        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isCancel = true;
    }
}
