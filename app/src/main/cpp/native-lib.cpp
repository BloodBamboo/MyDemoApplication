#include <jni.h>
#include <string>
#include "android/log.h"
#include "native-head.h"

#define TAG "TEST_JNI"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)
extern "C"

JNIEXPORT jint JNICALL
Java_com_example_admin_myapplication_ndk_NDKTest_stringFromJNI(JNIEnv *env, jobject instance,
                                                               jint a, jint b) {
    return addTest(a,b);
}
