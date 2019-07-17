package ua.palamarenko.cozyandroid2.BaseFragment.navigation

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tbruyelle.rxpermissions2.RxPermissions
import java.lang.Exception
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction


open class NavigateActivity : AppCompatActivity() {

    lateinit var navigator: Navigator
    lateinit var frameLayout: FrameLayout

    var ignoreEnterBackground: Boolean = false

    fun simpleInit(fragment: Fragment? = null) {
        frameLayout = FrameLayout(this)
        frameLayout.id = View.generateViewId()
        this.setContentView(frameLayout)
        this.navigator = Navigator(frameLayout.id, supportFragmentManager)

        if (fragment != null) {
            setFragment(fragment)
        }


    }


    fun setFragment(fragment: Fragment) {
        navigator.replaceFragment(fragment, Bundle())
    }


    override fun onBackPressed() {
        if (navigator.fragmentManager.fragments.isNotEmpty() && navigator.fragmentManager.fragments[navigator.fragmentManager.fragments.size - 1] is BackPress) {
            val back =
                (navigator.fragmentManager.fragments[navigator.fragmentManager.fragments.size - 1] as BackPress).onBackPress()
            if (back) return
        }
        onBackPressed(null)
    }


    open fun onBackPressed(fragment: Class<*>?) {
        if (fragment != null) {
            navigator.fragmentManager.popBackStack(fragment.simpleName, 0)
        } else {
            val manager = navigator.fragmentManager
            val count = manager.backStackEntryCount
            if (count <= 1) {
                finish()
            } else {
                navigator.fragmentManager.popBackStack()
            }
        }
    }

    fun request(): RxPermissions {
        return RxPermissions(this)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }
}

open class Navigator(val contId: Int, val fragmentManager: FragmentManager) {

    fun showDialog(dialog: androidx.fragment.app.DialogFragment) {
        dialog.show(this.fragmentManager, "")
    }


    open fun replaceFragment(fragment: Fragment, bundle: Bundle) {

        try {
            val ft = this.fragmentManager.beginTransaction()
            ft.replace(this.contId, fragment)
            ft.addToBackStack(fragment.javaClass.simpleName)
            addCustomAnimation(ft)
            ft.commitAllowingStateLoss()

        } catch (e: Exception) {
        }


    }


    @SuppressLint("ResourceType")
    open fun addCustomAnimation(
        ft: FragmentTransaction
    ) {
        ft.setCustomAnimations(
            android.R.attr.activityOpenEnterAnimation,
            android.R.attr.activityOpenExitAnimation,
            android.R.attr.activityCloseEnterAnimation,
            android.R.attr.activityCloseExitAnimation
        )
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
    }

}