package com.example.admin.myapplication.camera2;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.Window;
import android.widget.TextView;

import com.example.admin.myapplication.R;
import com.example.admin.myapplication.Utils.AppUtile;
import com.example.admin.myapplication.Utils.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * create by yx
 * on 2020/6/1
 */
public class Camera2TestActivity extends AppCompatActivity {
    private int REQUEST_PERMISSION_CODE = 1001;
    private String[] REQUIRED_PERMISSIONS = {android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
    /**
     * ID of the current [CameraDevice].
     */
    private String backCameraId, fontCameraId;

    /**
     * An [AutoFitTextureView] for camera preview.
     */
    private AutoFitTextureView textureView;

    /**
     * A [CameraCaptureSession] for camera preview.
     */
    private CameraCaptureSession captureSession;

    /**
     * A reference to the opened [CameraDevice].
     */
    private CameraDevice cameraDevice;

    /**
     * The [android.util.Size] of camera preview.
     */
    private Size previewSize;

    /**
     * An additional thread for running tasks that shouldn't block the UI.
     */
    private HandlerThread backgroundThread;

    /**
     * A [Handler] for running tasks in the background.
     */
    private Handler backgroundHandler;

    /**
     * An [ImageReader] that handles still image capture.
     */
    private ImageReader imageReader;

    /**
     * This is the output file for our picture.
     */
    private File file;
    /**
     * [CaptureRequest.Builder] for the camera preview
     */
    private CaptureRequest.Builder previewRequestBuilder;

    /**
     * [CaptureRequest] generated by [.previewRequestBuilder]
     */
    private CaptureRequest previewRequest;
    /**
     * Orientation of the camera sensor
     */
    private int mSensorOrientation = 0;
    /**
     * A [Semaphore] to prevent the app from exiting before closing the camera.
     */
    private Semaphore cameraOpenCloseLock = new Semaphore(1);

    /**
     * Camera state: Showing camera preview.
     */
    private int STATE_PREVIEW = 0;

    /**
     * Camera state: Waiting for the focus to be locked.
     */
    private int STATE_WAITING_LOCK = 1;

    /**
     * Camera state: Waiting for the exposure to be precapture state.
     */
    private int STATE_WAITING_PRECAPTURE = 2;

    /**
     * Camera state: Waiting for the exposure state to be something other than precapture.
     */
    private int STATE_WAITING_NON_PRECAPTURE = 3;

    /**
     * Camera state: Picture was taken.
     */
    private int STATE_PICTURE_TAKEN = 4;
    /**
     * The current state of camera state for taking pictures.
     *
     * @see .captureCallback
     */
    private int state = STATE_PREVIEW;


    private Camera2Helper helper;
    //当前测试使用此规格，默认为后置摄像头，如果是前置摄像头使用 720p的规格，这样应该能保证大部分机型正常使用
    private final int MAX_PREVIEW_WIDTH = 1920;
    private final int MAX_PREVIEW_HEIGHT = 1080;


    private boolean flashSupported = false;
    private TextView textCamera;

    private final TextureView.SurfaceTextureListener surfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            openCamera(width, height);
            Log.e("===", "onSurfaceTextureAvailable");
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            configureTransform(width, height);
            Log.e("===", "onSurfaceTextureSizeChanged");
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            Log.e("===", "onSurfaceTextureDestroyed");
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };

