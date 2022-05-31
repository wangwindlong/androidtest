package net.wangyl.test

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import net.wangyl.test.databinding.ActivityMain2Binding
import net.wangyl.test.ipc.Person
import net.wangyl.test.ipc.PersonManager
import net.wangyl.test.ipc.PersonServer
import net.wangyl.test.service.ServerService

class MainActivity2 : AppCompatActivity() {

    lateinit var mServiceConnection: ServiceConnection
    var mService: PersonManager? = null

    var mBinded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mServiceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                mService = PersonServer.asInterface(service)
                Log.d("MainActivity2", "我MainActivity2绑定了 mService=$mService")
                mBinded = true
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                Log.d("MainActivity2", "我MainActivity2解绑了")
                mBinded = false
            }

        }
        val binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btn2.setOnClickListener {
            bindService(Intent(this@MainActivity2, ServerService::class.java).apply {
            }, mServiceConnection, Context.BIND_AUTO_CREATE)
        }

        binding.btn3.setOnClickListener {
            Log.d("MainActivity2", "添加用户")
            mService?.addUser(Person("王五", "fff", 30))
        }
        binding.btn4.setOnClickListener {
            Log.d("MainActivity2", "获取用户")
            println(mService?.getPersonList())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mBinded)  {
            Log.d("MainActivity2", "我MainActivity2退出了 开始解绑")
            unbindService(mServiceConnection)
        }
    }
}