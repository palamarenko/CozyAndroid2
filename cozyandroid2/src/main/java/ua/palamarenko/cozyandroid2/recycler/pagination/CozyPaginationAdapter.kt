package ua.palamarenko.cozyandroid2.recycler.pagination

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import ua.palamarenko.cozyandroid2.CozyCell
import ua.palamarenko.cozyandroid2.ViewBuilder
import ua.palamarenko.cozyandroid2.recycler.CozyViewHolder

class CozyPaginationAdapter :
    PagingDataAdapter<CozyCell, CozyViewHolder<CozyCell>>(CozyDiffCallback) {

    private var listViewBuilder = HashSet<ViewBuilder>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CozyViewHolder<CozyCell> {
        return CozyViewHolder(listViewBuilder.find { it.viewType == viewType }!!.buildView(parent))
    }

    override fun onBindViewHolder(holder: CozyViewHolder<CozyCell>, position: Int) {
        getItem(position)!!.bind(holder.itemView)
    }

    override fun getItemViewType(position: Int): Int {
        listViewBuilder.add(getItem(position)!!.getViewBuilder())
        return getItem(position)!!.getViewBuilder().viewType
    }

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
}