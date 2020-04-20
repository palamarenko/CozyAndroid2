package ua.palamarenko.cozyandroid2.tools.shape_view.shape

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import ua.palamarenko.cozyandroid2.tools.shape_view.shape.RectShape


class RoundRectShape(private val radius: Float) :
    RectShape() {
    override fun draw(canvas: Canvas, paint: Paint) {
        val margin: Float = paint.strokeWidth / 2
        val rectF = RectF(
            rect().left + margin,
            rect().top + margin,
            rect().right - margin,
            rect().bottom - margin
        )
        canvas.drawRoundRect(rectF, radius, radius, paint)
    }

}
