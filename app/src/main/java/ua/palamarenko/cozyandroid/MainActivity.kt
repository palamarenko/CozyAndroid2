package ua.palamarenko.cozyandroid

import android.os.Bundle
import android.os.Handler
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.CozyActivity
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.CozyFragment
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.EmptyViewModel
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.NAVIGATE

class MainActivity : CozyActivity<EmptyViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        simpleInit(FragmenA())
        setContentView(R.layout.activity_main)

    }
}


class FragmenA : CozyFragment<EmptyViewModel>() {
    override val layout = R.layout.fragment_a

    override fun onViewCreated() {
        super.onViewCreated()
        Handler().postDelayed({
            task(NAVIGATE,FragmenB())
        },500)
    }
}


class FragmenB : CozyFragment<EmptyViewModel>() {
    override val layout = R.layout.fragment_b
}
