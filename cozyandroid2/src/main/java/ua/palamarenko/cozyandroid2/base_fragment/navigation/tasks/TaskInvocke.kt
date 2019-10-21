package ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks

import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import ua.palamarenko.cozyandroid2.CozyLibrarySettings
import com.google.android.material.snackbar.Snackbar
import androidx.core.view.accessibility.AccessibilityEventCompat.setAction


fun showPopup(data: Any, fragmentManager: FragmentManager?) {

    when (data) {
        is Popup -> showDefaultPopup(data, fragmentManager)
        is CozyFullPopup<*> -> data.show(fragmentManager)
        is CozyBottomSheets<*> -> data.show(fragmentManager)
    }
}

fun showDefaultProgress(progress: Boolean, activity: FragmentActivity?) {
    if (activity == null) {
        return
    }
    if (progress) {
        ProgressView.displayProgressDialog(activity, false)
    } else {
        ProgressView.hideProgressDialog(activity)
    }
}


fun showSnackbar(baseView: View, popup: SnackbarPopup) {

    val snackbar = Snackbar.make(baseView, convertAnyToTitle(popup.title), Snackbar.LENGTH_LONG)

    if (popup.action != null) {

        val actionName =
            if (convertAnyToTitle(popup.actionName).isNotEmpty()) convertAnyToTitle(popup.actionName) else "Ok"

        snackbar.setAction(actionName) {
            popup.action?.invoke()
        }

        if(popup.actionColor!=null){
            snackbar.setActionTextColor(popup.actionColor!!)
        }
    }

    snackbar.show()
}

class SnackbarPopup(
    val title: Any = "",
    var action: (() -> Unit)? = null,
    var actionName: Any = "",
    @ColorInt var actionColor : Int? = null
)


private fun showDefaultPopup(data: Popup?, fragmentManager: FragmentManager?) {
    checkNotNull(data) { "Use Popup class" }

    val popup = CozyLibrarySettings.popupMaker.invoke()


    if (data.positiveAction != null) {
        popup.setPositiveCallBack {
            data.positiveAction!!.invoke(it)
        }
    }

    if (data.negativeAction != null) {
        popup.setNegativeCallBack {
            data.negativeAction!!.invoke(it)
        }
    }


    if (data.dissmisAction != null) {
        popup.setDissmisCallBack {
            data.dissmisAction!!.invoke(it)
        }
    }


    if (data.finishAction != null) {
        popup.setFinishCallBack {
            data.finishAction!!.invoke(it)
        }
    }

    popup.setShortPopup(data.generationShortPopup())

    popup.show(fragmentManager)
}