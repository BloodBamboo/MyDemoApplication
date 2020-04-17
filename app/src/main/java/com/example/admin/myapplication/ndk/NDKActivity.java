package com.example.admin.myapplication.ndk;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.admin.myapplication.R;
import com.example.admin.myapplication.Utils.AppUtile;
import com.example.admin.myapplication.Utils.Constant;
import com.example.admin.myapplication.Utils.ToastUtil;

import java.io.File;
import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;


/**
 * Created by Administrator on 2017/8/16.
 * ndk中的对象不能混淆
 */

public class NDKActivity extends AppCompatActivity {

    private static String SD_CARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    NDKTest ndkTest;
    String path = SD_CARD_PATH + File.separatorChar + "JavaScript设计模式.pdf";
    String pattern_Path = SD_CARD_PATH + File.separatorChar + "JavaScript设计模式_%d.pdf";
    String path1 = SD_CARD_PATH + File.separatorChar + "JavaScript设计模式1.pdf";
    NDKFFmpegTest fmpegTest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ndk);
        ndkTest = new NDKTest();
        fmpegTest = new NDKFFmpegTest();
        Log.i("TEST_JNI", "onCreate: " + ndkTest.stringFromJNI(3, 2));
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        ndkTest.onDestroy();
        ndkTest = null;
        super.onDestroy();
    }

    @RequiresPermission(allOf = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void diff(View v) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    10011);
        } else {
            NDKTest.diff(path, pattern_Path, 5);
        }
    }

    @RequiresPermission(allOf = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void path(View v) {
        PermissionGen.with(this)
                .addRequestCode(100)
                .permissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request();
    }

    @PermissionSuccess(requestCode = 10011)
    public void a_diff() {
        NDKTest.diff(path, pattern_Path, 5);
    }

    @PermissionSuccess(requestCode = 100)
    public void a_path() {
        NDKTest.patch(path1, pattern_Path, 5);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case 10011:
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    // permission was granted, yay! Do the
//                    // contacts-related task you need to do.
//
//                } else {
//
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                }
//
//                return;
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @OnClick(R.id.button_test1)
    public void test1(View view) {
        Student s = new Student();
        ndkTest.student(s);
        Log.i(Constant.TAG, s.toString());
    }

    @OnClick(R.id.button_test2)
    public void test2(View view) {
        Person p = ndkTest.person();
        Log.i(Constant.TAG, "java____test2: " + p.student.toString());
    }

    @OnClick(R.id.button_test3)
    public void test3(View view) {
        ndkTest.initStudent();
    }

    @OnClick(R.id.button_test4)
    public void test4(View view) {
        ndkTest.deleteStudent();
    }

    @OnClick(R.id.button_test5)
    public void test5(View view) {
        ndkTest.threadTest();
    }

    @OnClick(R.id.button_test6)
    public void test6(View view) {
        ndkTest.ffmepg();
    }

    @OnClick(R.id.button_test7)
    public void test7(View view) {
        String path = AppUtile.getInnerSDCardPath() +"/test.pcm";
        String targpath = AppUtile.getInnerSDCardPath() +"/test.mp3";
        File f  = new File(path);
        if (f.exists()) {
            //采样率如果是44100转换出的声音就会变快，原理暂时不知道(问题原因可能是转换逻辑是按双声道进行的转换，录制是单声道，所以使用22050转换就正常了) 比特率=采样率 * 采样位数 * 通道数
            fmpegTest.init(path, 1, 64, 22050, targpath);
            ToastUtil.showShort(getBaseContext(), "转换完成");
        } else {
            ToastUtil.showShort(getBaseContext(), "不存在");
        }
//        String targpath = AppUtile.getInnerSDCardPath() +"/test.mp4";
//        File dataFile = new File(targpath);
//        if (dataFile.exists()) {
//            ToastUtil.showShort(getBaseContext(), "存在");
//        } else {
//            ToastUtil.showShort(getBaseContext(), "不存在");
//        }
//        fmpegTest.ffmpegLoadPath(targpath);

    }
}
