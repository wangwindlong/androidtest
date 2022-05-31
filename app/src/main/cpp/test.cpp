//
// Created by 王云龙 on 2022/5/30.
//

#include "test.h"

extern "C"
JNIEXPORT jstring JNICALL
Java_net_wangyl_test_TestJni_00024Companion_nativeInit(JNIEnv *env, jobject thiz) {
    std::string hello = "hello from c++";
    return env->NewStringUTF(hello.c_str());
}