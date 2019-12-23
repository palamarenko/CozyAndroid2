package ua.palamarenko.cozyandroid2.tools

import android.graphics.*
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import java.io.File

fun ImageView.load(file: File, transformater: Transformation? = null, viewSize: Int? = null) {
    post {
        val pick = Picasso.get()
            .load(file)
            .centerCrop()
            .resize(viewSize ?: width, viewSize?: height)

        if (transformater != null) {
            pick.transform(transformater)
        }
        pick.into(this)
    }
}


fun ImageView.load(url: String, transformater: Transformation? = null, viewSize: Int? = null, errorIcon : Any? = null) {
    if (url.isNullOrEmpty()) {

        if(errorIcon!=null){
          if(errorIcon is Int) this.setImageResource(errorIcon)
          if(errorIcon is Drawable) this.setImageDrawable(errorIcon)
        }

        return
    }

    post {
        val pick = Picasso.get()
            .load(url)
            .centerCrop()
            .resize(viewSize ?: width, viewSize?: height)

        if (transformater != null) {
            pick.transform(transformater)
        }
        if(errorIcon!=null){
            if(errorIcon is Int) pick.error(errorIcon)
            if(errorIcon is Drawable) pick.error(errorIcon)
        }


        pick.into(this)
    }
}






fun ImageView.loadFromAssets(path : String){
    try {
        val d = Drawable.createFromStream(
            this.context!!.assets.open(path), null)
    this.setImageDrawable(d)
    }catch (e : Exception){}


}



class CircleTransform : Transformation {
    override fun key(): String {
        return "CircleTransform"
    }

    override fun transform(source: Bitmap): Bitmap {
        val size = Math.min(source.width, source.height)

        val x = (source.width - size) / 2
        val y = (source.height - size) / 2

        val squaredBitmap = Bitmap.createBitmap(source, x, y, size, size)
        if (squaredBitmap != source) {
            source.recycle()
        }

        val bitmap = Bitmap.createBitmap(size, size, source.config)

        val canvas = Canvas(bitmap)
        val paint = Paint()
        val shader = BitmapShader(
            squaredBitmap,
            Shader.TileMode.CLAMP, Shader.TileMode.CLAMP
        )
        paint.shader = shader
        paint.isAntiAlias = true

        val r = size / 2f
        canvas.drawCircle(r, r, r, paint)

        squaredBitmap.recycle()
        return bitmap
    }


}


