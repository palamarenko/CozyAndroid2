package ua.palamarenko.cozyandroid2.tools

import android.graphics.Typeface
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import androidx.annotation.ColorInt


enum class SPAN { BOLD, ITALIC, UNDERLINE, NORMAL, STRIKE }


fun String.makeCharSequence(span: SPAN = SPAN.NORMAL, @ColorInt color: Int? = null): CharSequence {

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

operator fun CharSequence.plus(b: CharSequence): CharSequence {
    return TextUtils.concat(this,b)
}



