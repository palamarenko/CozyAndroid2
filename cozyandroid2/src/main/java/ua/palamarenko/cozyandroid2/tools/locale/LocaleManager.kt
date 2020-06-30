package ua.palamarenko.cozyandroid2.tools.locale

import android.content.Context
import android.content.res.Configuration
import java.util.*

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
