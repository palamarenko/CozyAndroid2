package ua.palamarenko.cozyandroid2.tools.shape_view.shape

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.shapes.Shape


open class RectShape : Shape() {
    private val mRect = RectF()
    override fun draw(canvas: Canvas, paint: Paint) {
        val margin: Float = paint.strokeWidth / 2
        val rectF = RectF(
            mRect.left + margin,
            mRect.top + margin,
            mRect.right - margin,
            mRect.bottom - margin
        )
        canvas.drawRect(rectF, paint)
    }

  override fun onResize(width: Float, height: Float) {
        mRect[0f, 0f, width] = height
    }



    protected fun rect(): RectF {
        return mRect
    }
}