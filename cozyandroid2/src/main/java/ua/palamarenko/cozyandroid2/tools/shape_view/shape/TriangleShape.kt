package ua.palamarenko.cozyandroid2.tools.shape_view.shape

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import ua.palamarenko.cozyandroid2.tools.shape_view.shape.RectShape


class TriangleShape : RectShape() {
    private val path: Path = Path()
   override fun draw(canvas: Canvas, paint: Paint) {
        val margin: Float = paint.getStrokeWidth() / 2
        val rectF = RectF(
            rect().left + margin,
            rect().top + margin,
            rect().right - margin,
            rect().bottom - margin
        )
        path.moveTo(rectF.left, rectF.bottom + margin)
        path.lineTo(rectF.centerX(), rectF.top)
        path.lineTo(rectF.right, rectF.bottom)
        path.lineTo(rectF.left, rectF.bottom)
        canvas.drawPath(path, paint)
    }
}