package ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks

import android.app.Activity
import android.content.ComponentName
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
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import ua.palamarenko.cozyandroid2.CozyLibrarySettings
import ua.palamarenko.cozyandroid2.base_fragment.navigation.*
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.activity.HostActivity
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.popups.PopupCell
import ua.palamarenko.cozyandroid2.image_picker.*
import ua.palamarenko.cozyandroid2.tools.click
import ua.palamarenko.cozyandroid2.tools.image_viewer.getImageViewBundle


val hostActivityFragment = "HostActivityFragment"

abstract class CozyFragment<T : CozyViewModel> : CozyBaseFragment<T>(), BackPress {

    private val POPUP_TAG = "POPUP_TAG"
    private val RESULT_KEY = "COZY_RESULT_KEY"
    private val COZY_RESULT_CLASS_NAME = "COZY_RESULT_CLASS_NAME"
    val RESULT_ACTIVITY_CODE = 99


    var activityResultCallBack: ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit)? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vm().tm.task.observe(
            viewLifecycleOwner,
            Observer { observeTasks(it!!.id, it.data, it.rule) })
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    private fun observeTasks(id: Int, data: Any, bundle: Bundle) {

        when (id) {
            SHOW_PROGRESS -> showProgress(data as Boolean)
            NAVIGATE -> navigate(data, bundle)
            TOAST -> showToast(data as String)
            START_ACTIVITY -> changeActivity(data, bundle)
            BACK_PRESS -> onBackPress(data as? Class<*>)
            CUSTOM_ACTION -> customAction(data as? CustomActionCallback)
            FINISH_ACTIVITY -> activity?.finish()
            SHOW_POPUP -> showPopup(data, parentFragmentManager)
            START_ACTIVITY_FOR_RESAULT -> startResultActivityResault(data, bundle)
            SET_RESULT -> setResult(data)
            SHOW_SNACKBAR -> view?.apply { showSnackbar(this, data as SnackbarPopup) }
            REQUEST_PERMISSION -> checkPermission(data as RequestPermissionCallback)
            CLEAR_FRAGMENTS -> parentFragmentManager.popBackStack(
                null,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
            OPEN_LINK -> openLink(data as String)
            PICK_FILE -> pickFile(data as ImageRequest)
            IMAGE_VIEWER -> {
                startActivity(
                    getImageViewBundle(
                        (data as ImageViewerRequest).list,
                        data.url,
                        data.title
                    )
                )
            }
            else -> observeCustomTasks(id, data, bundle)
        }
    }


    private fun pickFile(data: ImageRequest) {

        when (data) {
            is PickSingleImagePopupRequest -> {
                FilePicker.pickSingleImage(
                    this,
                    data.strings,
                    data.callback,
                    data.cropMode
                )
            }
            is PickMultipleImageRequest -> {
                activityResultCallBack = FilePicker.pickMultipleImage(this, data.callback)
            }
            is PickFileRequest -> {
                activityResultCallBack = FilePicker.pickFile(this, data)
            }
            is PickPhotoRequest -> {
                FilePicker.pickJustPhoto(this,data.cropMode,data.callback)
            }
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

    private fun checkPermission(data: RequestPermissionCallback) {
        permission({ data.callBack.invoke(it) }, *data.permission)
    }

    open fun customAction(callback: CustomActionCallback?) {
        if (activity != null && activity is CozyActivity<*>) {
            callback?.listener?.invoke(activity as CozyActivity<*>)
        }
    }


    fun task(id: Int, data: Any? = 0, rule: Bundle = Bundle()) {
        vm().tm.task(id, data ?: 0, rule)
    }


    open fun showProgress(progress: Boolean) {
        showDefaultProgress(progress, activity)
    }

    open fun navigate(fragment: Any, bundle: Bundle) {

        when (fragment) {
            is Fragment -> getNavigator().replaceFragment(fragment, bundle)
            is Int -> findNavController().navigate(fragment,bundle)
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
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }


    open fun startResultActivityResault(data: Any, intentBundle: Bundle) {
        if (data is StartActivityCallback) {
            startResultActivity(data, intentBundle)
            return
        }

        if (data is StartActivityTypeCallback<*>) {
            startResultActivity(data, intentBundle)
            return
        }

    }

    open fun startResultActivity(data: StartActivityCallback, intentBundle: Bundle) {

        activityResultCallBack = { i: Int, i1: Int, intent: Intent? ->
            data.callBack.invoke(intent!!)
        }

        when (data.activity) {
            is Class<*> -> {
                val intent = Intent(context, data.activity)
                intent.putExtras(intentBundle)
                startActivityForResult(intent, RESULT_ACTIVITY_CODE)
            }
            is Intent -> {
                startActivityForResult(data.activity, RESULT_ACTIVITY_CODE)
            }
            is NavigateNewActivity -> {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.component = ComponentName(
                    CozyLibrarySettings.appContext!!,
                    HostActivity::class.java
                )
                intentBundle.putString(hostActivityFragment, data.activity.fragment.canonicalName)
                intent.putExtras(intentBundle)
                startActivityForResult(intent,RESULT_ACTIVITY_CODE)
            }
        }
    }


    open fun <RESULT : Any> startResultActivity(
        data: StartActivityTypeCallback<RESULT>,
        intentBundle: Bundle
    ) {

        if (data.activity is Class<*>) {
            val intent = Intent(context, data.activity)
            intent.putExtras(intentBundle)
            startActivityForResult(intent, RESULT_ACTIVITY_CODE)
        } else {
            startActivityForResult(data.activity as Intent, RESULT_ACTIVITY_CODE)
        }
        activityResultCallBack = { i: Int, i1: Int, intent: Intent? ->
            val json = intent!!.getStringExtra(RESULT_KEY)
            val cl: Class<RESULT> =
                Class.forName(intent.getStringExtra(COZY_RESULT_CLASS_NAME)!!) as Class<RESULT>
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
        if (activityResultCallBack != null && data != null) {
            activityResultCallBack!!.invoke(requestCode, resultCode, data)
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


class RequestPermissionCallback(val permission: Array<String>, val callBack: (Boolean) -> Unit)
class NavigateNewActivity(val fragment: Class<*>)
class StartActivityCallback(val activity: Any, val callBack: (Intent) -> Unit)
class StartActivityTypeCallback<T : Any>(val activity: Any, val callBack: (T) -> Unit)
class ImageViewerRequest(
    val url: String,
    val list: List<String>? = null,
    val title: String = "Image"
)

class ShowProgressCallBack(val progress: Boolean, val dismissCallBack: (() -> Unit)? = null)
class ShowPopupWindow(val anchor: View, val list: List<WindowPopupItems>, val click: (Int) -> Unit)
class WindowPopupItems(val title: String, val id: Int)

class ShowPopupWindowCustomCell(val anchor: View, val list: List<PopupCell>)
class CustomActionCallback(val listener: (CozyActivity<*>) -> Unit)
