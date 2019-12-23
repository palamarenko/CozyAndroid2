package ua.palamarenko.cozyandroid

import android.graphics.Canvas
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
import ua.palamarenko.cozyandroid2.recycler.ButtonSwipeCallBack
import ua.palamarenko.cozyandroid2.tools.LOG_EVENT


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

        recycler.setCell(arrayListOf(TestCell("asdcsad"),TestCell("231e")),LinearLayoutManager(context))


    }
}

class TestCell(override val data : String) : CozyCell(){
    override val layout = R.layout.item_test

    override fun bind(view: View) {}


}






