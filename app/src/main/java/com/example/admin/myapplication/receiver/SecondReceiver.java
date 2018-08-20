package com.example.admin.myapplication.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.admin.myapplication.Utils.ToastUtil;

/**
 * @author Administrator
 */
public class SecondReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int limit = intent.getIntExtra("limit", -1001);
        if (limit == 2) {
            String msg = intent.getStringExtra("msg");
            Bundle bundle = getResultExtras(true);
            ToastUtil.showShort(context, msg +"\n" + bundle.getString("new"));
            abortBroadcast();
        } else {
            Bundle b = new Bundle();
            b.putString("new", "Message form SecondReceiver");
            setResultExtras(b);
        }
    }
}
