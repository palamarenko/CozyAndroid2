package ua.palamarenko.cozyandroid2.base_fragment.nav_bar_activity

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.activity_nav_bar.*
import ua.palamarenko.cozyandroid2.R
import ua.palamarenko.cozyandroid2.base_fragment.navigation.Navigator
import ua.palamarenko.cozyandroid2.base_fragment.navigation.TRANSACTION_ANIMATION
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.CozyActivity
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.CozyFragment
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.CozyViewModel
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.NAVIGATE
import ua.palamarenko.cozyandroid2.cozy_view.CozyNavigateView
import ua.palamarenko.cozyandroid2.cozy_view.NavigationItem
import ua.palamarenko.cozyandroid2.cozy_view.TitleItem

abstract class NavBarActivity<T : CozyViewModel> : CozyActivity<T>() {


    abstract val items: List<NavigationActivityItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_bar)
        this.frameLayout = flFragments
        this.navigator = NavBarNavigator(R.id.flFragments, supportFragmentManager, vNavigation,items)
        vNavigation.initView(items.map { NavigationItem(it.id,it.iconSelect,it.iconUnSelect,it.title) })


        vNavigation.listnener = {item ->
            task(NAVIGATE,items.find { it.id ==  item.id}!!.choiceFragment)
        }
    }


    override fun handleOnBackPressEvent() {
        super.handleOnBackPressEvent()
        Handler().postDelayed({
            items.forEach {
                if(navigator.getCurrentFragment()!=null && navigator.getCurrentFragment()!!::class.java.simpleName == it.choiceFragment::class.java.simpleName){
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
    vararg val grooupFragmens: Class<CozyFragment<*>>
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