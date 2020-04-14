package ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.google.gson.JsonSerializer
import ua.palamarenko.cozyandroid2.CozyLibrarySettings
import ua.palamarenko.cozyandroid2.base_fragment.navigation.*
import ua.palamarenko.cozyandroid2.image_picker.ImagePicker
import ua.palamarenko.cozyandroid2.image_picker.ImagePickerRequest
import ua.palamarenko.cozyandroid2.tools.click


abstract class CozyFragment<T : CozyViewModel> : CozyBaseFragment<T>(), BackPress {

    private val POPUP_TAG = "POPUP_TAG"
    private val RESULT_KEY = "COZY_RESULT_KEY"
    private val COZY_RESULT_CLASS_NAME = "COZY_RESULT_CLASS_NAME"
    val RESULT_ACTIVITY_CODE = 99
    var activityResultCallBack: ((Intent) -> Unit)? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vm().tm.task.observe(this, Observer { observeTasks(it!!.id, it.data, it.rule) })
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    private fun observeTasks(id: Int, data: Any, bundle: Bundle) {

        when (id) {
            SHOW_PROGRESS -> showProgress(data as Boolean)
            NAVIGATE -> navigate(data as Fragment, bundle)
            TOAST -> showToast(data as String)
            START_ACTIVITY -> changeActivity(data, bundle)
            BACK_PRESS -> onBackPress(data as? Class<*>)
            CUSTOM_ACTION -> customAction(data as? ActivityCallBack)
            FINISH_ACTIVITY -> activity?.finish()
            SHOW_POPUP -> showPopup(data, fragmentManager)
            START_ACTIVITY_FOR_RESAULT -> startResultActivityRout(data, bundle)
            SET_RESULT -> setResult(data)
            SHOW_SNACKBAR -> view?.apply { showSnackbar(this, data as SnackbarPopup) }
            REQUEST_PERMISSION -> checkPermission(data as PermissionModel)
            CLEAR_FRAGMENTS -> fragmentManager?.popBackStack(
                null,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
            OPEN_LINK -> openLink(data as String)
            PICK_IMAGE -> ImagePicker.pickImage(
                this,
                (data as ImagePickerRequest).strings,
                data.callback,
                data.cropMode
            )
            else -> observeCustomTasks(id, data, bundle)
        }
    }


    open fun observeCustomTasks(id: Int, data: Any, bundle: Bundle) {
        activity?.apply {
            CozyLibrarySettings.customListener?.observeCustomTasks(this, id, data, bundle)
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


    private fun onBackPress(fragment: Class<*>?) {
        if (activity?.isDestroyed != true && activity is NavigateActivity) {
            (activity as NavigateActivity).onBackPressed(fragment)
        }
    }

    private fun checkPermission(data: PermissionModel) {
        permission({ data.callBack.invoke(it) }, *data.permission)
    }

    open fun customAction(callBack: ActivityCallBack?) {
        if (activity != null && activity is CozyActivity<*>) {
            callBack?.listener?.invoke(activity as CozyActivity<*>)
        }
    }


    fun task(id: Int, data: Any? = 0, rule: Bundle = Bundle()) {
        vm().tm.task(id, data ?: 0, rule)
    }


    open fun showProgress(progress: Boolean) {
        showDefaultProgress(progress, activity)
    }

    open fun navigate(fragment: Fragment, bundle: Bundle) {
        getNavigator().replaceFragment(fragment, bundle)
    }

    open fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }


    open fun startResultActivityRout(data: Any, intentBundle: Bundle) {
        if (data is ActivityResult) {
            startResultActivity(data, intentBundle)
            return
        }

        if (data is ActivityResultResponse<*>) {
            startResultActivity(data, intentBundle)
            return
        }

        throw IllegalStateException("Please use ActivityResult or ActivityResultResponse")
    }

    open fun startResultActivity(data: ActivityResult, intentBundle: Bundle) {
        if (data.activity is Class<*>) {
            val intent = Intent(context, data.activity)
            intent.putExtras(intentBundle)
            startActivityForResult(intent, RESULT_ACTIVITY_CODE)
        } else {
            startActivityForResult(data.activity as Intent, RESULT_ACTIVITY_CODE)
        }
        activityResultCallBack = data.callBack
    }


    open fun <RESULT : Any> startResultActivity(
        data: ActivityResultResponse<RESULT>,
        intentBundle: Bundle
    ) {


        if (data.activity is Class<*>) {
            val intent = Intent(context, data.activity)
            intent.putExtras(intentBundle)
            startActivityForResult(intent, RESULT_ACTIVITY_CODE)
        } else {
            startActivityForResult(data.activity as Intent, RESULT_ACTIVITY_CODE)
        }
        activityResultCallBack = {

            val json = it.getStringExtra(RESULT_KEY)
            val cl: Class<RESULT> = Class.forName(it.getStringExtra(COZY_RESULT_CLASS_NAME)!!) as Class<RESULT>
            val t = Gson().fromJson(json, cl)
            data.callBack.invoke(t)
        }
    }

    open fun setResult(data: Any) {
        if (data is Intent) {
            activity?.setResult(Activity.RESULT_OK, data)
        } else {
            val intent = Intent()
            intent.putExtra(RESULT_KEY, Gson().toJson(data))
            intent.putExtra(COZY_RESULT_CLASS_NAME, data::class.java.canonicalName)
            activity?.setResult(Activity.RESULT_OK, intent)
        }

        activity?.finish()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_ACTIVITY_CODE && activityResultCallBack != null && data != null) {
            activityResultCallBack!!.invoke(data)
            activityResultCallBack = null
            return
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activityResultCallBack = null
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


    fun ImageView.backButton(cl: Class<*>? = null) {
        click {
            task(BACK_PRESS, cl)
        }
    }


    private fun getNavigator(): Navigator {

        try {
            return (activity as NavigateActivity).navigator

        } catch (e: Exception) {
            throw IllegalStateException("Use only NAVIGATE Activity")

        }
    }
}


class PermissionModel(val permission: Array<String>, val callBack: (Boolean) -> Unit)
class ActivityResult(val activity: Any, val callBack: (Intent) -> Unit)
class ActivityResultResponse<T : Any>(val activity: Any, val callBack: (T) -> Unit)


class ActivityCallBack(val listener: (CozyActivity<*>) -> Unit)
