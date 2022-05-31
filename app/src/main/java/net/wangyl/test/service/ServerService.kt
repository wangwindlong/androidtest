package net.wangyl.test.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import net.wangyl.test.ipc.Person
import net.wangyl.test.ipc.PersonServer

class ServerService : Service() {
    private val mPeople: ArrayList<Person> = ArrayList()

    override fun onCreate() {
        super.onCreate()
        Log.d("ServerService", "onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //只有startService启动才会执行该回调
        Log.d("ServerService", "onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        //多个客户端绑定先后只会执行一次
        Log.d("ServerService", "onBind")
        return object : PersonServer() {
            override fun addUser(person: Person) {
                mPeople.add(person)
            }

            override fun getPersonList(): List<Person> {
                return mPeople
            }
        }
    }

    override fun onRebind(intent: Intent?) {
        Log.d("ServerService", "onRebind")
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d("ServerService", "服务已解绑")
        //返回true，在重新绑定后会回调onReBind，（需要startservice+bindservice）即服务运行状态下重新绑定服务
        //否则只回调onBind
        return true
    }
}