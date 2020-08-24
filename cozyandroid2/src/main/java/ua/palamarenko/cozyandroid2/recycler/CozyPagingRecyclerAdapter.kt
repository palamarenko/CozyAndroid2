package ua.palamarenko.cozyandroid2.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import ua.palamarenko.cozyandroid2.CozyCell
import ua.palamarenko.cozyandroid2.tools.LOG_EVENT

class CozyPagingRecyclerAdapter : PagingDataAdapter<CozyCell, CozyViewHolder<CozyCell>>(CozyDiffCallback){

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


    override fun getItemViewType(position: Int): Int {

        LOG_EVENT("POSITION",if(getItem(position)==null) position else -1)
        return getItem(position)!!.layout
    }

    override fun onBindViewHolder(holder: CozyViewHolder<CozyCell>, position: Int) {
        getItem(position)?.position = position
        getItem(position)?.bind(holder.itemView)

    }

    override fun onBindViewHolder(
        holder: CozyViewHolder<CozyCell>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        getItem(position)?.position = position
        getItem(position)?.bind(holder.itemView)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CozyViewHolder<CozyCell> {
        return CozyViewHolder(LayoutInflater.from(parent.context).inflate(
            viewType, parent, false))
    }

}