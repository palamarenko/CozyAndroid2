package ua.palamarenko.cozyandroid2.tools

import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.style.*
import android.view.View
import androidx.annotation.ColorInt


enum class SPAN { BOLD, ITALIC, UNDERLINE, NORMAL, STRIKE }


fun String.makeCharSequence(
    span: SPAN = SPAN.NORMAL, @ColorInt color: Int? = null
): CharSequence {

    return when (span) {
        SPAN.BOLD -> {
            SpannableString(this).apply {
                setSpan(StyleSpan(Typeface.BOLD), 0, this.length, 0)


            }
        }
        SPAN.ITALIC -> {
            SpannableString(this).apply {
                setSpan(StyleSpan(Typeface.ITALIC), 0, this.length, 0)
            }
        }

        SPAN.NORMAL -> {
            SpannableString(this).apply {
                setSpan(StyleSpan(Typeface.NORMAL), 0, this.length, 0)
            }
        }

        SPAN.UNDERLINE -> {
            SpannableString(this).apply {
                setSpan(UnderlineSpan(), 0, this.length, 0)
            }
        }
        SPAN.STRIKE -> {
            SpannableString(this).apply {
                setSpan(StrikethroughSpan(), 0, this.length, 0)
            }
        }


    }.apply {
        if (color != null) {
            setSpan(ForegroundColorSpan(color), 0, this.length, 0)
        }
    }
}


fun CharSequence.setClick(color: Int? = null, click: (() -> Unit)): CharSequence {
    val st = SpannableString(this)

    val clickableSpan: ClickableSpan = object : ClickableSpan() {
        override fun onClick(textView: View) {
            click.invoke()
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            if (color != null) {
                ds.color = color
            }
        }
    }
    st.setSpan(clickableSpan, 0, this.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    return st
}


operator fun CharSequence.plus(b: CharSequence): CharSequence {
    return TextUtils.concat(this, b)
}



