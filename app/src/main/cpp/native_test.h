//
// Created by 12 on 2019/12/31.
//
#include <stdio.h>
#include <jni.h>
#include <android/log.h>
#ifndef MYDEMOAPPLICATION_NATIVE_TEST_H
#define MYDEMOAPPLICATION_NATIVE_TEST_H
#define TAG "TEST_JNI"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)
#define NELEM(x) ((int) (sizeof(x) / sizeof((x)[0])))//计算有几个JNINativeMethod

extern "C" {
    int add(int a, int b);
}

#endif //MYDEMOAPPLICATION_NATIVE_TEST_H


