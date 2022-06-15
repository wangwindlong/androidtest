package net.wangyl.test.thread

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

val queue: Queue<String> = LinkedList()


class ThreadHandlerTest(name: String?, val runnable: Runnable) : HandlerThread(name) {
    override fun run() {
        super.run()
        runnable.run()
    }
}

class RegHandler(looper: Looper) : Handler(looper) {
    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
//        synchronized(obj) {
        while (true) {
            if (!queue.isEmpty()) {
                println("登记：" + queue.poll())
            }
        }
//        }
    }
}


fun main() {
    val obj = Any()
    //需要在android环境中运行
    val th = HandlerThread("tttt")
//    {
////        synchronized(obj) {
//            while (true) {
//                if (!queue.isEmpty()) {
//                    println("登记：" + queue.poll())
//                }
//            }
////        }
//    }
    th.start()
    val reg = RegHandler(th.looper)
    val executors = Executors.newFixedThreadPool(10)
    for (i in 1..100) {
        executors.execute {
            Thread.sleep(1000)
            queue.offer(i.toString())
        }
    }

    Thread.sleep(15000)
    executors.shutdown()
}