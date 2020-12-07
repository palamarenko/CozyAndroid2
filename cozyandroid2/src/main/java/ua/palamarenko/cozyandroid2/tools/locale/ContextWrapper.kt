package ua.palamarenko.cozyandroid2.tools.locale

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import java.util.*

class LanguageSwipe(base: Context?) : ContextWrapper(base) {
    companion object {
        fun wrap(context: Context, newLocale: Locale?): Context {
            val res = context.resources
            val configuration = Configuration(res.configuration)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                configuration.setLocale(newLocale)
                val localeList = LocaleList(newLocale)
                LocaleList.setDefault(localeList)
                configuration.setLocales(localeList)
                return context.createConfigurationContext(configuration)
            } else {
                configuration.setLocale(newLocale)
                return context.createConfigurationContext(configuration)
            }
        }



        fun updateResources(context: Context, locale : Locale): Context? {
            Locale.setDefault(locale)
            updateResourcesLocaleLegacy(context, locale)
            return updateResourcesLocale(context, locale)
        }



        fun updateResourcesLocale(context: Context, locale: Locale): Context {
            val configuration = Configuration(context.resources.configuration)
            configuration.setLocale(locale);
            return context.createConfigurationContext(configuration);
        }

        @SuppressWarnings("deprecation")
        fun updateResourcesLocaleLegacy(context: Context, locale: Locale): Context {
            val resources = context.resources;
            val configuration = resources.configuration;
            configuration.locale = locale;
            resources.updateConfiguration(configuration, resources.displayMetrics);
            return context;
        }

    }
}
