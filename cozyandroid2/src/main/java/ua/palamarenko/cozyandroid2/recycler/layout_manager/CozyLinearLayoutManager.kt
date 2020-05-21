package ua.palamarenko.cozyandroid2.recycler.layout_manager

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler





class CozyLinearLayoutManager(context: Context?,orientation : Int,reverseLayout : Boolean) : LinearLayoutManager(context,orientation,reverseLayout) {

    var count : Int? = null

    var lastItemVisible : () -> Unit = {}




    override fun scrollVerticallyBy(dy: Int, recycler: Recycler?, state: RecyclerView.State?): Int {
        val result = super.scrollVerticallyBy(dy, recycler, state)
        if (findLastVisibleItemPosition() == itemCount - 1) {
            lastItemVisible.invoke()
        }
        return result
    }

    override fun onLayoutChildren(recycler: Recycler?, state: RecyclerView.State?) {
        super.onLayoutChildren(recycler, state)
        if (findLastVisibleItemPosition() == itemCount- 1) {
            lastItemVisible.invoke()
        }
    }


}