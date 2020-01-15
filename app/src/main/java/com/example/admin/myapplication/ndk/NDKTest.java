package com.example.admin.myapplication.ndk;

import android.os.Looper;
import android.util.Log;

import com.example.admin.myapplication.Utils.Constant;

/**
 * Created by Administrator on 2017/8/15.
 */

public class NDKTest {

    //动态库需要手动引用，静态库会绑定指定的库，所以不用引用（测试不引用也没报错，但是还是引用安全稳定）
    static {
//        System.loadLibrary("add");
        System.loadLibrary("native-lib");
        System.loadLibrary("test-lib");
    }

    public native int stringFromJNI(int a, int b);
    public static native void diff(String path, String pattern_Path, int file_num);
    public static native void patch(String merger_path, String pattern_Path, int file_num);

    public native int student(Student student);
    public native void initStudent();
    public native void deleteStudent();
    public native void threadTest();
    public native void onDestroy();
    public native void ffmepg();
    public native Person person();

    public void upDataUI() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Log.i(Constant.TAG, "NDKTest_upDataUI_MainLooper: "+Thread.currentThread().getName());
        } else {
            Log.i(Constant.TAG, "NDKTest_upDataUI_thread: "+Thread.currentThread().getName());
        }
    }
}
