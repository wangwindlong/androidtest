package net.wangyl.test

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Log
import com.github.moduth.blockcanary.BlockCanaryContext


class BlockContext : BlockCanaryContext() {
    private val TAG = "BlockContext"

    override fun provideQualifier(): String {
        var qualifier = ""
        try {
            val info: PackageInfo = MyApp.instance.packageManager
                .getPackageInfo(MyApp.instance.packageName, 0)
            qualifier += info.versionCode.toString() + "_" + info.versionName + "_YYB"
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "provideQualifier exception", e)
        }
        println("provideQualifier qualifier=$qualifier")
        return qualifier
    }

    override fun provideUid(): String {
        return "87224330"
    }

    override fun provideNetworkType(): String {
        return "4G"
    }

    override fun provideMonitorDuration(): Int {
        return 9999
    }

    override fun provideBlockThreshold(): Int {
        return 500
    }

    override fun displayNotification(): Boolean {
        return BuildConfig.DEBUG
    }

    override fun concernPackages(): List<String>? {
        val list = super.provideWhiteList()
        list.add("com.example")
        return list
    }

    override fun provideWhiteList(): List<String>? {
        val list = super.provideWhiteList()
        list.add("com.whitelist")
        return list
    }

    override fun stopWhenDebugging(): Boolean {
        return true
    }
}