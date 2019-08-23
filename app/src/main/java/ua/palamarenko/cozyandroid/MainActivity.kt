package ua.palamarenko.cozyandroid

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_test.view.*
import ua.palamarenko.cozyandroid2.CozyCell
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.*
import ua.palamarenko.cozyandroid2.tools.CozyTimer
import ua.palamarenko.cozyandroid2.tools.LOG_EVENT

class MainActivity : CozyActivity<EmptyViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        simpleInit(FragmenA())

    }
}


class FragmenA : CozyFragment<EmptyViewModel>() {
    override val layout = R.layout.activity_main

    var b = true

    override fun onViewCreated() {
        super.onViewCreated()

        task(SHOW_POPUP,HelloPopup())


        btButton.setOnClickListener {
            if (b) {
                recycler.setCell(make2())
            } else {
                recycler.setCell(make())
            }
            b = !b
        }


        recycler.setLayoutManager(StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL))
        recycler.setCell(make())

    }

    fun make(): List<TestCell> {
        return ArrayList<TestCell>().apply {
            add(TestCell(Model("hello0")))
            add(TestCell(Model("hello1")))
            add(TestCell(Model("hello2")))
            add(TestCell(Model("hello3")))
            add(TestCell(Model("hello4")))
            add(TestCell(Model("hello5")))
        }
    }

    fun make2(): List<TestCell> {
        return ArrayList<TestCell>().apply {
            add(TestCell(Model("hello1")))
            add(TestCell(Model("hello2")))
            add(TestCell(Model("hello3")))
            add(TestCell(Model("hello4")))
            add(TestCell(Model("hello5")))
        }
    }

}
data class Model (val title : String)


class TestCell(val text: Model) : CozyCell(text) {
    override val layout: Int = R.layout.item_test

    override fun bind(view: View) {
        view.tvText.text = text.title
    }

}


