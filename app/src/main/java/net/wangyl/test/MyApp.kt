package net.wangyl.test

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.system.Os
import android.util.Log
import com.github.moduth.blockcanary.BlockCanary
import com.github.moduth.blockcanary.BlockCanaryContext
import leakcanary.LeakCanary

class MyApp: Application() {

    init {
        instance = this
    }
    override fun onCreate() {
        super.onCreate()
        //这里会执行2次，不管有多少个进程，只要超过2个都有且仅会执行2次，会造成泄漏？
        Log.d(TAG, "启动MyApp current pid = ${android.os.Process.myPid()} instance=$instance")
        if (isMainProcess()) {
            Log.d(TAG, "onCreate isMainProcess install BlockCanary")
            BlockCanary.install(this, BlockContext()).start()
        }
    }

    companion object {
        lateinit var instance: MyApp
        private val TAG = "MyApp"

        fun isMainProcess() :Boolean {
            val activityManager: ActivityManager = instance.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val processes = activityManager.runningAppProcesses
            for (process in processes) {
                Log.d(TAG, "process.processName=${process.processName}")
                println("process.processName=${process.processName}")
                if (process.processName.equals(BuildConfig.APPLICATION_ID) && process.pid == android.os.Process.myPid()) {
                    return true
                }
            }
            return false
        }
    }
}