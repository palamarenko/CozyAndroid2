package ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tbruyelle.rxpermissions2.RxPermissions
import ua.palamarenko.cozyandroid2.base_fragment.navigation.NavigateActivity
import ua.palamarenko.cozyandroid2.base_fragment.navigation.Navigator

abstract class CozyFullPopup<T : CozyViewModel> : CozyBasePopup<T>() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm().tm.task.observe(this, Observer { observeTasks(it!!.id, it.data, it.rule) })
    }


    fun requestPermission(): RxPermissions {
        if (activity != null && activity is NavigateActivity) {
            return (activity as NavigateActivity).request()
        }

        throw IllegalStateException("Use navigate activity")
    }



    private fun observeTasks(id: Int, data: Any, bundle: Bundle) {

        when (id) {
            SHOW_PROGRESS -> showProgress(data as Boolean)
            NAVIGATE -> navigate(data as Fragment, bundle)
            TOAST -> showToast(data as String)
            START_ACTIVITY -> changeActivity(data as Class<*>, bundle)
            BACK_PRESS ->  onBackPress(data as? Class<*>)
            DISMISS -> dismiss()
            CUSTOM_ACTION -> customAction(data)
        }
    }

    open fun customAction(obj: Any) {

    }

    protected fun hideKeyboard() {
        val view = activity!!.currentFocus
        if (view != null) {
            val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }


    open fun navigate(fragment: Fragment, bundle: Bundle = Bundle()) {
        getNavigator().replaceFragment(fragment, bundle)
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
    }


    open fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }


    open fun changeActivity(activityClass: Class<*>, intentBundle: Bundle) {
        val intent = Intent(context, activityClass)
        intent.putExtras(intentBundle)
        startActivity(intent)
        activity?.finish()
    }
}