package com.example.admin.myapplication.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;

import com.example.admin.myapplication.R;
import com.example.admin.myapplication.Utils.ToastUtil;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class MediaRecoderActivity extends AppCompatActivity {

    MediaRecorder mMediaRecorder;
    String fileName, filePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_recoder);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

//    @RequiresPermission(allOf = {
//            Manifest.permission.RECORD_AUDIO,
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void onStartRecord(View view) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //这个权限框架不好用，当有权限时好像就不会再去回调
            Log.i("startRecord", "申请权限");
            PermissionGen.with(MediaRecoderActivity.this)
                    .addRequestCode(100)
                    .permissions(
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .request();
        } else {
            startRecord();
        }
    }

    @PermissionFail(requestCode = 100)
    public void permissionFailure() {
        ToastUtil.showShort(this, "权限拒绝,无法正常使用");
    }

    @PermissionSuccess(requestCode = 100)
    public void startRecord() {
        Log.i("startRecord", "开始录制");
        runOnUiThread(() -> {
            ToastUtil.showShort(getBaseContext(), "开始录制");
        });
        if (mMediaRecorder == null)
            mMediaRecorder = new MediaRecorder();
        try {

            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置麦克风

            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            fileName = DateFormat.format("yyyyMMdd_HHmmss", Calendar.getInstance(Locale.CHINA)) + ".mp4";
            File destDir = new File(Environment.getExternalStorageDirectory() + "/test/");
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
            filePath = Environment.getExternalStorageDirectory() + "/test/" + fileName;

            mMediaRecorder.setOutputFile(filePath);
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (IllegalStateException e) {
            Log.i("failed!", e.getMessage());
        } catch (IOException e) {
            Log.i("failed!", e.getMessage());
        }
    }

    public void stopRecord(View view) {
        try {
            ToastUtil.showShort(getBaseContext(), "停止录制");
            Log.i("startRecord", "停止录制");
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;

            File file = new File(filePath);
            if (file.exists())
                file.delete();
        }
    }
}
