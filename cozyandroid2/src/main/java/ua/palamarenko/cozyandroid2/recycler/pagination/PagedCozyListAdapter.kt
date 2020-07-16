package ua.palamarenko.cozyandroid2.recycler.pagination

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import ua.palamarenko.cozyandroid2.CozyCell
import ua.palamarenko.cozyandroid2.recycler.CozyDiffCallBack
import ua.palamarenko.cozyandroid2.recycler.CozyViewHolder

class PagedCozyListAdapter : PagedListAdapter<CozyCell, CozyViewHolder<CozyCell>>(UserDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CozyViewHolder<CozyCell> {
      return  CozyViewHolder(LayoutInflater.from(parent.context)
          .inflate(viewType, parent, false))
    }

    override fun getItemId(position: Int): Long {
        return currentList!![position]!!.getIdentifier()
    }

    override fun onBindViewHolder(holder: CozyViewHolder<CozyCell>, position: Int) {
        currentList!![position]!!.position = position
        currentList!![position]!!.bind(holder.itemView)
    }

    override fun getItemViewType(position: Int): Int {
        return currentList!![position]!!.layout
    }

}


val UserDiffCallback = object : DiffUtil.ItemCallback<CozyCell>() {

    override fun areItemsTheSame(oldItem: CozyCell, newItem: CozyCell): Boolean {
        return oldItem.getIdentifier() == newItem.getIdentifier()
    }

    override fun areContentsTheSame(oldItem: CozyCell, newItem: CozyCell): Boolean {
        return oldItem.getContentHash() == newItem.getContentHash()
    }


}