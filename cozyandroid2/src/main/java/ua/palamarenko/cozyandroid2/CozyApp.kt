package ua.palamarenko.cozyandroid2

import android.app.Application
import android.content.Context


var appContext : Context? = null


class CozyLibrary {


    companion object{

        fun init(context: Context) {
            appContext = context
        }
    }


}