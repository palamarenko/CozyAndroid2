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

    companion object {
        val CozyDiffCallback = object : DiffUtil.ItemCallback<CozyCell>() {
            override fun areItemsTheSame(oldItem: CozyCell, newItem: CozyCell): Boolean {
                return oldItem.getIdentifier() == newItem.getIdentifier()
            }

            override fun areContentsTheSame(oldItem: CozyCell, newItem: CozyCell): Boolean {
                return oldItem.getContentHash() == newItem.getContentHash()
            }

            override fun getChangePayload(oldItem: CozyCell, newItem: CozyCell): Any? {
                return newItem
            }
        }
    }


    var differ = AsyncPagingDataDiffer(CozyDiffCallback, AdapterListUpdateCallback(this))

    var pagingMode: Boolean = false


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
        return if (pagingMode) differ.itemCount else list.size
    }

    override fun onBindViewHolder(holder: CozyViewHolder<CozyCell>, position: Int) {
        if (pagingMode) {
            differ.getItem(position)?.position = position
            differ.getItem(position)?.bind(holder.itemView)
        } else {
            list[position].position = position
            list[position].bind(holder.itemView)
        }
    }

    override fun onBindViewHolder(
        holder: CozyViewHolder<CozyCell>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (pagingMode) {
            differ.getItem(position)?.position = position
            differ.getItem(position)?.bind(holder.itemView)
        } else {
            list[position].position = position
            list[position].bind(holder.itemView)
        }
    }


    fun submitData(lifecycle: Lifecycle, pagingData: PagingData<CozyCell>) {
        pagingMode = true
        differ.submitData(lifecycle, pagingData)
    }

    fun addLoadStateListener(listener: (CombinedLoadStates) -> Unit) {
        differ.addLoadStateListener(listener)
    }

    fun withLoadStateFooter(
        footer: LoadStateAdapter<*>
    ): ConcatAdapter {
        addLoadStateListener { loadStates ->
            footer.loadState = loadStates.append
        }
        return ConcatAdapter(this, footer)
    }


    override fun getItemId(position: Int): Long {
        return if (pagingMode) {
            super.getItemId(position)
        } else {
            list[position].data.hashCode().toLong()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(pagingMode){
            listViewBuilder.add(differ.getItem(position)!!.getViewBuilder())
            differ.getItem(position)!!.getViewBuilder().viewType
        }else{
            listViewBuilder.add(list[position].getViewBuilder())
            list[position].getViewBuilder().viewType
        }
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


    @Deprecated("")
    fun addProgressCell(cell: CozyCell) {
        val newList = ArrayList<CozyCell>()
        newList.addAll(list)
        newList.add(cell)
        val callBack = CozyDiffCallBack(list, newList)
        val diffResult = DiffUtil.calculateDiff(callBack)
        this.list.clear()
        this.list.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    @Deprecated("")
    fun removeProgressCell() {
        if (list.isEmpty()) {
            return
        }

        if (list[list.lastIndex] is DefaultProgressCell) {
            val newList = ArrayList<CozyCell>()
            newList.addAll(list)
            newList.removeAt(newList.size - 1)
            val callBack = CozyDiffCallBack(list, newList)
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
