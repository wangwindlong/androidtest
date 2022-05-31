##用代码实现ipc通信
#ServerService是另一个进程的服务端，用来接收数据，MainActivity和MainActivity2都绑定它，获取数据
注意用binder传递对象时，需要手动设置classloader，因为每个进程一个JVM，而新开的进程没有Person类的加载器，在接收到数据时会报
ClassNotFound异常。

#LocalService2也是新进程，用来测试Messenger传递信息
[参考](https://developer.android.com/guide/components/aidl)
[参考2](https://developer.android.com/guide/components/bound-services)
[参考3](https://android.googlesource.com/platform/development/+/master/samples/ApiDemos/src/com/example/android/apis/app/MessengerService.java)


#LocalService是测试服务，暂时没有用



#后续需要测试多apk间进程通信。
[参考](https://github.com/mjzuo/BlogSample)


#JNI调用
1. 静态注册(手动)：繁琐效率低，需要在jni层搜索对应的本地函数然后建立对应关系。
   编写java类， TestJNI.java,  kotlin: external fun stringFromJNI(): String 
                              java: static native String stringFromJNI();
   编译class文件，javac TestJNI.java 生成TestJNI.class
   javah xxx.TestJNI 生成xxx_TestJNI.h (xxx全类名，如：net.wangyl.test)头文件
       1.8之后可以直接javac -h . TestJNI.java 可以在当前目录下同时生成.h和.c
   编写xxx_TestJNI.c源文件，拷贝xxx_TestJNI.h下的函数并实现，引入#include <jni.h> #include <string>
   编写CMakeLists.txt加载对应的依赖库，编译生成动态/静态库
   java/kotlin层加载这个so  System.loadLibrary("test")
2. 动态注册是通过注册方法表 https://juejin.cn/post/7022875228690710565    是否需要解注册？jni内存申请和释放？
   java层在加载so的时候(System.loadLibrary("test"))，jvm会调用JNI_OnLoad(JavaVM* jvm, void* reserved),
   在这个方法中添加java层方法和native层方法的对应关系
   生成java的class文件并查看方法的签名： javap -s -p TestJNI2.class 如果是kotlin 直接as就可以查看字节码
   java或kotlin层和上面静态注册一个操作，就是native方法会爆红，可以直接运行。
   代码模板
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
       if (r != JNI_OK) return -1;
       return JNI_VERSION_1_6;
   }
3. System.loadLibrary 和 System.load 的区别
   System.load：必须是绝对路径+完整前后缀名
   System.loadLibrary：参数为库文件名，不需要后缀和前缀(linux会自动在前面加lib)
4. 


打印日志： 
    在CMakeLists.txt中添加 find_library(log-lib, log)
    target_link_libraries(xxx, ${log-lib})
    在cpp中 #include <android/log.h>
    #define TAG "testjni" // 这个是自定义的LOG的标识
    #define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__) // 定义LOGD类型
    std::string hello = "hello from c++";
    LOGD("这个是c++自身打印 %s", hello.c_str());

MyApp会调用两次onCreate，2个进程也是调用2次，3个进程也是调用2次，why？
会造成内存泄漏和其他问题吗？


