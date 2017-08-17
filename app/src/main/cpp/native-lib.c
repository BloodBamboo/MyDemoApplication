#include <android/log.h>
#include "native-head.h"
#include "assert.h"
#include "pthread.h"


#define TAG "TEST_JNI"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)

JNIEXPORT jint JNICALL
Java_com_example_admin_myapplication_ndk_NDKTest_stringFromJNI(JNIEnv *env, jobject instance,
                                                               jint a, jint b) {
    return addTest(a,b);
}

JNIEXPORT void JNICALL native_diff(JNIEnv *env, jclass type, jstring path,
                                                      jstring pattern_path, jint file_num) {

    LOGI("JNI native diff begin");

    const char *path_str = (*env)->GetStringUTFChars(env, path, NULL);
    const char *pattern_path_str = (*env)->GetStringUTFChars(env, pattern_path, NULL);

    LOGI("path_str = %s pattern_path_str = %s", path_str, pattern_path_str);
    //申请二维字符数据, 存放子文件名
    char **patches = (char **)malloc(sizeof(char *) * file_num);

    int i = 0;

    for (int i = 0; i < file_num; i++)
    {
        //每个文件名申请地址
        LOGI("char = %ld char * = %ld", sizeof(char), sizeof(char *));
        patches[i] = (char *)malloc(sizeof(char) * 100);
        // 需要分割的文件 Vibrato.mp4
        // 每个子文件名称 Vibrato_n.mp4
        sprintf(patches[i], pattern_path_str, i);
        LOGI("patch path : %s",patches[i]);
    }

    int fileSize =  get_file_size(path_str);

    FILE* fpr = fopen(path_str, "rb");

    /*
     * 1.判断文件大小能够被 file_num整除
     * 2.能整除就平分
     * 3.不能整除就先分 file_num -1
     * */
    if (fileSize % file_num == 0)
    {
        int part = fileSize / file_num;
        for (int i = 0; i < file_num; i++)
        {
            FILE *fpw = fopen(patches[i], "wb");//文件已经存在 就删除，只运行写
            for (int j = 0; j < part; j++)
            {
                fputc(fgetc(fpr), fpw);
            }
            fclose(fpw);
        }
    }
    else
    {
        int part = fileSize / (file_num - 1);
        int surplus = fileSize % file_num;
        for (int i = 0; i < file_num - 1; i++)
        {
            FILE *fpw = fopen(patches[i], "wb");//文件已经存在 就删除，只运行写
            for (int j = 0; j < part; j++)
            {
                fputc(fgetc(fpr), fpw);
            }
            fclose(fpw);
        }

        FILE *fpw = fopen(patches[file_num - 1], "wb");

        for (int i = 0; i < fileSize % (file_num - 1); i++) {
            fputc(fgetc(fpr),fpw);
        }
        fclose(fpw);
    }

    fclose(fpr);

    for (int i = 0; i < file_num; i++)
    {
        free(patches[i]);
    }

    free(patches);

    (*env)->ReleaseStringUTFChars(env, path, path_str);
    (*env)->ReleaseStringUTFChars(env, pattern_path, pattern_path_str);


    LOGI("JNI native diff end");
}

JNIEXPORT void JNICALL native_patch(JNIEnv *env, jclass type,
                                                       jstring merger_path, jstring pattern_path,
                                                       jint file_num) {
    LOGI("JNI native patch begin");

    const char *merger_path_str = (*env)->GetStringUTFChars(env, merger_path, NULL);
    const char *pattern_path_str = (*env)->GetStringUTFChars(env, pattern_path, NULL);

//申请二维字符数据, 存放子文件名
    char **patches = (char **)malloc(sizeof(char *) * file_num);

    int i =0;
    for (; i < file_num; i++) {
        //每个文件名申请地址
//        LOGI("char = %d char * = %d", sizeof(char), sizeof(char *));
        patches[i] = (char*) malloc(sizeof(char) * 100);
        // 需要分割的文件 Vibrato.mp4
        // 每个子文件名称 Vibrato_n.mp4
        sprintf(patches[i], pattern_path_str, i);// 格式化文件名
        LOGI("patch path : %s",patches[i]);
    }

    FILE* fpw = fopen(merger_path_str, "wb");

    for (int i =0; i < file_num; i++) {
        int filesize = get_file_size(patches[i]);
        FILE *fpr = fopen(patches[i], "rb");
        for (int j =0; j < filesize; j++) {
            fputc(fgetc(fpr), fpw);
        }
        fclose(fpr);
    }
    fclose(fpw);

    for (int i = 0; i < file_num; i++)
    {
        free(patches[i]);
    }

    free(patches);

    (*env)->ReleaseStringUTFChars(env, merger_path, merger_path_str);
    (*env)->ReleaseStringUTFChars(env, pattern_path, pattern_path_str);

    LOGI("JNI native patch end");
}

static const JNINativeMethod gMethods[] = {
        {
                "diff","(Ljava/lang/String;Ljava/lang/String;I)V",(void*)native_diff
        },
        {
                "patch","(Ljava/lang/String;Ljava/lang/String;I)V",(void*)native_patch
        }
};

static int registerNatives(JNIEnv *env)
{
    LOGI("registerNatives begin");
    jclass clazz;
    clazz = (*env)->FindClass(env, "com/example/admin/myapplication/ndk/NDKTest");

    if (clazz == NULL)
    {
        LOGI("clazz is null");
        return JNI_FALSE;
    }

    if ((*env)->RegisterNatives(env, clazz, gMethods, NELEM(gMethods)) > 0)
    {
        LOGI("registerNatives_Main error");
        return JNI_FALSE;
    }

    return JNI_TRUE;
}


JNIEXPORT jint JNI_OnLoad(JavaVM* vm, void* reserved)
{
    LOGI("jni_OnLoad begin");

    JNIEnv *env = NULL;

    if ((*vm)->GetEnv(vm, (void **)&env, JNI_VERSION_1_4) != JNI_OK)
    {
        LOGI("ERROR: GetEnv failed\n");
        return -1;
    }

    assert(env != NULL);

    registerNatives(env);

    return JNI_VERSION_1_4;
}
