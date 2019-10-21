package ua.palamarenko.cozyandroid2.tools

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import androidx.annotation.RequiresPermission
import ua.palamarenko.cozyandroid2.CozyLibrarySettings

object CozyHelper {
    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    fun isNetworkConnected(): Boolean {
        val cm =
            CozyLibrarySettings.appContext!!.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        return cm?.activeNetworkInfo != null
    }
}