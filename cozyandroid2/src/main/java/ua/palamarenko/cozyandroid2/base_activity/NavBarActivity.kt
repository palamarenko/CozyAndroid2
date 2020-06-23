package ua.palamarenko.cozyandroid2.base_activity

import android.os.Bundle
import android.os.Handler
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.activity_nav_bar.*
import ua.palamarenko.cozyandroid2.R
import ua.palamarenko.cozyandroid2.base_fragment.navigation.BackPress
import ua.palamarenko.cozyandroid2.base_fragment.navigation.Navigator
import ua.palamarenko.cozyandroid2.base_fragment.navigation.TRANSACTION_ANIMATION
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.CozyActivity
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.CozyFragment
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.CozyViewModel
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.NAVIGATE
import ua.palamarenko.cozyandroid2.cozy_view.BaseNavigationItem
import ua.palamarenko.cozyandroid2.cozy_view.CozyNavigateView
import ua.palamarenko.cozyandroid2.cozy_view.NavigationItem
import ua.palamarenko.cozyandroid2.cozy_view.TitleItem


enum class BackClickStrategy { DEFAULT, GO_TO_FIRST }

abstract class NavBarActivity<T : CozyViewModel> : CozyActivity<T>() {

    private var backClickStrategy =
        BackClickStrategy.DEFAULT
    abstract val items: List<NavigationActivityItem>


    fun setBackClickStrategy(strategy: BackClickStrategy) {
        backClickStrategy = strategy
    }


    open fun getMainFrame(): FrameLayout {
        return flFragments
    }

    open fun getNavigationView(): CozyNavigateView {
        return vNavigation
    }

    open fun getActivityView(): Int {
        return R.layout.activity_nav_bar
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getActivityView())
        init()

    }

    fun init() {
        this.frameLayout = getMainFrame()
        this.navigator =
            NavBarNavigator(
                getMainFrame().id,
                supportFragmentManager,
                getNavigationView(),
                items
            )
        getNavigationView().initView(items.map {it.item})


        getNavigationView().listnener = { item ->
            task(NAVIGATE, items.find { it.item.id == item.id }!!.choiceFragment)
        }
        getNavigationView().setItem(true, items.first().item.id)
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

        val list = navigator.fragmentManager.fragments.filter {!it.tag.isNullOrBlank() }
        val currentFragment = list.lastOrNull()
        val name = if (currentFragment != null) currentFragment::class.java.canonicalName else ""


        val choiceItem = items.find { it.choiceFragment::class.java.canonicalName == name }


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
            !choiceItem.defaultItem -> {
               task(NAVIGATE,items.find { it.defaultItem }!!.choiceFragment)
            }

        }


        handleOnBackPressEvent()
    }

    override fun handleOnBackPressEvent() {
        super.handleOnBackPressEvent()
        Handler().postDelayed({
            items.forEach {
                if (navigator.getCurrentFragment() != null &&
                    navigator.getCurrentFragment()!!::class.java.canonicalName == it.choiceFragment::class.java.canonicalName
                ) {
                    getNavigationView().setItem(false, it.item.id)
                }
            }
        }, 100)
    }
}

class NavigationActivityItem(
    val item : BaseNavigationItem,
    val choiceFragment: CozyFragment<*>,
    vararg val grooupFragmens: Class<CozyFragment<*>>,
    val defaultItem: Boolean = false
)








class NavBarNavigator(
    contId: Int,
    fragmentManager: FragmentManager,
    val navigateView: CozyNavigateView,
    val items: List<NavigationActivityItem>
) : Navigator(contId, fragmentManager, TRANSACTION_ANIMATION.NONE) {

    override fun replaceFragment(fragment: Fragment, bundle: Bundle) {


        items.forEach {
            if (fragment.javaClass.canonicalName == it.choiceFragment::class.java.canonicalName) {
                navigateView.setItem(false, it.item.id)
            }
        }


        val ft = this.fragmentManager.beginTransaction()


        try {
            if (fragmentManager.findFragmentByTag(fragment.javaClass.canonicalName) != null && (items.find { it.choiceFragment::class.java.canonicalName == fragment.javaClass.canonicalName } != null)
            ) {
                ft.replace(
                    this.contId,
                    fragmentManager.findFragmentByTag(fragment.javaClass.canonicalName)!!,
                    fragment.javaClass.canonicalName
                )
            } else {
                ft.replace(this.contId, fragment, fragment.javaClass.canonicalName)
            }

            ft.addToBackStack(fragment.javaClass.canonicalName)

            ft.commitAllowingStateLoss()
        } catch (e: Exception) {
        }


    }
}