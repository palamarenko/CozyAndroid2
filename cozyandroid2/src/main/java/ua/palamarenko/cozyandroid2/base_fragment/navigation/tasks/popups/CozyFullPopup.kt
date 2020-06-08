package ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.popups

import android.app.Dialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tbruyelle.rxpermissions2.RxPermissions
import ua.palamarenko.cozyandroid2.CozyLibrarySettings
import ua.palamarenko.cozyandroid2.R
import ua.palamarenko.cozyandroid2.base_fragment.navigation.NavigateActivity
import ua.palamarenko.cozyandroid2.base_fragment.navigation.Navigator
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.*
import ua.palamarenko.cozyandroid2.tools.image_viewer.getImageViewBundle
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.activity.HostActivity

abstract class CozyFullPopup<T : CozyViewModel> : CozyBasePopup<T>() {


    private var callViewCreated: Boolean = false

    open fun onRestartScreen() {}
    open fun onStartScreen() {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm().tm.task.observe(viewLifecycleOwner, Observer { observeTasks(it!!.id, it.data, it.rule) })

        if (!callViewCreated) {
            onStartScreen()
        } else {
            onRestartScreen()
        }

        callViewCreated = true
    }

    override fun onDestroy() {
        super.onDestroy()
        callViewCreated = false

    }


    fun requestPermission(): RxPermissions {
        if (activity != null && activity is NavigateActivity) {
            return (activity as NavigateActivity).request()
        }

        throw IllegalStateException("Use navigate activity")
    }

    open fun observeCustomTasks(id: Int, data: Any, bundle: Bundle) {
        activity?.apply {
            CozyLibrarySettings.customListener?.observeCustomTasks(this,id,data,bundle)
        }
    }



    private fun observeTasks(id: Int, data: Any, bundle: Bundle) {

        when (id) {
            SHOW_PROGRESS -> showProgress(data as Boolean)
            NAVIGATE -> navigate(data, bundle)
            TOAST -> showToast(data as String)
            START_ACTIVITY -> changeActivity(data, bundle)
            BACK_PRESS ->  onBackPress(data as? Class<*>)
            DISMISS -> dismiss()
            FINISH_ACTIVITY -> activity?.finish()
            SHOW_POPUP -> showPopup(
                data,
                fragmentManager
            )
            CUSTOM_ACTION -> customAction(data as? CustomActionCallback)
            SHOW_SNACKBAR -> view?.apply {
                showSnackbar(
                    this,
                    data as SnackbarPopup
                )
            }
            OPEN_LINK -> openLink(data as String)
            IMAGE_VIEWER -> {
                startActivity(
                    getImageViewBundle(
                        (data as ImageViewerRequest).list,
                        data.url,
                        data.title
                    )
                )
            }
            else -> observeCustomTasks(id,data,bundle)
        }
    }

    private fun openLink(link: String) {
        val formattedLink = if (!link.startsWith("http://") && !link.startsWith("https://")) {
            "http://$link";
        } else {
            link
        }
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(formattedLink))
        startActivity(browserIntent)
    }

    open fun customAction(callback: CustomActionCallback?) {
        if (activity != null && activity is CozyActivity<*>) {
            callback?.listener?.invoke(activity as CozyActivity<*>)
        }
    }

    protected fun hideKeyboard() {
        val view = activity!!.currentFocus
        if (view != null) {
            val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }


    open fun navigate(fragment: Any, bundle: Bundle = Bundle()) {
        when (fragment) {
            is Fragment -> getNavigator().replaceFragment(fragment, bundle)
            is NavigateNewActivity -> {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.component = ComponentName(
                    CozyLibrarySettings.appContext!!,
                    HostActivity::class.java
                )
                bundle.putString(hostActivityFragment, fragment.fragment.canonicalName)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }

    }

    private fun getNavigator(): Navigator {

        try {
            return (activity as NavigateActivity).navigator

        } catch (e: Exception) {
            throw IllegalStateException("Use only NAVIGATE Activity")

        }
    }


    private fun onBackPress(fragment: Class<*>?) {
        if (activity?.isDestroyed != true && activity is NavigateActivity) {
            (activity as NavigateActivity).onBackPressed(fragment)
        }
    }



    fun task(id: Int, data: Any = "") {
        vm().tm.task(id, data)
    }

    fun task(id: Int, data: Any, rule: Bundle) {
        vm().tm.task(id, data, rule)
    }


    open fun showProgress(progress: Boolean) {
        showDefaultProgress(
            progress,
            activity
        )
    }


    open fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }


    open fun changeActivity(data: Any, intentBundle: Bundle) {
        if (data is Class<*>) {
            val intent = Intent(context, data)
            intent.putExtras(intentBundle)
            startActivity(intent)
        } else {
            startActivity(data as Intent)
        }
    }
}



abstract class CozyBottomSheets<T : CozyViewModel> : CozyFullPopup<T>() {
    private var bottomSheet: View? = null
    private var behavior: BottomSheetBehavior<*>? = null
    var canDrag = true
    var canCancelByClick = true
    private val mBottomSheetBehaviorCallback = object : BottomSheetBehavior.BottomSheetCallback() {

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_COLLAPSED || newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismissAllowingStateLoss()
                collapsed()
            }

            if (!canDrag) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    behavior?.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }



    open fun collapsed() {

    }



    override fun dismiss() {
        behavior!!.state = BottomSheetBehavior.STATE_HIDDEN
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (dialog != null && dialog?.window != null) {
            dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        v = inflater.inflate(R.layout.popup_bottom_sheet, container, false)
        val frameLayout = v.findViewById<FrameLayout>(R.id.container)
        bottomSheet = v.findViewById(R.id.bottom_sheet)
        frameLayout.addView(View.inflate(context, layout, null))
        val params = bottomSheet!!.layoutParams as CoordinatorLayout.LayoutParams
        behavior = params.behavior as BottomSheetBehavior<*>?
        v.findViewById<View>(R.id.main_lay).setOnClickListener {
            if(canCancelByClick)
                behavior!!.setState(BottomSheetBehavior.STATE_HIDDEN)
        }
        behavior!!.setBottomSheetCallback(mBottomSheetBehaviorCallback)
        Handler().postDelayed({ behavior!!.setState(BottomSheetBehavior.STATE_EXPANDED) }, 300)

        return v
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }



}