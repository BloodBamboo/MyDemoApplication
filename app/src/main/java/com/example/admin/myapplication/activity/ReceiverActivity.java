package com.example.admin.myapplication.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.admin.myapplication.R;
import com.example.admin.myapplication.receiver.FirstReceiver;

/**
 * @author Administrator
 */
public class ReceiverActivity extends AppCompatActivity {
    FirstReceiver r;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

//        r = new FirstReceiver();
//        registerReceiver(r, new IntentFilter("com.example.admin.myapplication.ORDER_BROADCAST"));

        findViewById(R.id.button_send_broadcast).setOnClickListener(v -> {
            Intent i = new Intent();
            i.setAction("com.example.admin.myapplication.ORDER_BROADCAST");
            i.setPackage("com.example.admin.myapplication");
            i.putExtra("limit", 1);
            i.putExtra("msg", "Message from ReceiverActivity");
            sendOrderedBroadcast(i, null);
        });
        findViewById(R.id.button_action1).setOnClickListener(v -> {
            Intent intent = new Intent("ttt");
            intent.setPackage("com.example.admin.myapplication");
            intent.putExtra("key", "静态注册");
            sendBroadcast(intent);//发送广播
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(r);
    }
}
