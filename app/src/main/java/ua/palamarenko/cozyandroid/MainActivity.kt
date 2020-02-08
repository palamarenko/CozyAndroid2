package ua.palamarenko.cozyandroid

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.cell_test.view.*
import kotlinx.android.synthetic.main.cell_test_sliding.view.*
import ua.palamarenko.cozyandroid2.CozyCell
import ua.palamarenko.cozyandroid2.CozyLibrary
import ua.palamarenko.cozyandroid2.SlidingCozyCell
import ua.palamarenko.cozyandroid2.base_fragment.nav_bar_activity.NavBarActivity
import ua.palamarenko.cozyandroid2.base_fragment.nav_bar_activity.NavigationActivityItem
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.CozyFragment
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.EmptyViewModel
import ua.palamarenko.cozyandroid2.cozy_view.TitleItem
import ua.palamarenko.cozyandroid2.tools.*


class MainActivity : NavBarActivity<EmptyViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CozyLibrary.init(this)


    }

    override val items =
            ArrayList<NavigationActivityItem>().apply {
                add(NavigationActivityItem(1,R.drawable.ic_important2,R.drawable.ic_important,
                    TitleItem("Срака","байрака"),FragmenA()
                ))
                add(NavigationActivityItem(2,R.drawable.ic_important2,R.drawable.ic_important,
                    TitleItem("Срака","байрака"),FragmenB()
                ))
            }

}


class FragmenA : CozyFragment<EmptyViewModel>(){

    override val layout = R.layout.activity_main

    override fun onStartScreen() {
        super.onStartScreen()
        photoView.load("https://i.stack.imgur.com/pio3A.jpg?s=328&g=1")

    }
}



class FragmenB : CozyFragment<EmptyViewModel>(){

    override val layout = R.layout.fragment_b

    override fun onStartScreen() {
        super.onStartScreen()


    }

}

class TestCell(override val data: String) : SlidingCozyCell(){
    override val slidingLayout = R.layout.cell_test_sliding

    override val layout = R.layout.cell_test

    override fun bind(view: View) {


        if(data == "1"){
            startSliding(view)
        }else{
            stopSliding(view)
        }



        view.text.text = data
        view.btTest.click {
            LOG_EVENT("HELLO","CLICK")
        }
    }
}


class TestCell2(override val data: String) : CozyCell(){

    override val layout = R.layout.cell_test2

    override fun bind(view: View) {
        view.text.text = data
//        view.btTest.click {
//        }
    }
}









