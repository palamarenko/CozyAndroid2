package ua.palamarenko.cozyandroid2.BaseFragment.navigation

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.tbruyelle.rxpermissions2.RxPermissions

abstract class BaseFragment<T : BaseViewModel> : Fragment() {

    abstract val layout: Int

    private var fragmentView: View? = null
    private var callViewCreated: Boolean = false
    private var clearFocusView: EditText? = null


    fun vm(): T {
        val cl: Class<T> = ReflectionUtils.getGenericParameterClass(
            this::class.java,
            0
        ) as Class<T>
        return ViewModelProviders.of(this).get(cl)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if (fragmentView == null) {
            fragmentView = inflater.inflate(layout, null)
        }
        return fragmentView
    }

    override fun onDestroy() {
        super.onDestroy()
        callViewCreated = false

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!callViewCreated) {
            onViewCreated()
        } else {
            onRestartScreen()
        }

        callViewCreated = true
    }


    open fun onRestartScreen() {}


    protected fun hideKeyboard() {
        val view = activity!!.currentFocus
        if (view != null) {
            val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


    protected fun hideKeyboardAndFocus() {


        val view = activity!!.currentFocus
        if (view != null) {
            try {
                if (view.rootView != null && view.rootView is ViewGroup) {
                    if (clearFocusView != null) {
                        clearFocusView?.requestFocus()
                    } else {
                        val edit = EditText(context)
                        edit.width = 0
                        edit.height = 0
                        edit.id = View.generateViewId()
                        (view.rootView as ViewGroup).addView(edit)
                        val params = edit.layoutParams
                        params.height = 0
                        params.width = 0
                        edit.layoutParams = params
                        edit.requestFocus()
                        clearFocusView = edit
                    }
                }

            } catch (e: Exception) {
            }


            val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


    fun requestPermission(): RxPermissions {
        if (activity != null && activity is NavigateActivity) {
            return (activity as NavigateActivity).request()
        }

        throw IllegalStateException("Use navigate activity")
    }


    @SuppressLint("CheckResult")
    fun permission(listener : (Boolean) -> Unit,vararg  permissions: String){
        requestPermission().request(*permissions)
            .subscribe({
                listener.invoke(it)

            },{
                listener.invoke(false)
            })
    }



    open fun onViewCreated() {}

    fun setArgumentString(key: String, value: String): BaseFragment<T> {
        if (arguments == null) {
            arguments = Bundle()
        }

        arguments?.putString(key, value)
        return this
    }

    fun setArgumentDouble(key: String, value: Double): BaseFragment<T> {
        if (arguments == null) {
            arguments = Bundle()
        }

        arguments?.putDouble(key, value)
        return this
    }


    fun setArgumentInt(key: String, value: Int): BaseFragment<T> {
        if (arguments == null) {
            arguments = Bundle()
        }

        arguments?.putInt(key, value)
        return this
    }

    fun setArgumentLong(key: String, value: Long): BaseFragment<T> {
        if (arguments == null) {
            arguments = Bundle()
        }

        arguments?.putLong(key, value)
        return this
    }

    fun setArgumentBool(key: String, value: Boolean): BaseFragment<T> {
        if (arguments == null) {
            arguments = Bundle()
        }

        arguments?.putBoolean(key, value)
        return this
    }

    fun setArgumentObject(key: String, value: Any?): BaseFragment<T> {
        if (arguments == null) {
            arguments = Bundle()
        }
        if (value != null) arguments?.putString(key, Gson().toJson(value))
        return this
    }


    fun getArgumentString(key: String, fromActivity : Boolean = false): String {
        var value =  arguments?.getString(key) ?: ""

        if(value.isEmpty() && fromActivity){
            value =  activity?.intent?.getStringExtra(key)?:""
        }

        return value
    }

    fun getArgumentLong(key: String, fromActivity : Boolean = false): Long {
        var value = arguments?.getLong(key) ?: 0

        if(value == 0L && fromActivity){
            value =  activity?.intent?.getLongExtra(key,0)?: 0
        }
        return value
    }

    fun getArgumentDouble(key: String, fromActivity : Boolean = false): Double {
        var value =  arguments?.getDouble(key) ?: 0.0

        if(value == 0.0 && fromActivity){
            value =  activity?.intent?.getDoubleExtra(key,0.0)?: 0.0
        }
        return value
    }

    fun getArgumentInt(key: String, fromActivity : Boolean = false): Int {
        var value =  arguments?.getInt(key) ?: 0

        if(value == 0 && fromActivity){
            value =  activity?.intent?.getIntExtra(key,0)?: 0
        }
        return value
    }

    fun getArgumentBool(key: String, fromActivity : Boolean = false): Boolean {
        var value = arguments?.getBoolean(key) ?: false

        if(!value && fromActivity){
            value =  activity?.intent?.getBooleanExtra(key,false)?: false
        }
        return value
    }

    inline fun <reified T> getArgumentObject(key: String, fromActivity : Boolean = false): T? {
        val typeObject = arguments?.getString(key)
        var value = if (typeObject != null) Gson().fromJson(typeObject, T::class.java) else null

        if(value != null && fromActivity){
            val json = activity?.intent?.getStringExtra(key)
            value = if (json != null) Gson().fromJson(json, T::class.java) else null

        }
        return value
    }

    override fun onStart() {
        super.onStart()
        hideKeyboard()
    }

    override fun onStop() {
        super.onStop()
        hideKeyboard()
    }

}