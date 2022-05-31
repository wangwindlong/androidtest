package net.wangyl.test

import android.app.Application
import android.system.Os
import android.util.Log

class MyApp: Application() {

    init {
        instance = this
    }
    override fun onCreate() {
        super.onCreate()
        //这里会执行2次，不管有多少个进程，只要超过2个都有且仅会执行2次，会造成泄漏？
        Log.d("MyApp", "启动MyApp current pid = ${android.os.Process.myPid()} instance=$instance")
    }

    companion object {
        lateinit var instance: MyApp
    }
}