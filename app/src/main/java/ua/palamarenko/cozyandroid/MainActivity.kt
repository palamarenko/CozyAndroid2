package ua.palamarenko.cozyandroid

import android.graphics.Color
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_b.*
import ua.palamarenko.cozyandroid2.base_fragment.navigation.BackPress
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.CozyActivity
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.CozyFragment
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.EmptyViewModel
import ua.palamarenko.cozyandroid2.tools.initSimple
import java.util.*


class MainActivity : CozyActivity<EmptyViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        simpleInit(FragmenA())


    }
}


class FragmenA : CozyFragment<EmptyViewModel>(), BackPress {
    override fun onBackPress(): Boolean {
        return true
    }

    override val layout = R.layout.activity_main
    

    override fun onViewCreated() {
        super.onViewCreated()

        viewPager.initSimple(fragmentManager,3) {
            return@initSimple FragmentB()
        }

    }


}

class FragmentB : CozyFragment<EmptyViewModel>() {
    override val layout = R.layout.fragment_b


    override fun onViewCreated() {
        super.onViewCreated()
        val rnd = Random()
        val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        ivImage.setBackgroundColor(color)
    }


}






