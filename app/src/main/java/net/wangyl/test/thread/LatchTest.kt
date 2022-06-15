package net.wangyl.test.thread

import java.util.concurrent.CountDownLatch


class ThreadTest(val id: String, val countDownLatch: CountDownLatch) : Thread("test") {
    override fun run() {
        super.run()
        println("hello $id")
        countDownLatch.countDown()
        countDownLatch.await()
        println("$id world")
        Thread.sleep(1000)
    }
}

fun main() {
    val countDownLatch = CountDownLatch(5)

    for (i in 1..5) {
        val thread = ThreadTest("$i", countDownLatch)
        thread.start()
    }
    countDownLatch.await()
    println("程序结束")
}