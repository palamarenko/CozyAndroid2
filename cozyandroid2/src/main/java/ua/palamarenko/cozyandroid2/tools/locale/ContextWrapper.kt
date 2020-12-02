package ua.palamarenko.cozyandroid2.tools.locale

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import java.util.*

class LanguageSwipe(base: Context?) : ContextWrapper(base) {
    companion object {
        fun wrap(context: Context, newLocale: Locale?): ContextWrapper {
            var context = context
            val res = context.resources
            val configuration = Configuration(res.configuration)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                configuration.setLocale(newLocale)
                val localeList = LocaleList(newLocale)
                LocaleList.setDefault(localeList)
                configuration.setLocales(localeList)
                context = context.createConfigurationContext(configuration)
            } else {
                configuration.setLocale(newLocale)
                context = context.createConfigurationContext(configuration)
            }
            return ContextWrapper(context)
        }
    }
}
