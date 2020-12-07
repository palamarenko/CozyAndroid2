package ua.palamarenko.cozyandroid2.tools.locale

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import java.util.*

fun updateResources(context: Context, locale: Locale): Context {
    return LanguageSwipe.wrap(context, locale)
}