    private CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraOpenCloseLock.release();
            Camera2TestActivity.this.cameraDevice = camera;
            createCameraPreviewSession();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            cameraOpenCloseLock.release();
            cameraDevice.close();
            Camera2TestActivity.this.cameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            cameraOpenCloseLock.release();
            cameraDevice.close();
            cameraDevice = null;
            finish();
        }
    };

    private CameraCaptureSession.CaptureCallback captureCallback = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session,
                                        @NonNull CaptureRequest request,
                                        @NonNull CaptureResult partialResult) {
            super.onCaptureProgressed(session, request, partialResult);
            process(partialResult);
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                       @NonNull CaptureRequest request,
                                       @NonNull TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
            process(result);
        }


        private void process(CaptureResult result) {
            switch (state) {
                //STATE_PREVIEW
                case 0:
                    break;
                //STATE_WAITING_LOCK
                case 1:
                    capturePicture(result);
                    break;
                //STATE_WAITING_PRECAPTURE
                case 2: {
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null ||
                            aeState == CaptureResult.CONTROL_AE_STATE_PRECAPTURE ||
                            aeState == CaptureRequest.CONTROL_AE_STATE_FLASH_REQUIRED) {
                        state = STATE_WAITING_NON_PRECAPTURE;
                    }
                    break;
                }
                //STATE_WAITING_NON_PRECAPTURE
                case 3: {
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null || aeState != CaptureResult.CONTROL_AE_STATE_PRECAPTURE) {
                        state = STATE_PICTURE_TAKEN;
                        captureStillPicture();
                    }
                    break;
                }
                default:
                    return;
            }
        }

        private void capturePicture(CaptureResult result) {
            Integer afState = result.get(CaptureResult.CONTROL_AF_STATE);
            if (afState == null) {
                captureStillPicture();
            } else if (afState == CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED
                    || afState == CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED) {
                // CONTROL_AE_STATE can be null on some devices
                Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                if (aeState == null || aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED) {
                    state = STATE_PICTURE_TAKEN;
                    captureStillPicture();
                } else {
                    runPrecaptureSequence();
                }
            }
        }
    };

    private void runPrecaptureSequence() {
        try {
            // This is how to tell the camera to trigger.
            previewRequestBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER,
                    CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_START);
            // Tell #captureCallback to wait for the precapture sequence to be set.
            state = STATE_WAITING_PRECAPTURE;
            captureSession.capture(previewRequestBuilder.build(), captureCallback,
                    backgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    //Capture a still picture. This method should be called when we get a response in
    private void captureStillPicture() {
        try {
            if (cameraDevice == null) return;
            int rotation = this.getWindowManager().getDefaultDisplay().getRotation();
            // This is the CaptureRequest.Builder that we use to take a picture.
            CaptureRequest.Builder builder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            builder.addTarget(imageReader.getSurface());

            // Sensor orientation is 90 for most devices, or 270 for some devices (eg. Nexus 5X)
            // We have to take that into account and rotate JPEG properly.
            // For devices with orientation of 90, we return our mapping from ORIENTATIONS.
            // For devices with orientation of 270, we need to rotate the JPEG 180 degrees.
            //这里对拍照图片进行旋转以符合正确视角
            builder.set(CaptureRequest.JPEG_ORIENTATION,
                    (helper.ORIENTATIONS.get(rotation) + mSensorOrientation + 270) % 360);

            // Use the same AE and AF modes as the preview.
            builder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            setAutoFlash(builder);

            captureSession.stopRepeating();
            captureSession.abortCaptures();
            captureSession.capture(builder.build(), new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    ToastUtil.showShort(getApplicationContext(), "Saved: " + file.getPath());
                    Log.d("===", file.toString());
                    unlockFocus();
                }
            }, null);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void lockFocus() {
        try {
            // This is how to tell the camera to lock focus.
            previewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_START);
            // Tell #captureCallback to wait for the lock.
            state = STATE_WAITING_LOCK;
            captureSession.capture(previewRequestBuilder.build(), captureCallback,
                    backgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void unlockFocus() {
        try {
            // Reset the auto-focus trigger
            previewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
            setAutoFlash(previewRequestBuilder);
            captureSession.capture(previewRequestBuilder.build(), captureCallback,
                    backgroundHandler);
            // After this, the camera will go back to the normal state of preview.
            state = STATE_PREVIEW;
            captureSession.setRepeatingRequest(previewRequest, captureCallback,
                    backgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    private void createCameraPreviewSession() {
        try {
            if (previewSize == null) return;

            SurfaceTexture texture = textureView.getSurfaceTexture();
            // We configure the size of default buffer to be the size of camera preview we want.
            //我们将默认缓冲区的大小配置为我们想要的相机预览的大小。
            texture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());

            // This is the output Surface we need to start preview.
            Surface surface = new Surface(texture);
            // We set up a CaptureRequest.Builder with the output Surface.
            previewRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            previewRequestBuilder.addTarget(surface);
            List<Surface> l = new ArrayList<Surface>();
            l.add(surface);
            if (imageReader != null) {
                l.add(imageReader.getSurface());
            }
            cameraDevice.createCaptureSession(l, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    // The camera is already closed
                    if (cameraDevice == null) return;

                    // When the session is ready, we start displaying the preview.
                    captureSession = session;

                    // Auto focus should be continuous for camera preview.
                    previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                            CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);

                    // Flash is automatically enabled when necessary.
                    setAutoFlash(previewRequestBuilder);

                    // Finally, we start displaying the camera preview.
                    previewRequest = previewRequestBuilder.build();

                    try {
                        captureSession.setRepeatingRequest(previewRequest, captureCallback, backgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    ToastUtil.showShort(getApplicationContext(), "创建session失败");
                }
            }, null);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        setContentView(R.layout.activity_camera2);
        if (checkRequiredPermissions()) {
            init();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        startBackgroundThread();
//        if (textureView.isActivated()) {
        openCamera(textureView.getWidth(), textureView.getHeight());
//        } else {
//
//        }
    }

    @Override
    protected void onPause() {
        closeCamera();
        stopBackgroundThread();
        super.onPause();

    }

    private void stopBackgroundThread() {
        backgroundThread.quitSafely();
        try {
            backgroundThread.join();
            backgroundThread = null;
            backgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void closeCamera() {
        try {
            if (cameraOpenCloseLock != null) {
                cameraOpenCloseLock.acquire();
            }

            if (captureSession != null) {
                captureSession.close();
                captureSession = null;
            }
            if (cameraDevice != null) {
                cameraDevice.close();
                cameraDevice = null;
            }
            if (imageReader != null) {
                imageReader.close();
                imageReader = null;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.", e);
        } finally {
            cameraOpenCloseLock.release();
        }
    }

    private void init() {
        helper = new Camera2Helper();
        textureView = findViewById(R.id.texture_view);
        textCamera = findViewById(R.id.text_camera);
        file = new File(AppUtile.getInnerSDCardPicturesPath() + "test.jpg");
        textureView.setSurfaceTextureListener(surfaceTextureListener);
        textCamera.setOnClickListener(v -> {
            lockFocus();
        });
    }

    private void openCamera(int width, int height) {
        setUpCameraOutputs(width, height);
        configureTransform(width, height);
        CameraManager manager = (CameraManager) this.getSystemService(Context.CAMERA_SERVICE);
        try {
            // Wait for camera to open - 2.5 seconds is sufficient
            if (!cameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }
            if (!TextUtils.isEmpty(backCameraId)) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                manager.openCamera(backCameraId, stateCallback, backgroundHandler);
            }

        } catch (CameraAccessException e) {
            Log.e("===", e.toString());
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera opening.", e);
        }
    }

    private void setAutoFlash(CaptureRequest.Builder requestBuilder) {
        if (flashSupported) {
            requestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
        }
    }

    /**
     * 设置textureView图像根据显示画面进行缩放
     */
    private void configureTransform(int viewWidth, int viewHeight) {

        if (null == textureView || null == previewSize) {
            ToastUtil.showShort(this, "预览画面size获取失败");
            return;
        }
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, previewSize.getHeight(), previewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / previewSize.getHeight(),
                    (float) viewWidth / previewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180, centerX, centerY);
        }
        textureView.setTransform(matrix);
    }

    /**
     * 设置相机相关设置
     */
    private void setUpCameraOutputs(int width, int height) {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String cameraId : manager.getCameraIdList()) {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
                //获取相机方向，前置还是后置
                int cameraDirection = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (cameraDirection == CameraCharacteristics.LENS_FACING_FRONT) {
                    fontCameraId = cameraId;
                    //这里暂时跳过前置摄像头，如果不跳过就有可能使用前置摄像头参数进行了配置，但是使用后置摄像头，导致拍照出来的图有问题
                    continue;
                } else if (cameraDirection == CameraCharacteristics.LENS_FACING_BACK) {
                    backCameraId = cameraId;
                }
                //获取当前id相机可用的每种规格/尺寸集合
                StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

                if (map == null) {
                    continue;
                }

                if (map.isOutputSupportedFor(ImageFormat.JPEG)) {
                    Size size = helper.getOptimalSize(map.getOutputSizes(ImageFormat.JPEG), MAX_PREVIEW_WIDTH, MAX_PREVIEW_HEIGHT);
                    if (size == null) {
                        continue;
                    }
                    //创建拍照imageReader实例
                    imageReader = ImageReader.newInstance(size.getWidth(), size.getHeight(), ImageFormat.JPEG, 3);
                    imageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
                        @Override
                        public void onImageAvailable(ImageReader reader) {
                            //保存照片到指定file
                            backgroundHandler.post(new Camera2Helper.ImageSaver(reader.acquireNextImage(), file));
                        }
                    }, backgroundHandler);

                    // Find out if we need to swap dimension to get the preview size relative to sensor
                    // coordinate.
                    int displayRotation = getWindowManager().getDefaultDisplay().getRotation();
                    //noinspection ConstantConditions
                    mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
                    boolean swappedDimensions = false;
                    switch (displayRotation) {
                        case Surface.ROTATION_0:
                        case Surface.ROTATION_180:
                            if (mSensorOrientation == 90 || mSensorOrientation == 270) {
                                swappedDimensions = true;
                            }
                            break;
                        case Surface.ROTATION_90:
                        case Surface.ROTATION_270:
                            if (mSensorOrientation == 0 || mSensorOrientation == 180) {
                                swappedDimensions = true;
                            }
                            break;
                        default:
                            Log.e("====", "Display rotation is invalid: " + displayRotation);
                    }

                    Point displaySize = new Point();
                    getWindowManager().getDefaultDisplay().getSize(displaySize);
                    int rotatedPreviewWidth = width;
                    int rotatedPreviewHeight = height;
                    int maxPreviewWidth = displaySize.x;
                    int maxPreviewHeight = displaySize.y;

                    if (swappedDimensions) {
                        rotatedPreviewWidth = height;
                        rotatedPreviewHeight = width;
                        maxPreviewWidth = displaySize.y;
                        maxPreviewHeight = displaySize.x;
                    }

                    if (maxPreviewWidth > MAX_PREVIEW_WIDTH) {
                        maxPreviewWidth = MAX_PREVIEW_WIDTH;
                    }

                    if (maxPreviewHeight > MAX_PREVIEW_HEIGHT) {
                        maxPreviewHeight = MAX_PREVIEW_HEIGHT;
                    }

                    // Danger, W.R.! Attempting to use too large a preview size could  exceed the camera
                    // bus' bandwidth limitation, resulting in gorgeous previews but the storage of
                    // garbage capture data.
                    previewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
                            rotatedPreviewWidth, rotatedPreviewHeight, maxPreviewWidth,
                            maxPreviewHeight, size);

                    // We fit the aspect ratio of TextureView to the size of preview we picked.
                    int orientation = getResources().getConfiguration().orientation;
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        textureView.setAspectRatio(
                                previewSize.getWidth(), previewSize.getHeight());
                    } else {
                        textureView.setAspectRatio(previewSize.getHeight(), previewSize.getWidth());
                    }

                    // Check if the flash is supported.
                    flashSupported = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showShort(this, "相机相关设置出错");
        }
    }

    /**
     * Given {@code choices} of {@code Size}s supported by a camera, choose the smallest one that
     * is at least as large as the respective texture view size, and that is at most as large as the
     * respective max size, and whose aspect ratio matches with the specified value. If such size
     * doesn't exist, choose the largest one that is at most as large as the respective max size,
     * and whose aspect ratio matches with the specified value.
     *
     * @param choices           The list of sizes that the camera supports for the intended output
     *                          class
     * @param textureViewWidth  The width of the texture view relative to sensor coordinate
     * @param textureViewHeight The height of the texture view relative to sensor coordinate
     * @param maxWidth          The maximum width that can be chosen
     * @param maxHeight         The maximum height that can be chosen
     * @param aspectRatio       The aspect ratio
     * @return The optimal {@code Size}, or an arbitrary one if none were big enough
     */
    private static Size chooseOptimalSize(Size[] choices, int textureViewWidth,
                                          int textureViewHeight, int maxWidth, int maxHeight, Size aspectRatio) {

        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<>();
        // Collect the supported resolutions that are smaller than the preview Surface
        List<Size> notBigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getWidth() <= maxWidth && option.getHeight() <= maxHeight &&
                    option.getHeight() == option.getWidth() * h / w) {
                if (option.getWidth() >= textureViewWidth &&
                        option.getHeight() >= textureViewHeight) {
                    bigEnough.add(option);
                } else {
                    notBigEnough.add(option);
                }
            }
        }

        // Pick the smallest of those big enough. If there is no one big enough, pick the
        // largest of those not big enough.
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new Camera2Helper.CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {
            return Collections.max(notBigEnough, new Camera2Helper.CompareSizesByArea());
        } else {
            Log.e("===", "Couldn't find any suitable preview size");
            return choices[0];
        }
    }

    /**
     * Starts a background thread and its [Handler].
     */
    private void startBackgroundThread() {
        backgroundThread = new HandlerThread("CameraBackground");
        backgroundThread.start();
        backgroundHandler = new Handler(backgroundThread.getLooper());
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
                init();
            } else {
                //获取权限失败
                ToastUtil.showShort(this, "请通过全部权限");
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
