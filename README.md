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


MyApp会调用两次onCreate，2个进程也是调用2次，3个进程也是调用2次，why？
会造成内存泄漏和其他问题吗？


