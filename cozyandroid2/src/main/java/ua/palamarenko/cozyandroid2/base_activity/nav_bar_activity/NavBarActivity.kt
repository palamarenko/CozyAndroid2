package ua.palamarenko.cozyandroid2.base_activity.nav_bar_activity

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.activity_nav_bar.*
import ua.palamarenko.cozyandroid2.R
import ua.palamarenko.cozyandroid2.base_fragment.navigation.BackPress
import ua.palamarenko.cozyandroid2.base_fragment.navigation.FRAGMENT_TAG
import ua.palamarenko.cozyandroid2.base_fragment.navigation.Navigator
import ua.palamarenko.cozyandroid2.base_fragment.navigation.TRANSACTION_ANIMATION
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.CozyActivity
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.CozyFragment
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.CozyViewModel
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.NAVIGATE
import ua.palamarenko.cozyandroid2.cozy_view.CozyNavigateView
import ua.palamarenko.cozyandroid2.cozy_view.NavigationItem
import ua.palamarenko.cozyandroid2.cozy_view.TitleItem


enum class BackClickStrategy { DEFAULT, GO_TO_FIRST }

abstract class NavBarActivity<T : CozyViewModel> : CozyActivity<T>() {

    private var backClickStrategy = BackClickStrategy.DEFAULT
    abstract val items: List<NavigationActivityItem>


    fun setBackClickStrategy(strategy : BackClickStrategy){
        backClickStrategy = strategy
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_bar)
        this.frameLayout = flFragments
        this.navigator =
            NavBarNavigator(R.id.flFragments, supportFragmentManager, vNavigation, items)
        vNavigation.initView(items.map {
            NavigationItem(
                it.id,
                it.iconSelect,
                it.iconUnSelect,
                it.title
            )
        })


        vNavigation.listnener = { item ->
            task(NAVIGATE, items.find { it.id == item.id }!!.choiceFragment)
        }
        vNavigation.setItem(true, items.first().id)
    }

    fun getNavigationView(): CozyNavigateView {
        return vNavigation
    }

    override fun onBackPressed(fragment: Class<*>?) {
        when (backClickStrategy) {
            BackClickStrategy.DEFAULT -> {
                super.onBackPressed(fragment)
            }
            BackClickStrategy.GO_TO_FIRST -> {
                onBackPressedGoToFirst()
            }
        }
    }

    private fun onBackPressedGoToFirst() {
        if (navigator.fragmentManager.fragments.isNotEmpty() && findCurrentFragment() is BackPress) {
            val back = (findCurrentFragment() as BackPress).onBackPress()
            if (back) return
        }

        val currentFragment = navigator.fragmentManager.fragments.lastOrNull()
        val name = if(currentFragment!=null)currentFragment::class.java.simpleName else ""


       val choiceItem =  items.find {it.choiceFragment::class.java.simpleName == name }


        when {
            choiceItem == null -> {
                val manager = navigator.fragmentManager
                val count = manager.backStackEntryCount
                if (count <= 1) {
                    finish()
                } else {
                    navigator.fragmentManager.popBackStack()
                }

            }
            choiceItem.defaultItem -> {
                finish()
            }
            !choiceItem.defaultItem ->{
                for (i in 0 until navigator.fragmentManager.backStackEntryCount - 1) {
                    navigator.fragmentManager.popBackStack()
                }
            }

        }


        handleOnBackPressEvent()
    }

    override fun handleOnBackPressEvent() {
        super.handleOnBackPressEvent()
        Handler().postDelayed({
            items.forEach {
                if (navigator.getCurrentFragment() != null &&
                    navigator.getCurrentFragment()!!::class.java.simpleName == it.choiceFragment::class.java.simpleName
                ) {
                    vNavigation.setItem(false, it.id)
                }
            }
        }, 100)
    }
}

class NavigationActivityItem(
    val id: Int,
    val iconSelect: Int,
    val iconUnSelect: Int,
    val title: TitleItem? = null,
    val choiceFragment: CozyFragment<*>,
    vararg val grooupFragmens: Class<CozyFragment<*>>,
    val defaultItem : Boolean = false
)


class NavBarNavigator(
    contId: Int,
    fragmentManager: FragmentManager,
    val navigateView: CozyNavigateView,
    val items: List<NavigationActivityItem>
) : Navigator(contId, fragmentManager, TRANSACTION_ANIMATION.NONE) {

    override fun replaceFragment(fragment: Fragment, bundle: Bundle) {


        items.forEach {
            if (fragment.javaClass.simpleName == it.choiceFragment::class.java.simpleName) {
                navigateView.setItem(false, it.id)
            }
        }


        val ft = this.fragmentManager.beginTransaction()


        try {
            if (fragmentManager.findFragmentByTag(fragment.javaClass.simpleName) != null && (items.find { it.choiceFragment::class.java.simpleName == fragment.javaClass.simpleName } != null)
            ) {
                ft.replace(
                    this.contId,
                    fragmentManager.findFragmentByTag(fragment.javaClass.simpleName)!!,
                    fragment.javaClass.simpleName
                )
            } else {
                ft.replace(this.contId, fragment, fragment.javaClass.simpleName)
            }

            ft.addToBackStack(fragment.javaClass.simpleName)

            ft.commitAllowingStateLoss()
        } catch (e: Exception) {
        }


    }
}