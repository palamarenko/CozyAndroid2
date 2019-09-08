package ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import ua.palamarenko.cozyandroid2.CozyLibrarySettings


fun showPopup(data: Any, fragmentManager: FragmentManager?) {

    when (data) {
        is Popup -> showDefaultPopup(data,fragmentManager)
        is CozyFullPopup<*> -> data.show(fragmentManager)
        is CozyBottomSheets<*> -> data.show(fragmentManager)
    }
}

fun showDefaultProgress(progress: Boolean, activity : FragmentActivity?) {
    if(activity==null){
        return
    }
    if (progress) {
        ProgressView.displayProgressDialog(activity, false)
    } else {
        ProgressView.hideProgressDialog(activity)
    }
}




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

    popup.setShortPopup(data.generationShortPopup(CozyLibrarySettings.appContext!!))

    popup.show(fragmentManager)
}