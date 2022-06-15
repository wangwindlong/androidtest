package net.wangyl.test

import android.content.Context
import android.graphics.Bitmap
import android.os.HandlerThread

class MyHandlerThread(name: String?, val context: Context) : HandlerThread(name) {

    init {
//        val bitmap = Bitmap.createBitmap(10000, 10800, Bitmap.Config.ARGB_8888)
//        println("MyHandlerThread create  $bitmap : ${bitmap.byteCount}")
//        println("MyHandlerThread isDaemon=" + Thread.currentThread().isDaemon)
    }

    override fun run() {
        println("MyHandlerThread run isDaemon=" + Thread.currentThread().isDaemon)
        super.run()
    }

    @Override
    fun finalize() {
        println("MyHandlerThread ...finalize...")
    }
}