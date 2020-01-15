//
// Created by 12 on 2019/12/31.
//

#include <pthread.h>
#include "native_test.h"

//c++使用c的头文件和库需要使用混合编译，否则报错
extern "C" {
#include "libavutil/avutil.h"
}

JavaVM *jvm;

extern "C" JNIEXPORT jint JNICALL
Java_com_example_admin_myapplication_ndk_NDKTest_student(JNIEnv *env, jobject thiz,
                                                         jobject student) {
    LOGI("====setStu===begin====");

    LOGI("第三方库引用1+2=%d", add(1,2));
    const char *student_class_str = "com/example/admin/myapplication/ndk/Student";
    //获取jclass的实例
    jclass student_class = env->FindClass(student_class_str);
    if (student_class == NULL) {
        LOGI("student_class is null!");
        return -1;
    }
    //获取student_class的字段ID
    jfieldID nameId = env->GetFieldID(student_class, "name", "Ljava/lang/String;");
    jfieldID desId = env->GetFieldID(student_class, "des", "Ljava/lang/String;");
    jfieldID ageId = env->GetFieldID(student_class, "age", "I");

    env->SetIntField(student, ageId, 10);
    env->SetObjectField(student, nameId, env->NewStringUTF("李明"));
    env->SetObjectField(student, desId, env->NewStringUTF("无话可说"));
    LOGI("====setStu===end====");
    return 0;
}

extern "C" JNIEXPORT jobject JNICALL
Java_com_example_admin_myapplication_ndk_NDKTest_person(JNIEnv *env, jobject thiz) {
    LOGI("====person===begin====");
    //获取person相关信息
    const char *person_class_str = "com/example/admin/myapplication/ndk/Person";
    jclass person_class = env->FindClass(person_class_str);
    //获取student相关信息
    const char *student_class_str = "com/example/admin/myapplication/ndk/Student";
    //获取jclass的实例
    jclass student_class = env->FindClass(student_class_str);
    if (person_class == NULL || student_class == NULL) {
        LOGI("class is null!");
        return nullptr;
    }
    //无餐构造方法获取person和student的实例
    jobject person = env->AllocObject(person_class);
    jobject student = env->AllocObject(student_class);

    const char *setStudent_sig = "(Lcom/example/admin/myapplication/ndk/Student;)V";
    jmethodID setStudentId = env->GetMethodID(person_class, "setStudent", setStudent_sig);

    //获取student_class的字段ID
    jfieldID nameId = env->GetFieldID(student_class, "name", "Ljava/lang/String;");
    jfieldID desId = env->GetFieldID(student_class, "des", "Ljava/lang/String;");
    jmethodID ageId = env->GetMethodID(student_class, "setAge", "(I)V");

    env->CallVoidMethod(student, ageId, 10);
    env->SetObjectField(student, nameId, env->NewStringUTF("李明"));
    env->SetObjectField(student, desId, env->NewStringUTF("无话可说"));

    env->CallVoidMethod(person, setStudentId, student);

//    env->DeleteLocalRef(person);
//    env->DeleteLocalRef(student);
    LOGI("====person===end====");
    return person;
}

jclass student_class;

void init_student(JNIEnv *env) {
    if (student_class == nullptr) {
        //获取student相关信息
        const char *student_class_str = "com/example/admin/myapplication/ndk/Student";
        //获取jclass的实例
        jclass temp = env->FindClass(student_class_str);
        //提升为全局变量
        student_class = static_cast<jclass>(env->NewGlobalRef(temp));
        LOGI("全局提升完成");
    }

    const char *method = "<init>";
    jmethodID initId = env->GetMethodID(student_class, method, "(Ljava/lang/String;)V");
    env->NewObject(student_class, initId, env->NewStringUTF("李明"));
}

void delete_student(JNIEnv *env) {
    //全局变量用全局回收
    if (student_class != nullptr) {
        env->DeleteGlobalRef(student_class);
        student_class = NULL;
        LOGI("全局回收完成");
    }
}

/**
 * 静态使用方式示例
 */
//extern "C" JNIEXPORT void JNICALL
//Java_com_example_admin_myapplication_ndk_NDKTest_initStudent(JNIEnv *env, jobject thiz){
//    init_student(env);
//}
//
//extern "C" JNIEXPORT void JNICALL
//Java_com_example_admin_myapplication_ndk_NDKTest_deleteStudent(JNIEnv *env, jobject thiz) {
//  delete_student(env);
//}

void *cunrrent_thread(void *pVoid) {
    for (int i = 0; i < 10; ++i) {
        LOGI("线程执行了%d", i);
    }
    jobject instance = static_cast<jobject>(pVoid);
    JNIEnv *env = nullptr;
    //native线程绑定到jvm线程,只有jvm能扣跨线程, 线程原因GetEnv和FindClass都无法正常获取到结果
    jint result = jvm->AttachCurrentThread(&env, 0);
//    jint result = jvm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6);
    if (result != JNI_OK) {
        LOGI("env获取失败");
        return 0;
    }

    jclass clazz = env->GetObjectClass(instance);
//    const char *ndk_test_class_str = "com/example/admin/myapplication/ndk/NDKTest";
//    jclass clazz2= env->FindClass(ndk_test_class_str);
    if (clazz == nullptr) {
        LOGI("clazz获取失败");
        return 0;
    }
    jmethodID methodId = env->GetMethodID(clazz, "upDataUI", "()V");

    env->CallVoidMethod(instance, methodId);
    //解除jvm线程绑定
    jvm->DetachCurrentThread();

    return 0;
}

jobject instance;

void thread_test(JNIEnv *env, jobject thiz) {
    pthread_t pthreadId;
    instance = env->NewGlobalRef(thiz);
    pthread_create(&pthreadId, 0, cunrrent_thread, instance);
    pthread_join(pthreadId, 0);
}

void onDestroy(JNIEnv *env, jobject thiz) {
    LOGI("c++ onDestroy begin");
    //释放全局变量instance
    if (instance != nullptr) {
        env->DeleteGlobalRef(instance);
        instance = nullptr;
    }
}

void ffmepg(JNIEnv *env, jobject thiz) {
    LOGI("c++ ffmepg begin %s", av_version_info());
}

/*
 * 动态使用示例
 */
static const JNINativeMethod d_register[] = {
        {"initStudent",   "()V", (void *) (init_student)},
        {"deleteStudent", "()V", (void *) (delete_student)},
        {"threadTest",    "()V", (void *) (thread_test)},
        {"onDestroy",    "()V", (void *) (onDestroy)},
        {"ffmepg",    "()V", (void *) (ffmepg)}
};


extern "C" JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    LOGI("c++ jni_OnLoad begin");
    jvm = vm;
    JNIEnv *env = nullptr;
    jint result = vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6);
    if (result != JNI_OK) {
        LOGI("ERROR: GetEnv failed\n");
        return -1;
    }

    const char *ndk_test_class_str = "com/example/admin/myapplication/ndk/NDKTest";
    jclass ndk_test_class = env->FindClass(ndk_test_class_str);
    //动态注册
    env->RegisterNatives(ndk_test_class, d_register, NELEM(d_register));
    return JNI_VERSION_1_6;
};

