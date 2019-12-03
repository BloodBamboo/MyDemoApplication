package com.example.admin.myapplication.activity;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;

import com.example.admin.myapplication.R;
import com.example.admin.myapplication.Utils.ToastUtil;
import com.example.admin.myapplication.Utils.TwoTuple;

import java.util.ArrayList;
import java.util.List;

/**
 * 相机练习
 * @author yw
 */
public class Camera2Activity extends AppCompatActivity {


    private int REQUEST_PERMISSION_CODE = 1001;
    private String[] REQUIRED_PERMISSIONS = {android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private CameraManager cameraManager;
    private String[] cameraIds;
    private List<TwoTuple<String, CameraCharacteristics>> frontCamera = new ArrayList<>();
    private List<TwoTuple<String, CameraCharacteristics>> backCamera = new ArrayList<>();
    private CameraStateCallback cameraStateCallback;
    private TextureView cameraPreview;
    //预览Surface
    private Surface previewSurface;
    //TextureView SurfaceTexture
    private SurfaceTexture previewSurfaceTexture;


    private static Handler cameraHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.e("====", "msg==" + msg.what);
            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2);
        if (checkRequiredPermissions()) {
            initCamera();
        }
        findViewById(R.id.text_camera).setOnClickListener(v -> {

        });

        findViewById(R.id.text_close_camera).setOnClickListener(v -> {
            closeCamera();
        });
        findViewById(R.id.text_change_camera).setOnClickListener(v -> {

        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        closeCamera();
    }

    /**
     * 判断我们需要的权限是否被授予，只要有一个没有授权，我们都会返回 false，并且进行权限申请操作。
     *
     * @return true 权限都被授权
     */
    private boolean checkRequiredPermissions() {
        List<String> deniedPermissions = new ArrayList<>();
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
                deniedPermissions.add(permission);
            }
        }
        if (!deniedPermissions.isEmpty()) {
            requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), REQUEST_PERMISSION_CODE);
        }
        return deniedPermissions.isEmpty();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            boolean result = true;
            for (int i : grantResults) {
                if (i != PackageManager.PERMISSION_GRANTED) {
                    result = false;
                }
            }

            if (result) {
                //获取权限
                initCamera();
                cameraPreview = findViewById(R.id.texture_view);
                cameraPreview.setSurfaceTextureListener(new PreviewSurfaceTextureListener());


            } else {
                //获取权限失败
                ToastUtil.showShort(this, "请通过全部权限");
            }
        } else  {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * 初始化相机
     */
    private void initCamera() {
        cameraManager = getSystemService(CameraManager.class);
        try {
            cameraIds = cameraManager.getCameraIdList();
            checkCamera4Level(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY);
            if (frontCamera.isEmpty()) {
                openCamera(backCamera.get(0).one, backCamera.get(0).two);
            } else  {
                openCamera(frontCamera.get(0).one, backCamera.get(0).two);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
            ToastUtil.showShort(this, "相机访问异常");
        }
    }

    /**
     * 开启相机,传参是前置还是后置
     */
    @SuppressLint("MissingPermission")
    private void openCamera(String cameraId, CameraCharacteristics cameraCharacteristics) {
        cameraStateCallback = new CameraStateCallback();
        try {
            cameraManager.openCamera(cameraId, cameraStateCallback, cameraHandler);
            Size size = getOptimalSize(cameraCharacteristics, SurfaceTexture.class, 1440, 1080);
            previewSurfaceTexture.setDefaultBufferSize(size.getWidth(), size.getHeight());
            previewSurface = new Surface(previewSurfaceTexture);



        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取对应格式的尺寸
     */
    private Size getOptimalSize(CameraCharacteristics cameraCharacteristics, Class clazz , int maxWidth, int maxHeight) {
        float aspectRatio = maxWidth * 1.0F / maxHeight;
        StreamConfigurationMap streamConfigurationMap = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        Size[] supportedSizes = streamConfigurationMap.getOutputSizes(clazz);
        if (supportedSizes != null) {
            for (Size size : supportedSizes) {
                if (size.getWidth()* 1.0F / size.getHeight() == aspectRatio && size.getHeight() <= maxHeight && size.getWidth() <= maxWidth) {
                    return size;
                }
            }
        }
        return null;
    }

    /**
     * 关闭相机
     */
    private void closeCamera() {
        if (cameraStateCallback != null) {
            cameraStateCallback.camera.close();
            Log.e("====", "closeCamera");
        }
    }

    /**
     * 检查相机级别，并按类别分出前后相机
     *
     * @param requiredLevel 相机级别
     */
    private void checkCamera4Level(int requiredLevel) {
        //相机性能级别
//        int[] sortedLevels = {
//                CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY,
//                CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED,
//                CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_FULL,
//                CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_3
//        };

        for (String id : cameraIds) {
            try {
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(id);
                int deviceLevel = cameraCharacteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
                int facing = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
                if (requiredLevel == deviceLevel) {
                    if (facing == CameraCharacteristics.LENS_FACING_FRONT) {
                        frontCamera.add(new TwoTuple(id, cameraCharacteristics));
                    } else if (facing == CameraCharacteristics.LENS_FACING_BACK) {
                        backCamera.add(new TwoTuple(id, cameraCharacteristics));
                    }
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    private class PreviewSurfaceTextureListener implements TextureView.SurfaceTextureListener {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            previewSurfaceTexture = surface;
        }
    }

    /**
     * 相机状态回调
     */
    private class CameraStateCallback extends CameraDevice.StateCallback {
        public CameraDevice camera;


        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            this.camera = camera;
            Log.e("====", "相机已开启");
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            camera.close();
            camera = null;
            Log.e("====", "相机断开连接");
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            camera.close();
            camera = null;
            Log.e("====", "相机出现异常");
        }

        @Override
        public void onClosed(@NonNull CameraDevice camera) {
            camera.close();
            camera = null;
            Log.e("====", "相机关闭");
        }
    }
}
