package ua.palamarenko.cozyandroid2.recycler

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ua.palamarenko.cozyandroid2.CompareItem
import ua.palamarenko.cozyandroid2.CozyCell
import ua.palamarenko.cozyandroid2.DefaultProgressCell
import ua.palamarenko.cozyandroid2.ViewBuilder
import java.util.*
import kotlin.collections.ArrayList

class CozyRecyclerAdapter() : RecyclerView.Adapter<CozyViewHolder<CozyCell>>(), DragAndDropCallbackListener.Listener {
    var list = ArrayList<CozyCell>()
    private var listViewBuilder = ArrayList<ViewBuilder>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CozyViewHolder<CozyCell> {
        return CozyViewHolder(
            listViewBuilder.find { it.viewType == viewType }!!.buildView(
                parent
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CozyViewHolder<CozyCell>, position: Int) {
        list[position].position = position
        list[position].bind(holder.itemView)
    }

    override fun getItemId(position: Int): Long {
        return list[position].data.hashCode().toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].getViewBuilder().viewType
    }


    fun updateList(data: List<CozyCell>, compareItem: CompareItem) {
        listViewBuilder.clear()
        data.forEach { cell ->
            if (listViewBuilder.find { it.viewType == cell.getViewBuilder().viewType } == null) {
                listViewBuilder.add(cell.getViewBuilder())
            }
        }

        val callBack = CozyDiffCallBack(list, data, compareItem)
        val diffResult = DiffUtil.calculateDiff(callBack)
        this.list.clear()
        this.list.addAll(data)
        diffResult.dispatchUpdatesTo(this)
    }


    fun addList(data: List<CozyCell>, compareItem: CompareItem) {
        listViewBuilder.clear()
        val newList = ArrayList<CozyCell>()
        newList.addAll(list)
        newList.addAll(data)
        newList.forEach { cell ->
            if (listViewBuilder.find { it.viewType == cell.getViewBuilder().viewType } == null) {
                listViewBuilder.add(cell.getViewBuilder())
            }
        }

        val callBack = CozyDiffCallBack(list, newList, compareItem)
        val diffResult = DiffUtil.calculateDiff(callBack)
        this.list.clear()
        this.list.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    fun addProgressCell(cell: CozyCell, compareItem: CompareItem) {
        listViewBuilder.add(cell.getViewBuilder())
        val newList = ArrayList<CozyCell>()
        newList.addAll(list)
        newList.add(cell)
        val callBack = CozyDiffCallBack(list, newList, compareItem)
        val diffResult = DiffUtil.calculateDiff(callBack)
        this.list.clear()
        this.list.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    fun removeProgressCell(compareItem: CompareItem) {
        if (list.isEmpty()) {
            return
        }


        if(list[list.lastIndex] is DefaultProgressCell) {
            val newList = ArrayList<CozyCell>()
            newList.addAll(list)
            newList.removeAt(newList.size - 1)
            val callBack = CozyDiffCallBack(list, newList, compareItem)
            val diffResult = DiffUtil.calculateDiff(callBack)
            this.list.clear()
            this.list.addAll(newList)
            diffResult.dispatchUpdatesTo(this)
        }
    }

    override fun onRowMoved(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(list, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(list, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }



}
