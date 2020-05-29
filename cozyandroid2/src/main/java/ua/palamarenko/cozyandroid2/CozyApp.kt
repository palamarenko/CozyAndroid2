package ua.palamarenko.cozyandroid2

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.CozyPopup
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.popups.CozyReusePopup


object CozyLibrarySettings {
    var appContext: Context? = null
    var popupMaker: () -> CozyReusePopup = { CozyPopup() }
    var customListener : CustomTasksListener? = null


    fun setPopupRealization(popupClass: Class<out CozyReusePopup>): CozyLibrarySettings {
        popupMaker = { popupClass.newInstance() }
        return this
    }

    fun setCustomTasksListener(listener: CustomTasksListener): CozyLibrarySettings {
        customListener = listener
        return this
    }
}

abstract class CustomTasksListener {
   abstract fun observeCustomTasks(activity : FragmentActivity, id: Int, data: Any, bundle: Bundle)
}


class CozyLibrary {

    companion object {

        fun init(context: Context): CozyLibrarySettings {
            CozyLibrarySettings.appContext = context
            return CozyLibrarySettings
        }
    }

}