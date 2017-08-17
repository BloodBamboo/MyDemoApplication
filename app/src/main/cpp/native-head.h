//
// Created by Administrator on 2017/8/15.
//
#include <stdio.h>
#include <jni.h>
#ifndef MYAPPLICATIONNDK_NATIVE_HEAD_H
#define MYAPPLICATIONNDK_NATIVE_HEAD_H
#define NELEM(x) ((int) (sizeof(x) / sizeof((x)[0])))//计算有几个JNINativeMethod


extern int addTest(int a, int b);
//获取文件大小
long  get_file_size(const char* path) {
    FILE *fp = fopen(path, "rb"); //打开一个文件， 文件必须存在，只运行读
    fseek(fp, 0, SEEK_END);
    long ret = ftell(fp);
    fclose(fp);
    return ret;
}
#endif //MYAPPLICATIONNDK_NATIVE_HEAD_H

