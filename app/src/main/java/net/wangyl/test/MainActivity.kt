package net.wangyl.test

import android.app.Activity
import android.app.Dialog
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import net.wangyl.test.databinding.ActivityMainBinding
import android.util.Log
import net.wangyl.test.ipc.Person
import net.wangyl.test.ipc.PersonManager
import net.wangyl.test.ipc.PersonServer
import net.wangyl.test.service.LocalService
import net.wangyl.test.service.LocalService2
import net.wangyl.test.service.MSG
import net.wangyl.test.service.ServerService
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.concurrent.thread

interface Test
class MainActivity : AppCompatActivity(),Test  {
    private lateinit var workHandler: Handler
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    var queue: LinkedList<String> = LinkedList()

    var handler: Handler? = null
    var mService: PersonManager? = null
    var mMessager: Messenger? = null
    var mBinded = false
    val mHandlerThread = MyHandlerThread("handlerThread", this)

    var useMessenger = false
    val executors: ExecutorService = Executors.newFixedThreadPool(10)

    var mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d("MainActivity", "我MainActivity绑定了 service=$service")
            if (useMessenger) {
                mMessager = Messenger(service)
            } else {
                mService = PersonServer.asInterface(service)
            }
            mBinded = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d("MainActivity", "我MainActivity解绑了")
            mBinded = false
            mService = null
            mMessager = null
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()

        TestJni() //测试jni代码 静态注册
        println(TestJni.doubleInt(9))  //动态注册+传参int+返回long
        println(TestJni.getString("你好吗")) //动态注册+传参string+返回string
        //启动服务，测试同时启动服务和绑定服务的生命周期
        startService(Intent(this@MainActivity, ServerService::class.java))
        startService(Intent(this@MainActivity, LocalService::class.java))
        startService(Intent(this@MainActivity, LocalService2::class.java))
        bindMyService()

        mHandlerThread.start()
        println("mHandlerThread.isDaemon=${mHandlerThread.isDaemon}")
        workHandler = object: Handler(mHandlerThread.getLooper()) {
            override fun handleMessage(msg: Message) {
                println("workHandler handleMessage $msg ${Thread.currentThread().name}")
            }
        }
//        workHandler.sendMessage()
//        mHandlerThread.quit()

//        for (i in 1..100) {
//            executors.execute {
//                Thread.sleep(1000)
//                queue.offer("用户 $i")
//                workHandler.sendMessage(Message.obtain(workHandler).apply { obj = "用户 $i" })
//            }
//        }

        //测试application加载dialog
//        Dialog(MyApp.instance).show()
        //测试内存泄漏
        LeakTest(this).start()

        //测试内存抖动
//        handler = Handler(object : Handler.Callback {
//            override fun handleMessage(msg: Message): Boolean {
//                val tmp = arrayListOf<String>()
//                for (i in 1..10000) {
//                    tmp.add("$i")
//                }
//                handler?.sendMessageDelayed(Message.obtain().apply { what = 1 }, 5000)
//                return false
//            }
//
//        })
//        handler?.sendMessage(Message.obtain().apply { what = 0 })
    }


    private fun initView() {
        setSupportActionBar(binding.appBarMain.toolbar)
        binding.appBarMain.fab.setOnClickListener { view ->
//            val msg: Message = Message.obtain()
//            msg.what = 2 //消息的标识
//            msg.obj = "B" // 消息的存放
//            workHandler.sendMessage(msg)
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
            if (useMessenger) {
                if (!mBinded) return@setOnClickListener
                mMessager?.send(Message.obtain().apply {
                    what = MSG
                    data.putParcelable("test", Person("王二"))
                })
            } else {
                println(mService?.getPersonList())
            }
        }
        binding.appBarMain.fab2.setOnClickListener {
            startActivity(Intent(this@MainActivity, MainActivity2::class.java))
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    fun bindMyService() {
        if (useMessenger) {
            bindService(Intent(this@MainActivity, LocalService2::class.java), mConnection, BIND_AUTO_CREATE)
        } else {
            bindService(Intent(this@MainActivity, ServerService::class.java), mConnection, BIND_AUTO_CREATE)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "我MainActivity退出了 开始解绑")
        if (mBinded) unbindService(mConnection)
        executors.shutdown()
        mHandlerThread.quit()
//        thread {
//            Thread.sleep(2000)
//            Runtime.getRuntime().gc()
//        }

    }

    @Override
    fun finalize() {
        println("...finalize...")
    }
}

class LeakTest(var obj: Any?) : Thread() {
    override fun run() {
        super.run()
        val tmp = obj
        obj = null
//        while (true) {
            SystemClock.sleep(1000)
            println("LeakTest Running tmp:${tmp}")
//        }
//        Thread.sleep(10000)
    }
}