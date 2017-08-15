package com.example.admin.myapplication.ndk;

/**
 * Created by Administrator on 2017/8/15.
 */

public class NDKTest {

    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("lib-test");
    }

    public native int stringFromJNI(int a, int b);
}
