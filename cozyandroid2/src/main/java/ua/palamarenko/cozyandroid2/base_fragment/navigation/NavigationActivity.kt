package ua.palamarenko.cozyandroid2.base_fragment.navigation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.tbruyelle.rxpermissions2.RxPermissions
import ua.palamarenko.cozyandroid2.R
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.CozyFragment
import ua.palamarenko.cozyandroid2.tools.LOG_EVENT


const val FRAGMENT_TAG = "FRAGMENT_TAG"


open class NavigateActivity : AppCompatActivity() {


    lateinit var navigator: Navigator
    lateinit var frameLayout: FrameLayout

    var ignoreEnterBackground: Boolean = false


    fun simpleInit(
        fragment: Fragment? = null,
        animation: TRANSACTION_ANIMATION = TRANSACTION_ANIMATION.DEFAULT_ANIMATION,
        customAnimation: ((ft: FragmentTransaction) -> Unit)? = null
    ) {

        val fm: FragmentManager = supportFragmentManager
        for (i in 0 until fm.backStackEntryCount) {
            fm.popBackStack()
        }


        frameLayout = FrameLayout(this)
        frameLayout.id = View.generateViewId()
        this.setContentView(frameLayout)
        this.navigator =
            Navigator(frameLayout.id, supportFragmentManager, animation, customAnimation)

        if (fragment != null) {
            setFragment(fragment)
        }


    }


    fun setFragment(fragment: Fragment) {
        navigator.replaceFragment(fragment, Bundle())
    }

    override fun onBackPressed() {
        onBackPressed(null)
    }

    fun findCurrentFragment(): Fragment? {
        return navigator.fragmentManager.fragments.findLast { it.tag == FRAGMENT_TAG || it.tag == it::class.java.simpleName }
    }

    open fun onBackPressed(fragment: Class<*>?) {
        if (navigator.fragmentManager.fragments.isNotEmpty() && findCurrentFragment() is BackPress) {
            val back = (findCurrentFragment() as BackPress).onBackPress()
            if (back) return
        }


        if (fragment != null) {
            navigator.fragmentManager.popBackStack(fragment.simpleName, 0)
        } else {
            val manager = navigator.fragmentManager
            val count = manager.backStackEntryCount

            LOG_EVENT("HELLO",count)
            if (count <= 1) {
                finish()
            } else {
                navigator.fragmentManager.popBackStack()
            }
        }
        handleOnBackPressEvent()
    }

    open fun handleOnBackPressEvent() {

    }

    fun request(): RxPermissions {
        return RxPermissions(this)
    }

}

enum class TRANSACTION_ANIMATION { DEFAULT_ANIMATION, SLIDE_ANIMATION, NONE }

open class Navigator(
    val contId: Int,
    val fragmentManager: FragmentManager,
    val animation: TRANSACTION_ANIMATION = TRANSACTION_ANIMATION.DEFAULT_ANIMATION,
    val customAnimation: ((ft: FragmentTransaction) -> Unit)? = null
) {


    fun getCurrentFragment(): CozyFragment<*>? {
        return fragmentManager.findFragmentById(contId) as? CozyFragment<*>
    }


    open fun replaceFragment(fragment: Fragment, bundle: Bundle) {

        try {
            val ft = this.fragmentManager.beginTransaction()

            if (customAnimation != null) {
                ft.replace(this.contId, fragment, FRAGMENT_TAG)
                customAnimation.invoke(ft)
            } else {
                if (getCurrentFragment() == null) {
                    ft.replace(this.contId, fragment, FRAGMENT_TAG)
                } else {
                    when (animation) {
                        TRANSACTION_ANIMATION.DEFAULT_ANIMATION -> {
                            ft.replace(this.contId, fragment, FRAGMENT_TAG)
                            defaultAnimation(ft)
                        }
                        TRANSACTION_ANIMATION.SLIDE_ANIMATION -> {
                            addSlideAnimation(ft)
                            ft.replace(this.contId, fragment, FRAGMENT_TAG)
                        }
                        TRANSACTION_ANIMATION.NONE -> {
                            ft.replace(this.contId, fragment, FRAGMENT_TAG)
                        }
                    }
                }

            }

            ft.addToBackStack(fragment.javaClass.simpleName)

            ft.commitAllowingStateLoss()

        } catch (e: Exception) {
        }


    }

    private fun addSlideAnimation(ft: FragmentTransaction) {
        ft.setCustomAnimations(
            R.anim.enter_from_right,
            R.anim.exit_to_left,
            R.anim.enter_from_left,
            R.anim.exit_to_right
        )

        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
    }

    @SuppressLint("ResourceType")
    private fun defaultAnimation(
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