package ua.palamarenko.cozyandroid2.recycler

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingData
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ua.palamarenko.cozyandroid2.CozyCell
import ua.palamarenko.cozyandroid2.DefaultProgressCell
import ua.palamarenko.cozyandroid2.ViewBuilder
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet


class CozyRecyclerAdapter() : RecyclerView.Adapter<CozyViewHolder<CozyCell>>(),
    DragAndDropCallbackListener.Listener {

    var list = ArrayList<CozyCell>()


    private var listViewBuilder = HashSet<ViewBuilder>()

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

    override fun onBindViewHolder(
        holder: CozyViewHolder<CozyCell>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        list[position].position = position
        list[position].bind(holder.itemView)
    }


    override fun getItemId(position: Int): Long {
        return list[position].data.hashCode().toLong()

    }

    override fun getItemViewType(position: Int): Int {
        listViewBuilder.add(list[position].getViewBuilder())
        return list[position].getViewBuilder().viewType

    }


    fun updateList(data: List<CozyCell>) {
        val callBack = CozyDiffCallBack(list, data)
        val diffResult = DiffUtil.calculateDiff(callBack)
        this.list.clear()
        this.list.addAll(data)
        diffResult.dispatchUpdatesTo(this)
    }


    fun addList(data: List<CozyCell>) {
        val newList = ArrayList<CozyCell>()
        newList.addAll(list)
        newList.addAll(data)
        val callBack = CozyDiffCallBack(list, newList)
        val diffResult = DiffUtil.calculateDiff(callBack)
        this.list.clear()
        this.list.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
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
