package ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatDialogFragment
import ua.palamarenko.cozyandroid2.R

object ProgressView {


    private var dialog: ProgressDialog? = null
    private val tag = "PROGRESS_FRAGMENT"
    private var dialogShown = false

    fun displayProgressDialog(activity: androidx.fragment.app.FragmentActivity?, white: Boolean = false) {
        if (!dialogShown) {
            try {
                if (activity != null && activity.supportFragmentManager.findFragmentByTag(tag) == null && (dialog == null || !dialog!!.isAdded)) {
                    dialogShown = true

                    if (dialog == null) {
                        dialog = ProgressDialog()
                    }

                    dialog?.show(activity.supportFragmentManager, tag)
                }
            } catch (e: Exception) {
            }
        } else {
            Log.e("progress", "progressDialog cannot be shown twice")
        }
    }

    /**
     * Hides the progress dialog
     */
    fun hideProgressDialog(activity: androidx.fragment.app.FragmentActivity?) {
        try {
            dialogShown = false
            dialog?.dismissAllowingStateLoss()

            if (activity?.supportFragmentManager?.findFragmentByTag(tag) != null) {

                val popup = activity.supportFragmentManager.findFragmentByTag(tag)

                try {
                    activity.supportFragmentManager.beginTransaction().remove(popup!!).commitAllowingStateLoss()
                }catch (e : Exception){}
            }
            // added but not tested
            dialog = null

        } catch (e: Exception) {
        }
    }

}

class ProgressDialog : AppCompatDialogFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false
        return inflater.inflate(R.layout.view_progress, container, false)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }


    fun show(manager: androidx.fragment.app.FragmentManager) {
        show(manager, "")
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val window = dialog.window
            if (window != null) {
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }


    override fun dismiss() {
        try {
            if (activity != null && !activity!!.isDestroyed && fragmentManager != null && isAdded) {
                super.dismiss()
            }
        } catch (ignored: Exception) {
        }
    }

}
