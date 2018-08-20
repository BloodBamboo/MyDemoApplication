package com.example.admin.myapplication.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


public class Receiver_1 extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String key = intent.getStringExtra("key");
        Toast.makeText(context, key+"成功", Toast.LENGTH_SHORT).show();
        Log.e("Receiver_1", key);
    }
}
