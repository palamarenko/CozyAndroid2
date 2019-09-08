package ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ua.palamarenko.cozyandroid2.CozyLibrarySettings
import ua.palamarenko.cozyandroid2.base_fragment.navigation.NavigateActivity
import ua.palamarenko.cozyandroid2.base_fragment.navigation.ReflectionUtils

open class CozyActivity<T : CozyViewModel> : NavigateActivity() {
    private val POPUP_TAG = "POPUP_TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm().tm.task.observe(this, Observer { observeTasks(it!!.id, it.data, it.rule) })
    }

    private fun observeTasks(id: Int, data: Any, bundle: Bundle) {

        when (id) {
            SHOW_PROGRESS -> showProgress(data as Boolean)
            NAVIGATE -> navigate(data as Fragment, bundle)
            TOAST -> showToast(data as String)
            START_ACTIVITY -> changeActivity(data as Class<*>, bundle)
            BACK_PRESS -> onBackPressed(data as? Class<*>)
            CUSTOM_ACTION -> customAction(data as? ActivityCallBack)
            SHOW_POPUP -> showPopup(data, supportFragmentManager)
            FINISH_ACTIVITY -> finish()
            else -> observeCustomTasks(id, data, bundle)
        }
    }

    open fun observeCustomTasks(id: Int, data: Any, bundle: Bundle) {
        CozyLibrarySettings.customListener?.observeCustomTasks(this, id, data, bundle)
    }

    fun task(id: Int, data: Any = 0, rule: Bundle = Bundle()) {
        vm().tm.task(id, data, rule)
    }


    open fun customAction(callBack: ActivityCallBack?) {
        callBack?.listener?.invoke(this)
    }


    fun vm(): T {
        val cl: Class<T> = ReflectionUtils.getGenericParameterClass(this::class.java, 0) as Class<T>
        return ViewModelProviders.of(this).get(cl)
    }

    open fun showProgress(progress: Boolean) {
        showDefaultProgress(progress, this)
    }


    open fun navigate(fragment: Fragment, bundle: Bundle) {
        navigator.replaceFragment(fragment, bundle)
    }

    open fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    open fun changeActivity(activityClass: Class<*>, intentBundle: Bundle) {
        val intent = Intent(this, activityClass)
        intent.putExtras(intentBundle)
        startActivity(intent)
    }
}