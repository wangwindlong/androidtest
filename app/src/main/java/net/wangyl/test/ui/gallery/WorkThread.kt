package net.wangyl.test.ui.gallery

import android.os.HandlerThread
import java.util.*
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

interface Observer {
    fun onClick(count: Int)
}

interface Clickable {
    fun click()
    fun register(observer: Observer)
    fun remove(observer: Observer)
}

class Button : Clickable {
    @Volatile
//    var count: AtomicInteger = AtomicInteger()
    var count: Int = 0
    val observers: ConcurrentLinkedDeque<Observer> = ConcurrentLinkedDeque()
    override fun click() {
//        count.incrementAndGet()
        count++
        for (observe in observers) {
            observe.onClick(count)
        }
    }

    override fun register(observer: Observer) {
        observers.remove(observer)
        observers.add(observer)
    }

    override fun remove(observer: Observer) {
        observers.remove(observer)
    }

}

class TextObserver: Observer {
    override fun onClick(count: Int) {
        println("点击了$count 次 改变文字")
    }
}
class ColorObserver: Observer {
    override fun onClick(count: Int) {
        println("点击了$count 次 改变颜色")
    }
}

fun main() {
//    val button = Button()
//    button.register(TextObserver())
////    button.register(ColorObserver())
//    val executors = Executors.newFixedThreadPool(100)
//    for (i in 1..1000) {
//        executors.execute {
//            button.click()
//        }
//    }
//    executors.shutdown()

    val queue: Queue<String> = LinkedList()

    for (i in 1..100) {
        Thread {
            queue.offer("用户$i")
        }.start()
    }
    Thread {
        queue.offer("1")
    }.start()
    TestThread().start()

}