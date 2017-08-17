package com.example.admin.myapplication.ndk;

/**
 * Created by Administrator on 2017/8/15.
 */

public class NDKTest {

    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("test-lib");
    }

    public native int stringFromJNI(int a, int b);
    public static native void diff(String path, String pattern_Path, int file_num);
    public static native void patch(String merger_path, String pattern_Path, int file_num);
}
