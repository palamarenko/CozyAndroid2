package ua.palamarenko.cozyandroid2.tools

import java.text.SimpleDateFormat
import java.util.*

fun Long.formatDate(
    format: String = "dd-MM-yyyy HH:mm:ss",
    locale: Locale = Locale.US,
    convertFromLocal: Boolean
): String {
    val formatter = SimpleDateFormat(format, locale)
    if (convertFromLocal) {
        formatter.timeZone = TimeZone.getTimeZone("UTC")
    }

    return formatter.format(Date(this))
}

fun String.parseData(
    format: String = "dd-MM-yyyy HH:mm:ss",
    locale: Locale = Locale.US,
    convertToLocal: Boolean = true
): Long {

    val formatter = SimpleDateFormat(format, locale)

    if (convertToLocal) {
        formatter.timeZone = TimeZone.getTimeZone("UTC")
    }

    try {
        val date = formatter.parse(this)
        return date.time
    } catch (e: java.lang.Exception) {
    }

    return 0L
}


fun Date.formatDate(
    format: String = "dd-MM-yyyy HH:mm:ss",
    convertFromLocal: Boolean,
    locale: Locale = Locale.US
): String {
    val formatter = SimpleDateFormat(format, locale)

    if (convertFromLocal) {
        formatter.timeZone = TimeZone.getTimeZone("UTC")
    }

    return formatter.format(this)
}