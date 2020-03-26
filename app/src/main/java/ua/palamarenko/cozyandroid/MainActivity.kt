package ua.palamarenko.cozyandroid

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import ua.palamarenko.cozyandroid2.CozyLibrary
import ua.palamarenko.cozyandroid2.base_activity.nav_bar_activity.BackClickStrategy
import ua.palamarenko.cozyandroid2.base_activity.nav_bar_activity.NavBarActivity
import ua.palamarenko.cozyandroid2.base_activity.nav_bar_activity.NavigationActivityItem
import ua.palamarenko.cozyandroid2.base_fragment.navigation.ReflectionUtils
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.ActivityResultResponse
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.CozyFragment
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.EmptyViewModel
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.PICK_IMAGE
import ua.palamarenko.cozyandroid2.cozy_view.TitleItem
import ua.palamarenko.cozyandroid2.image_picker.ImagePickerRequest
import ua.palamarenko.cozyandroid2.tools.*
import java.lang.reflect.Type


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
                    TitleItem("Срака", "байрака"), FragmenC()
                )
            )
        }

}


class FragmenA : CozyFragment<EmptyViewModel>() {

    override val layout = R.layout.activity_main

    @SuppressLint("SetTextI18n")
    override fun onStartScreen() {
        super.onStartScreen()

        tvClick.movementMethod = LinkMovementMethod.getInstance();
        tvClick.highlightColor = Color.TRANSPARENT;

        btButton.click {
            task(PICK_IMAGE, ImagePickerRequest() {
                ivTest.load(it)
            })
        }

    }
}


class TestData(val data : String)

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












