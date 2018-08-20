package com.example.admin.myapplication.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.admin.myapplication.Utils.ToastUtil;

/**
 * @author Administrator
 */
public class FirstReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int limit = intent.getIntExtra("limit", -1001);
        if (limit == 3) {
            String msg = intent.getStringExtra("msg");
            ToastUtil.showShort(context, msg);
            abortBroadcast();
        } else {
            Bundle b = new Bundle();
            b.putString("new", "Message form FirstReceiver");
            setResultExtras(b);
        }
    }
}
