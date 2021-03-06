package ua.palamarenko.cozyandroid2.tools

import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import java.io.File


private var picassoClient: Picasso? = null


fun setPicassoClient(picasso: Picasso) {
    picassoClient = picasso
}

private fun getPicassoClient() : Picasso{
   return if(picassoClient!=null) picassoClient!! else Picasso.get()
}



fun ImageView.load(file: File, transformater: Transformation? = null, viewSize: Int? = null) {
    post {
        val pick = Picasso.get()
            .load(file)
            .centerCrop()
            .resize(viewSize ?: width, viewSize ?: height)

        if (transformater != null) {
            pick.transform(transformater)
        }
        pick.into(this)
    }
}


fun ImageView.load(
    url: String,
    transformation: Transformation? = null,
    viewSize: Int? = null,
    errorIcon: Any? = null,
    placeholder: Any? = null,
    policy: MemoryPolicy? = null,
    needCenterCrop : Boolean = true
) {
    if (url.isNullOrEmpty()) {

        if (errorIcon != null) {
            if (errorIcon is Int) this.setImageResource(errorIcon)
            if (errorIcon is Drawable) this.setImageDrawable(errorIcon)
        } else if (placeholder != null) {
            if (placeholder is Int) this.setImageResource(placeholder)
            if (placeholder is Drawable) this.setImageDrawable(placeholder)
        }

        return
    }

    post {
        try {
            val pick = getPicassoClient()
                .load(url)

            if(needCenterCrop){
                pick.centerCrop()
                    .resize(viewSize ?: width, viewSize ?: height)
            }

            if (transformation != null) {
                pick.transform(transformation)
            }
            if (errorIcon != null) {
                if (errorIcon is Int) pick.error(errorIcon)
                if (errorIcon is Drawable) pick.error(errorIcon)
            }
            if (placeholder != null) {
                if (placeholder is Int) pick.placeholder(placeholder)
                if (placeholder is Drawable) pick.placeholder(placeholder)
            }
            if (policy != null) {
                pick.memoryPolicy(policy)
            }
            pick.into(this)
        } catch (e: java.lang.Exception) {
        }

    }
}

fun ImageView.load(
    url: Uri?,
    transformation: Transformation? = null,
    viewSize: Int? = null,
    errorIcon: Any? = null,
    placeholder: Any? = null
) {
    if (url == null) {

        if (errorIcon != null) {
            if (errorIcon is Int) this.setImageResource(errorIcon)
            if (errorIcon is Drawable) this.setImageDrawable(errorIcon)
        }

        return
    }

    post {
        try {


            val pick = getPicassoClient()
                .load(url)
                .centerCrop()
                .resize(viewSize ?: width, viewSize ?: height)

            if (transformation != null) {
                pick.transform(transformation)
            }
            if (errorIcon != null) {
                if (errorIcon is Int) pick.error(errorIcon)
                if (errorIcon is Drawable) pick.error(errorIcon)
            }
            if (placeholder != null) {
                if (placeholder is Int) pick.placeholder(placeholder)
                if (placeholder is Drawable) pick.placeholder(placeholder)
            }

            pick.into(this)
        } catch (e: java.lang.Exception) {
        }
    }
}


fun ImageView.loadFromAssets(path: String) {
    try {
        val d = Drawable.createFromStream(
            this.context!!.assets.open(path), null
        )
        this.setImageDrawable(d)
    } catch (e: Exception) {
    }


}





