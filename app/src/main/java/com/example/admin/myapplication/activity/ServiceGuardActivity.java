package com.example.admin.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.admin.myapplication.service.LocalService;
import com.example.admin.myapplication.service.RemoteService;

/**
 * Created by admin on 2017/2/22.
 */
public class ServiceGuardActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, LocalService.class));
        startService(new Intent(this, RemoteService.class));
    }
}
