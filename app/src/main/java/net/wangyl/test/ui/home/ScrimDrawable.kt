package net.wangyl.test.ui.home

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import androidx.core.graphics.toColorInt
import net.wangyl.test.dp

class ScrimDrawable: Drawable() {
    val INTERVAL = 50.dp.toInt()
    val paint = Paint().apply {
        color = "#A51A1A".toColorInt()
        strokeWidth = 10.dp
    }
    override fun draw(canvas: Canvas) {
        var x = bounds.left + paint.strokeWidth / 2
        while (x < bounds.right) {
            canvas.drawLine(x, bounds.top.toFloat(), x, bounds.bottom.toFloat(), paint)
            x += INTERVAL
        }
        var y = bounds.top + paint.strokeWidth / 2
        while (y < bounds.bottom) {
            canvas.drawLine(bounds.left.toFloat(), y, bounds.right.toFloat(), y, paint)
            y += INTERVAL
        }
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

//    override fun getAlpha(): Int {
//        return paint.alpha
//    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun getColorFilter(): ColorFilter? {
        return paint.colorFilter
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSPARENT
    }
}