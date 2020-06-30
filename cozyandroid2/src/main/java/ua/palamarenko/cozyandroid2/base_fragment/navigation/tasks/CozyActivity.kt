package ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ua.palamarenko.cozyandroid2.CozyLibrarySettings
import ua.palamarenko.cozyandroid2.base_fragment.navigation.NavigateActivity
import ua.palamarenko.cozyandroid2.base_fragment.navigation.ReflectionUtils
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.activity.HostActivity
import ua.palamarenko.cozyandroid2.tools.PreferencesProvider
import ua.palamarenko.cozyandroid2.tools.locale.updateResources
import java.util.*

open class CozyActivity<T : CozyViewModel> : NavigateActivity() {


    override fun attachBaseContext(newBase: Context) {
        if (CozyLibrarySettings.applocale != null) {
            super.attachBaseContext(updateResources(newBase, CozyLibrarySettings.applocale!!))
        } else {
            super.attachBaseContext(newBase)
        }
    }


    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        if (CozyLibrarySettings.applocale != null) {
            val config = Configuration(resources.configuration);
            Locale.setDefault(CozyLibrarySettings.applocale!!);
            config.setLocale(CozyLibrarySettings.applocale);
            super.applyOverrideConfiguration(config)
        } else {
            super.applyOverrideConfiguration(overrideConfiguration)
        }

    }


    private val POPUP_TAG = "POPUP_TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm().tm.task.observe(this, Observer { observeTasks(it!!.id, it.data, it.rule) })
    }

    private fun observeTasks(id: Int, data: Any, bundle: Bundle) {

        when (id) {
            SHOW_PROGRESS -> showProgress(data as Boolean)
            NAVIGATE -> navigate(data, bundle)
            TOAST -> showToast(data as String)
            START_ACTIVITY -> changeActivity(data, bundle)
            BACK_PRESS -> onBackPressed(data as? Class<*>)
            CUSTOM_ACTION -> customAction(data as? CustomActionCallback)
            SHOW_POPUP -> showPopup(data, supportFragmentManager)
            FINISH_ACTIVITY -> finish()
            SHOW_SNACKBAR -> showSnackbar(findViewById(android.R.id.content), data as SnackbarPopup)
            else -> observeCustomTasks(id, data, bundle)
        }
    }

    open fun observeCustomTasks(id: Int, data: Any, bundle: Bundle) {
        CozyLibrarySettings.customListener?.observeCustomTasks(this, id, data, bundle)
    }

    fun task(id: Int, data: Any = 0, rule: Bundle = Bundle()) {
        vm().tm.task(id, data, rule)
    }


    open fun customAction(callback: CustomActionCallback?) {
        callback?.listener?.invoke(this)
    }


    fun vm(): T {
        val cl: Class<T> = ReflectionUtils.getGenericParameterClass(this::class.java, 0) as Class<T>
        return ViewModelProviders.of(this).get(cl)
    }

    open fun showProgress(progress: Boolean) {
        showDefaultProgress(progress, this)
    }


    open fun navigate(fragment: Any, bundle: Bundle = Bundle()) {
        when (fragment) {
            is Fragment -> navigator.replaceFragment(fragment, bundle)
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


    open fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    open fun changeActivity(data: Any, intentBundle: Bundle) {
        if (data is Class<*>) {
            val intent = Intent(this, data)
            intent.putExtras(intentBundle)
            startActivity(intent)
        } else {
            startActivity(data as Intent)
        }
    }

}