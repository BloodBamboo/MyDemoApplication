package com.example.admin.myapplication.ndk;

public class NDKFFmpegTest {
//    static {
//        System.loadLibrary("native-lib");
//    }


    public native void ffmpegLoadPath(String path);

    public native int init(String pcmPath, int audioChannels, int bitRate, int
            sampleRate, String mp3Path);

}
