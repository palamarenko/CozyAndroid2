package ua.palamarenko.cozyandroid

import android.animation.Animator
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import androidx.core.view.ViewCompat
import kotlinx.android.synthetic.main.activity_main.*
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.CozyActivity
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.CozyFragment
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.EmptyViewModel
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.cell_test.view.*
import kotlinx.android.synthetic.main.cell_test_sliding.view.*
import ua.palamarenko.cozyandroid2.*
import ua.palamarenko.cozyandroid2.base_fragment.nav_bar_activity.NavBarActivity
import ua.palamarenko.cozyandroid2.base_fragment.nav_bar_activity.NavigationActivityItem
import ua.palamarenko.cozyandroid2.base_fragment.navigation.TRANSACTION_ANIMATION
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.NAVIGATE
import ua.palamarenko.cozyandroid2.cozy_view.TitleItem
import ua.palamarenko.cozyandroid2.image_picker.ImagePickPopupStrings
import ua.palamarenko.cozyandroid2.image_picker.ImagePicker
import ua.palamarenko.cozyandroid2.recycler.ButtonSwipeCallBack
import ua.palamarenko.cozyandroid2.tools.LOG_EVENT
import ua.palamarenko.cozyandroid2.tools.click
import ua.palamarenko.cozyandroid2.tools.makeCharSequence


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

        val list = ArrayList<CozyCell>().apply {
            add(TestCell2("sacasddscasd"))
            add(TestCell("1"))
            add(TestCell("1"))
            add(TestCell2("1"))
            add(TestCell("2"))
            add(TestCell("2"))
        }

        val list2 = ArrayList<CozyCell>().apply {
            add(TestCell2("1"))
            add(TestCell2("1"))
            add(TestCell("2"))
            add(TestCell("2"))
            add(TestCell("2"))
        }


        recycler.setCell(list)
        recycler.setRefreshing(true)
        recycler.refreshListener {
            recycler.setCell(list2)
        }


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









