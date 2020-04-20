package ua.palamarenko.cozyandroid2.tools.shape_view.views

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import ua.palamarenko.cozyandroid2.R
import ua.palamarenko.cozyandroid2.tools.shape_view.factory.ShapeDrawableFactory
import ua.palamarenko.cozyandroid2.tools.shape_view.factory.ShapeDrawableFactory.SHAPE


class ShapeTextView : AppCompatTextView {
    private var drawable: ShapeDrawableFactory? = null

    constructor(context: Context?) : super(context) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        if (attrs != null) {
            val arr: TypedArray =
                context.obtainStyledAttributes(attrs, R.styleable.ShapeTextView)
            drawable = getViewShapeDrawable(arr)
            arr.recycle()
        } else {
            drawable = ShapeDrawableFactory()
        }
    }

    private fun getViewShapeDrawable(arr: TypedArray): ShapeDrawableFactory {
        val drawable = ShapeDrawableFactory()
        drawable.shape = SHAPE.values()[arr.getInt(R.styleable.ShapeTextView_back_shape, 0)]
        val colorBack = arr.getString(R.styleable.ShapeTextView_back_shape_color)
        if (colorBack != null) {
            drawable.colorBack = Color.parseColor(colorBack)
        }
        val startColor =
            arr.getString(R.styleable.ShapeTextView_gradient_start_color)
        if (startColor != null) {
            drawable.startColor = Color.parseColor(startColor)
        }
        val endColor = arr.getString(R.styleable.ShapeTextView_gradient_end_color)
        if (endColor != null) {
            drawable.endColor = Color.parseColor(endColor)
        }
        val radius = arr.getDimensionPixelSize(R.styleable.ShapeTextView_corners_radius, -1)
        if (radius >= 0) {
            drawable.radius = radius
        }
        val border =
            arr.getBoolean(R.styleable.ShapeTextView_back_shape_border, false)
        drawable.isBorder = border
        val borderWidth =
            arr.getDimensionPixelOffset(R.styleable.ShapeTextView_back_shape_border_width, 0)
        if (borderWidth > 0) {
            drawable.borderWidth = borderWidth
        }
        val gradientDirection = arr.getInt(R.styleable.ShapeTextView_gradient_direction, 0)
        drawable.gradientDirection = gradientDirection
        return drawable
    }

    fun setShape(shape: SHAPE?) {
        drawable!!.shape = shape!!
        background = drawable!!.drawable
    }

    fun setShapeColor(@ColorInt color: Int) {
        drawable!!.colorBack = color
        background = drawable!!.drawable
    }

    fun setShapeColorFromRes(@ColorRes color: Int) {
        drawable!!.colorBack = ContextCompat.getColor(context, color)
        drawable!!.startColor = null
        drawable!!.endColor = null
        background = drawable!!.drawable
    }

    fun setShapeGradient(@ColorInt colorStart: Int, @ColorInt colorEnd: Int) {
        drawable!!.startColor = colorStart
        drawable!!.endColor = colorEnd
        background = drawable!!.drawable
    }

    fun setBorder(needBorder: Boolean) {
        drawable!!.isBorder = needBorder
        background = drawable!!.drawable
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        drawable!!.width = w
        drawable!!.height = h
        super.onSizeChanged(w, h, oldw, oldh)
        background = drawable!!.drawable
    }
}