package net.wangyl.test.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.widget.Toast
import net.wangyl.test.ipc.Person


const val MSG = 1

fun showToast(context: Context, msg: String?) {
    Toast.makeText(context, "获取到消息${msg}", Toast.LENGTH_SHORT).show()
}

//https://developer.android.com/guide/components/bound-services
//参考 https://android.googlesource.com/platform/development/+/master/samples/ApiDemos/src/com/example/android/apis/app/MessengerService.java
class LocalService2 : Service() {

    lateinit var mMessenger: Messenger

    override fun onBind(intent: Intent?): IBinder? {
        return Messenger(MessengerHandler(this)).binder
    }


    internal class MessengerHandler(context: Context, val app: Context = context.applicationContext) : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG -> {
                    //会报Class not found when unmarshalling
                    msg.data.classLoader = Person::class.java.classLoader
//                    msg.data.classLoader.loadClass("net.wangyl.test.ipc.Person")
                    showToast(app, msg.data.getParcelable<Person>("test")?.toString())
                }
            }
        }

    }
}