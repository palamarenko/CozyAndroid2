package ua.palamarenko.cozyandroid2.cozy_view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.VectorDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import ua.palamarenko.cozyandroid2.R
import kotlin.math.min

class CozyRatingView : AppCompatImageView {

    var starMaxSize = 0

    var starRating = 0f

    var fullColor = Color.WHITE
    var emptyColor = Color.parseColor("#B5C1C1C1")

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        if (attrs != null) parseAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        if (attrs != null) parseAttrs(attrs)
    }

    private fun parseAttrs(attrs: AttributeSet) {
        val arr: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.CozyRatingView)
        val startColor =
            arr.getString(R.styleable.CozyRatingView_empty_color)
        if (startColor != null) {
            emptyColor = Color.parseColor(startColor)
        }

        val endColor =
            arr.getString(R.styleable.CozyRatingView_full_color)

        if (endColor != null) {
            fullColor = Color.parseColor(endColor)
        }

        arr.recycle()
    }


    lateinit var paint: Paint
    lateinit var paintFull: Paint

    private fun getBitmap(vectorDrawable: VectorDrawable, size: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(
            size,
            size, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return bitmap
    }


    fun setRating(rating: Double) {
        starRating = rating.toFloat()
        invalidate()
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0f, 0f, starMaxSize.toFloat(), height.toFloat(), paint)
        canvas.drawRect(0f, 0f, starMaxSize.toFloat() / 5 * starRating, height.toFloat(), paintFull)
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        initPaint(changed, left, top, right, bottom)
    }

    private fun initPaint(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val weight = (right - left) / 5
        val height = bottom - top

        val min = min(weight, height)
        starMaxSize = min * 5


        paint = Paint().apply {
            val resize =
                getBitmap(
                    ua.palamarenko.cozyandroid2.tools.getDrawable(R.drawable.ic_star) as VectorDrawable,
                    min
                )

            val mBitmapShader = BitmapShader(resize, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
            shader = mBitmapShader
            colorFilter = PorterDuffColorFilter(emptyColor, PorterDuff.Mode.SRC_IN)
        }

        paintFull = Paint().apply {
            val resize =
                getBitmap(
                    ua.palamarenko.cozyandroid2.tools.getDrawable(R.drawable.ic_star) as VectorDrawable,
                    min
                )

            val mBitmapShader = BitmapShader(resize, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
            shader = mBitmapShader
            colorFilter = PorterDuffColorFilter(fullColor, PorterDuff.Mode.SRC_IN)
        }
    }


}

