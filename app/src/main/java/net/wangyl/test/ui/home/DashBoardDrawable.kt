package net.wangyl.test.ui.home

import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.graphics.toColorInt
import net.wangyl.test.dp

class DashBoardDrawable : Drawable() {
    val INTERVAL = 50.dp.toInt()
    val paint = Paint().apply {
        color = "#A51A1A".toColorInt()
        strokeWidth = 10.dp
    }
    val pointPaint = Paint().apply {
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 20.dp
        color = "#000000".toColorInt()
    }
    val path = Path()
    var radius = 100.dp
        set(value) {
            field = value
            initPath()
        }
    var point = PointF(0f, 0f)
        set(value) {
            field = value
            initPath()
        }

    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setBounds(left, top, right, bottom)
        initPath()
    }

    private fun initPath() {
        path.reset()
        path.addCircle(bounds.right / 2f, bounds.bottom / 2f, radius, Path.Direction.CCW)
        path.addRect(
            bounds.width() / 2f - radius,
            bounds.height() / 2f,
            bounds.width() / 2f + radius,
            bounds.height() / 2f + radius * 2,
            Path.Direction.CW
        )
        path.fillType = Path.FillType.WINDING  // INVERSE_WINDING
    }

    override fun draw(canvas: Canvas) {
        canvas.drawPath(path, paint)
        canvas.drawPoint(point.x, point.y, pointPaint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSPARENT
    }
}