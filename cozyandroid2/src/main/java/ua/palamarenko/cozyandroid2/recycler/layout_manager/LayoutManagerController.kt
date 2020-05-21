package ua.palamarenko.cozyandroid2.recycler.layout_manager

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.view_recycler.view.*

class LayoutManagerController(val layoutManager: RecyclerView.LayoutManager) {

    var size: Int? = null


    fun setCellCount(size: Int) {
        this.size = size


        if (layoutManager is CozyLinearLayoutManager) {
            layoutManager.count = size
        }
    }


    fun listenLastItem(listener: () -> Unit) {
        if (layoutManager is CozyLinearLayoutManager) {
            layoutManager.lastItemVisible = listener
        }
    }

    fun moveToPosition(position: Int) {

        if (layoutManager is LinearLayoutManager) {
            layoutManager.scrollToPosition(position)
        }

        if (layoutManager is GridLayoutManager) {
            layoutManager.scrollToPosition(position)
        }

        if (layoutManager is StaggeredGridLayoutManager) {
            layoutManager.scrollToPosition(position)
        }

    }





}