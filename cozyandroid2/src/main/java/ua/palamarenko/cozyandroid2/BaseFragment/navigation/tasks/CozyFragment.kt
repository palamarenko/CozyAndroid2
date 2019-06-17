package ua.palamarenko.cozyandroid2.BaseFragment.navigation.tasks

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import ua.palamarenko.cozyandroid2.BaseFragment.navigation.BaseFragment
import ua.palamarenko.cozyandroid2.BaseFragment.navigation.NavigateActivity
import ua.palamarenko.cozyandroid2.BaseFragment.navigation.Navigator


abstract class CozyFragment<T : CozyViewModel> : BaseFragment<T>() {

    private val POPUP_TAG = "POPUP_TAG"


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
            CUSTOM_ACTION -> customAction(data)
            FINISH_ACTIVITY -> activity?.finish()
            SHOW_POPUP -> showPopup(data as? Popup)
        }
    }


    private fun onBackPress(fragment: Class<*>?) {
        if (activity?.isDestroyed != true && activity is NavigateActivity) {
            (activity as NavigateActivity).onBackPressed(fragment)
        }
    }

    open fun customAction(obj: Any) {}


    fun task(id: Int, data: Any = 0, rule: Bundle = Bundle()) {
        vm().tm.task(id, data, rule)
    }


    open fun showProgress(progress: Boolean) {
        if (progress) {
            ProgressView.displayProgressDialog(activity, false)
        } else {
            ProgressView.hideProgressDialog(activity)
        }
    }

    open fun navigate(fragment: Fragment, bundle: Bundle = Bundle()) {
        getNavigator().replaceFragment(fragment, true)
    }

    open fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }



    open fun changeActivity(data : Any, intentBundle: Bundle) {


        if(data is Class<*>){
            val intent = Intent(context, data)
            intent.putExtras(intentBundle)
            startActivity(intent)
        }else{
            startActivity(data as Intent)
        }

    }

    private fun showPopup(data: Popup?) {
        if(data==null){
            throw java.lang.IllegalStateException("Use Popup class")
        }

            val popup = CozyPopup()


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


            if(data.finishAction!=null){
                popup.setFinishCallBack {
                    data.finishAction!!.invoke(it)
                }
            }


            popup.setArgumentObject(CozyPopup.POPUP_MODEL,data.generationShortPopup())
                .show(fragmentManager)
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