//
// Created by 王云龙 on 2022/5/31.
// 动态注册jni
//

#include "test.h"
#include <android/log.h>
#define TAG "testjni2" // 这个是自定义的LOG的标识
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__) // 定义LOGD类型
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__) // 定义LOGI类型
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN, TAG, __VA_ARGS__) // 定义LOGW类型
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__) // 定义LOGE类型
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL, TAG, __VA_ARGS__) // 定义LOGF类型


//动态注册
extern "C"
jstring getString(JNIEnv *env, jobject obj, jstring str) {
    std::string test = "这个是从动态注册jni返回的hello";
//    Jstring2CStr(env, str)
    LOGD("原始字符：%s， 转换后：%s/n", env->GetStringUTFChars(str, 0), test.c_str());
    return (*env).NewStringUTF(test.c_str());
}

jlong doubleInt(JNIEnv *env, jobject obj, jint origin) {
    return origin * 2;
}

static const char* mClassName = "net/wangyl/test/TestJni$Companion";
static const JNINativeMethod methods[] = {
        {"getString", "(Ljava/lang/String;)Ljava/lang/String;", (void *) getString},
        {"doubleInt", "(I)J",  (void *) doubleInt}
};

JNIEXPORT jint JNICALL
JNI_OnLoad(JavaVM *vm, void *unused) {
    JNIEnv* env = NULL;
    //获取JNIEnv
    int r = vm->GetEnv((void **) &env, JNI_VERSION_1_6);
    if (r != JNI_OK) {
        return -1;
    }
    jclass myClass = env -> FindClass(mClassName);
    //注册
    r = env->RegisterNatives(myClass, methods, 2);
    if (r != JNI_OK) return  -1;
    return JNI_VERSION_1_6;
}


/**
* 工具方法
* 作用: 把java中的string 转化成一个c语言中的char数组
* 接受的参数 envjni环境的指针
* jstr 代表的是要被转化的java的string 字符串
* 返回值 : 一个c语言中的char数组的首地址 (char 字符串)
*/

//char* Jstring2CStr(JNIEnv* env, jstring jstr)
//
//{
//    char* rtn = NULL;
//    jclass clsstring = env->FindClass(env, "java/lang/String");
//
//    jstring strencode = env->NewStringUTF(env,"GB2312");
//
//    jmethodID mid =
//
//            env->GetMethodID(env,clsstring,"getBytes","(Ljava/lang/String;)[B");
//
//// String.getByte("GB2312");
//
//    jbyteArray barr= (jbyteArray)(*env)->CallObjectMethod(env,jstr,mid,strencode);
//
//    jsize alen = (*env)->GetArrayLength(env,barr);
//
//    jbyte* ba = (*env)->GetByteArrayElements(env,barr,JNI_FALSE);
//
//    if(alen > 0)  {
//
//        rtn = (char*)malloc(alen+1); //"\0"
//
//        memcpy(rtn,ba,alen);
//
//        rtn[alen]=0;
//
//    }
//
//    env->ReleaseByteArrayElements(env,barr,ba,0); //
//
//    return rtn;
//
//}