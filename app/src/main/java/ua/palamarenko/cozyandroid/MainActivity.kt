package ua.palamarenko.cozyandroid

import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.CozyActivity
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.CozyFragment
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.EmptyViewModel
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ua.palamarenko.cozyandroid2.*
import ua.palamarenko.cozyandroid2.image_picker.ImagePickPopupStrings
import ua.palamarenko.cozyandroid2.image_picker.ImagePicker
import ua.palamarenko.cozyandroid2.recycler.ButtonSwipeCallBack
import ua.palamarenko.cozyandroid2.tools.LOG_EVENT
import ua.palamarenko.cozyandroid2.tools.click
import ua.palamarenko.cozyandroid2.tools.makeCharSequence


class MainActivity : CozyActivity<EmptyViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CozyLibrary.init(this)
        simpleInit(FragmenA())


    }
}


class FragmenA : CozyFragment<EmptyViewModel>(){

    override val layout = R.layout.activity_main

    override fun onStartScreen() {
        super.onStartScreen()
        bt.click {
            ImagePicker.pickImage(this, ImagePickPopupStrings(title = "Жопа".makeCharSequence(color = Color.MAGENTA),cancel = R.string.appbar_scrolling_view_behavior,cameraTitle = "Камера")) {


            }
        }


    }
}

class TestCell(override val data : String) : CozyCell(){
    override val layout = R.layout.item_test

    override fun bind(view: View) {}


}






