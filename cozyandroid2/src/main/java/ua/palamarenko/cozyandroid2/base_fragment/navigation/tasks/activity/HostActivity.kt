package ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.activity

import android.os.Bundle
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.CozyActivity
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.CozyFragment
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.EmptyViewModel
import java.lang.reflect.Constructor


class HostActivity : CozyActivity<EmptyViewModel>(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val c = Class.forName(intent.getStringExtra("HostActivityFragment")!!)
        val cons: Constructor<*> = c.getConstructor()
        val fragment: Any = cons.newInstance()
        simpleInit(fragment as CozyFragment<*>)


    }
}