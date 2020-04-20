package ua.palamarenko.cozyandroid2.tools.shape_view.factory

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.Shape
import ua.palamarenko.cozyandroid2.tools.shape_view.shape.CircleShape
import ua.palamarenko.cozyandroid2.tools.shape_view.shape.RectShape
import ua.palamarenko.cozyandroid2.tools.shape_view.shape.RoundRectShape
import ua.palamarenko.cozyandroid2.tools.shape_view.shape.TriangleShape


class ShapeDrawableFactory {
    enum class SHAPE {
        OVAL, RECTANGLE, ROUND_RECT, TRIANGLE
    }

    var width = 100
    var height = 100
    var shape = SHAPE.RECTANGLE
    var colorBack: Int? = Color.BLACK
    var startColor: Int? = null
    var endColor: Int? = null
    var gradientDirection = 0
    var radius = 10
    var isBorder = false
    var borderWidth = 10
    val drawable: Drawable
        get() {
            val shapeDrawable = ShapeDrawable(getShape(shape, radius))
            setColor(shapeDrawable.paint)
            if (isBorder) {
                shapeDrawable.paint.style = Paint.Style.STROKE
                shapeDrawable.paint.strokeWidth = borderWidth.toFloat()
            }
            return shapeDrawable
        }

    private fun getShape(shape: SHAPE?, radius: Int): Shape {
        return if (shape == null) {
            RectShape()
        } else when (shape) {
            SHAPE.OVAL -> CircleShape()
            SHAPE.RECTANGLE -> RectShape()
            SHAPE.ROUND_RECT -> RoundRectShape(
                radius.toFloat()
            )
            SHAPE.TRIANGLE -> TriangleShape()
            else -> RectShape()
        }
    }

    private fun setColor(paint: Paint) {
        if (startColor == null || endColor == null) {
            val colorRes = if (colorBack == null) Color.BLACK else colorBack!!
            paint.setColor(colorRes)
        } else {
            val startX = if (gradientDirection == 0) 0 else width / 2
            val startY = if (gradientDirection == 0) height / 2 else 0
            val endX = if (gradientDirection == 0) width else width / 2
            val endY = if (gradientDirection == 0) height / 2 else height
            val shader = LinearGradient(
                startX.toFloat(),
                startY.toFloat(),
                endX.toFloat(),
                endY.toFloat(),
                startColor!!,
                endColor!!,
                Shader.TileMode.CLAMP
            )
            paint.setShader(shader)
        }
    }

}