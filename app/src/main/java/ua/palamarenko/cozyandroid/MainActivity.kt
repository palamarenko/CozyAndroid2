package ua.palamarenko.cozyandroid

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.cell_test.view.*
import kotlinx.android.synthetic.main.cell_test_sliding.view.*
import ua.palamarenko.cozyandroid2.CozyCell
import ua.palamarenko.cozyandroid2.CozyLibrary
import ua.palamarenko.cozyandroid2.SlidingCozyCell
import ua.palamarenko.cozyandroid2.base_activity.nav_bar_activity.BackClickStrategy
import ua.palamarenko.cozyandroid2.base_activity.nav_bar_activity.NavBarActivity
import ua.palamarenko.cozyandroid2.base_activity.nav_bar_activity.NavigationActivityItem
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.CozyFragment
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.EmptyViewModel
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.NAVIGATE
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.PICK_IMAGE
import ua.palamarenko.cozyandroid2.cozy_view.TitleItem
import ua.palamarenko.cozyandroid2.image_picker.ImagePickerRequest
import ua.palamarenko.cozyandroid2.tools.*


class MainActivity : NavBarActivity<EmptyViewModel>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBackClickStrategy(BackClickStrategy.GO_TO_FIRST)
        CozyLibrary.init(this)


    }

    override val items =
        ArrayList<NavigationActivityItem>().apply {
            add(
                NavigationActivityItem(
                    1, R.drawable.ic_important2, R.drawable.ic_important,
                    TitleItem("Срака", "байрака"), FragmenA(), defaultItem = true
                )
            )
            add(
                NavigationActivityItem(
                    2, R.drawable.ic_important2, R.drawable.ic_important,
                    TitleItem("Срака", "байрака"), FragmenB()
                )
            )
        }

}


class FragmenA : CozyFragment<EmptyViewModel>() {

    override val layout = R.layout.activity_main

    @SuppressLint("SetTextI18n")
    override fun onStartScreen() {
        super.onStartScreen()

        tvClick.text = "sacsdcsad"
            .makeCharSequence(SPAN.BOLD)
            .setClick(Color.RED) {
                LOG_EVENT("HELLO", "CLIcK")
            }

        tvClick.movementMethod = LinkMovementMethod.getInstance();
        tvClick.highlightColor = Color.TRANSPARENT;

        btButton.click {
            task(PICK_IMAGE, ImagePickerRequest() {
                ivTest.load(it)
            })
        }

    }
}

class FragmenC : CozyFragment<EmptyViewModel>() {

    override fun onBackPress(): Boolean {
        LOG_EVENT("HELLO", "BACK_PRESS")
        return false
    }

    override val layout = R.layout.holder_test

    override fun onStartScreen() {
        super.onStartScreen()

    }
}


class FragmenB : CozyFragment<EmptyViewModel>() {

    override val layout = R.layout.fragment_b

    override fun onStartScreen() {
        super.onStartScreen()


    }

}

class TestCell(override val data: String) : SlidingCozyCell() {
    override val slidingLayout = R.layout.cell_test_sliding

    override val layout = R.layout.cell_test

    override fun bind(view: View) {


        if (data == "1") {
            startSliding(view)
        } else {
            stopSliding(view)
        }



        view.text.text = data
        view.btTest.click {
            LOG_EVENT("HELLO", "CLICK")
        }
    }
}









