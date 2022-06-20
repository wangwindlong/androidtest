package net.wangyl.test.ui

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import net.wangyl.test.R
import net.wangyl.test.dp

class MaxHeightRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {
    private var mMaxHeight: Float
    fun setMaxHeight(maxHeight: Int) {
        mMaxHeight = maxHeight.coerceAtLeast(240).toFloat()
        invalidate()
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        super.onMeasure(widthSpec, MeasureSpec.makeMeasureSpec(mMaxHeight.dp, MeasureSpec.AT_MOST))
    }

    init {
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.MaxHeightRecyclerView, defStyle, 0 /* defStyleRes */
        )
        mMaxHeight = a.getDimension(R.styleable.MaxHeightRecyclerView_maxheight, 0.0f)
        mMaxHeight = if (mMaxHeight <= 240) 240.toFloat() else mMaxHeight
        a.recycle()
    }
}