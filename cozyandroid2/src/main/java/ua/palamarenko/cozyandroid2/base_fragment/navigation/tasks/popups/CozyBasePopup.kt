package ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.popups

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import ua.palamarenko.cozyandroid2.base_fragment.navigation.CozyBaseFragment
import ua.palamarenko.cozyandroid2.base_fragment.navigation.CozyBaseViewModel
import ua.palamarenko.cozyandroid2.base_fragment.navigation.ReflectionUtils
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.CozyViewModel

abstract class CozyBasePopup<T : CozyViewModel> : AppCompatDialogFragment() {

    lateinit var v : View
    abstract val layout : Int
    private var clearFocusView: EditText? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        v = inflater.inflate(layout, container, false)
        return v
    }


    fun vm(): T {
        val cl : Class<T>  = ReflectionUtils.getGenericParameterClass(this::class.java,0) as Class<T>
        return ViewModelProviders.of(this).get(cl)
    }




    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    fun show(manager: androidx.fragment.app.FragmentManager?) {
        if(manager!=null) show(manager, "")
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


    fun setArgumentCharSequence(key: String, value: CharSequence): CozyBasePopup<T> {
        if (arguments == null) {
            arguments = Bundle()
        }
        arguments?.putCharSequence(key, value)
        return this
    }

    fun getArgumentString(key: String): String {
        return arguments?.getString(key) ?: ""
    }

    fun getArgumentCharSequence(key: String): CharSequence {
        return arguments?.getCharSequence(key) ?: ""
    }

    fun getArgumentInt(key: String): Int {
        return arguments?.getInt(key) ?: 0
    }

    fun getArgumentBool(key: String): Boolean {
        return arguments?.getBoolean(key) ?: false
    }

    fun getArgumentDouble(key: String): Double {
        return arguments?.getDouble(key) ?: 0.0
    }

    fun <B : CozyBaseViewModel, T : CozyBasePopup<B>> T.putString(key: String, value: String): T {
        if (arguments == null) {
            arguments = Bundle()
        }

        arguments?.putString(key, value)
        return this
    }

    fun <B : CozyBaseViewModel, T : CozyBasePopup<B>> T.putDouble(key: String, value: Double): T {
        if (arguments == null) {
            arguments = Bundle()
        }

        arguments?.putDouble(key, value)
        return this
    }

    fun <B : CozyBaseViewModel, T : CozyBasePopup<B>> T.putInt(key: String, value: Int): T {
        if (arguments == null) {
            arguments = Bundle()
        }

        arguments?.putInt(key, value)
        return this
    }

    fun <B : CozyBaseViewModel, T : CozyBasePopup<B>> T.putLong(key: String, value: Long): T {
        if (arguments == null) {
            arguments = Bundle()
        }

        arguments?.putLong(key, value)
        return this
    }

    fun <B : CozyBaseViewModel, T : CozyBasePopup<B>> T.putBool(key: String, value: Boolean): T {
        if (arguments == null) {
            arguments = Bundle()
        }

        arguments?.putBoolean(key, value)
        return this
    }

    fun <B : CozyBaseViewModel, T : CozyBasePopup<B>> T.putObject(key: String, value: Any?): T {
        if (arguments == null) {
            arguments = Bundle()
        }
        if (value != null) arguments?.putString(key, Gson().toJson(value))
        return this
    }





    inline fun <reified T> getArgumentObject(key: String): T?{
        val typeObject = arguments?.getString(key)

        return if(typeObject!= null) Gson().fromJson(typeObject,T::class.java) else null
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

}