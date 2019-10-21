package ua.palamarenko.cozyandroid2.base_fragment.navigation

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
import androidx.fragment.app.FragmentTransaction
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.CozyFragment
import android.R



const val FRAGMENT_TAG = "FRAGMENT_TAG"


open class NavigateActivity : AppCompatActivity() {


    lateinit var navigator: Navigator
    lateinit var frameLayout: FrameLayout

    var ignoreEnterBackground: Boolean = false
    var animation = TRANSACTION_ANIMATION.DEFAULT_ANIMATION


    fun simpleInit(
        fragment: Fragment? = null,
        animation: TRANSACTION_ANIMATION = TRANSACTION_ANIMATION.DEFAULT_ANIMATION
    ) {
        this.animation = animation
        frameLayout = FrameLayout(this)
        frameLayout.id = View.generateViewId()
        this.setContentView(frameLayout)
        this.navigator = Navigator(frameLayout.id, supportFragmentManager, this.animation)

        if (fragment != null) {
            setFragment(fragment)
        }


    }


    fun setFragment(fragment: Fragment) {
        navigator.replaceFragment(fragment, Bundle())
    }


    override fun onBackPressed() {
        if (navigator.fragmentManager.fragments.isNotEmpty() && findCurrentFragment() is BackPress) {
            val back =
                (findCurrentFragment() as BackPress).onBackPress()
            if (back) return
        }
        onBackPressed(null)
    }


    private fun findCurrentFragment() : Fragment? {
      return  navigator.fragmentManager.fragments.findLast { it.tag == FRAGMENT_TAG }
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
        handleOnBackPressEvent()
    }

    open fun handleOnBackPressEvent(){

    }


    fun request(): RxPermissions {
        return RxPermissions(this)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }
}

enum class TRANSACTION_ANIMATION { DEFAULT_ANIMATION, CUSTON, NONE }

open class Navigator(
    val contId: Int,
    val fragmentManager: FragmentManager,
    val animation: TRANSACTION_ANIMATION = TRANSACTION_ANIMATION.DEFAULT_ANIMATION
) {


    fun showDialog(dialog: androidx.fragment.app.DialogFragment) {
        dialog.show(this.fragmentManager, "")
    }


    fun getCurrentFragment() : CozyFragment<*>?{
       return fragmentManager.findFragmentById(contId) as? CozyFragment<*>
    }




    open fun replaceFragment(fragment: Fragment, bundle: Bundle) {

        try {
            val ft = this.fragmentManager.beginTransaction()
            ft.replace(this.contId, fragment, FRAGMENT_TAG)
            ft.addToBackStack(fragment.javaClass.simpleName)

            when (animation) {
                TRANSACTION_ANIMATION.DEFAULT_ANIMATION -> addCustomAnimation(ft)
                TRANSACTION_ANIMATION.NONE -> {
                }
                TRANSACTION_ANIMATION.CUSTON -> setCustomAnimation(ft)
            }

            ft.commitAllowingStateLoss()

        } catch (e: Exception) {
        }


    }

    open fun setCustomAnimation(ft: FragmentTransaction) {}


    @SuppressLint("ResourceType")
    fun addCustomAnimation(
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