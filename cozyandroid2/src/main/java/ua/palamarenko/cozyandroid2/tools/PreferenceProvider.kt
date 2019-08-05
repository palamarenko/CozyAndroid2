package ua.palamarenko.cozyandroid2.tools

import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import ua.palamarenko.cozyandroid2.appContext
import java.lang.reflect.Modifier.TRANSIENT

class PreferencesProvider {

    companion object {
        val manager = PreferenceManager.getDefaultSharedPreferences(appContext)

        fun putString(key: String, value: String) {
            manager.edit().apply {
                putString(key, value)
                apply()
            }
        }


        fun putBool(key: String, value: Boolean) {
            manager.edit().apply {
                putBoolean(key, value)
                apply()
            }
        }

        fun putInt(key: String, value: Int) {
            manager.edit().apply {
                putInt(key, value)
                apply()
            }
        }

        fun putLong(key: String, value: Long) {
            manager.edit().apply {
                putLong(key, value)
                apply()
            }
        }

        fun clearKey(key: String) {
            manager.edit().apply {
                putString(key, "")
                apply()
            }
        }

        fun putObject(key: String, data: Any) {
            manager.edit().apply {
                val gson = GsonBuilder()
                    .excludeFieldsWithModifiers(TRANSIENT)
                    .create()
                val st = gson.toJson(data)
                putString(key, st)
                apply()
            }
        }

        fun <T> getObject(key: String, cl: Class<T>): T? {
            val st = manager.getString(key, "")
            if (st.isEmpty()) {
                return null
            }
            return Gson().fromJson(st, cl)
        }


        fun readString(key: String, defValue: String = ""): String {
            return manager.getString(key, defValue)?:""
        }

        fun readBool(key: String, default : Boolean = false): Boolean {
            return manager.getBoolean(key, default)
        }

        fun readInt(key: String): Int {
            return manager.getInt(key, 0)
        }

        fun readLong(key: String): Long {
            return manager.getLong(key, 0L)
        }
    }





}
