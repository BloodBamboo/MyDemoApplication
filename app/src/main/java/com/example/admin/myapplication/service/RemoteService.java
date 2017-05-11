package com.example.admin.myapplication.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.annotation.Nullable;

import com.example.admin.myapplication.IMyAidlInterface;
import com.example.admin.myapplication.R;

/**
 * Created by admin on 2017/2/22.
 */

public class RemoteService extends Service {
    private MyBinder binder;
    private MyServiceConnection connection;
    private PendingIntent pendingIntent;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (binder == null) {
            binder = new MyBinder();
        }

        connection = new MyServiceConnection();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //连接本地服务
        this.bindService(new Intent(this, LocalService.class), connection, Context.BIND_IMPORTANT);
        //提高优先级
        pendingIntent = PendingIntent.getService(this, 0, intent, 0);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker("显示第一个通知1");
        builder.setContentTitle("安全服务启动中1");
        builder.setContentText("每天进步一点点1");
        builder.setWhen(System.currentTimeMillis()); //发送时间
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.getNotification();
        startForeground(startId, notification);
        return START_STICKY;
    }

    class MyBinder extends IMyAidlInterface.Stub {

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }
    }

    class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            //说明远程服务挂了需启动
            RemoteService.this.startService(new Intent(RemoteService.this, LocalService.class));
            //连接远端服务
            RemoteService.this.bindService(new Intent(RemoteService.this, LocalService.class), connection, Context.BIND_IMPORTANT);
        }
    }
}
