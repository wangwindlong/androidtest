//
// Created by 王云龙 on 2022/5/30.
//

#include "test.h"
#include <android/log.h>
#define TAG "testjni" // 这个是自定义的LOG的标识
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__) // 定义LOGD类型
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__) // 定义LOGI类型
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN, TAG, __VA_ARGS__) // 定义LOGW类型
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__) // 定义LOGE类型
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL, TAG, __VA_ARGS__) // 定义LOGF类型


//静态注册
extern "C"
JNIEXPORT jstring JNICALL
Java_net_wangyl_test_TestJni_00024Companion_nativeInit(JNIEnv *env, jobject thiz) {
    std::string hello = "hello from c++";
    LOGD("这个是c++自身打印 %s", hello.c_str());
    return env->NewStringUTF(hello.c_str());
}