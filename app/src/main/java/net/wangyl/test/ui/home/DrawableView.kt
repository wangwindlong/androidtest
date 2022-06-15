package net.wangyl.test.ui.home

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PointF
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import net.wangyl.test.dp

class DrawableView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    val drawable = DashBoardDrawable()
//    val drawable = ScrimDrawable()

    var radius = 50.dp
        set(value) {
            field = value
            invalidate()
        }
    var point = PointF(0f, 0f)
        set(value) {
            field = value
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
//        drawable.setBounds(10.dp.toInt(), 10.dp.toInt(), width, height)
        drawable.setBounds(0, 0, width, height)
        drawable.radius = radius
        drawable.point = point
        drawable.draw(canvas)

    }

//    override fun draw(canvas: Canvas) {
//        super.draw(canvas)
//        println("DrawableView draw")
//        drawable.radius = radius
//        drawable.draw(canvas)
//    }
}