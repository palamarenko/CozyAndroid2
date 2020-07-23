package ua.palamarenko.cozyandroid2.recycler.pagination

import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import ua.palamarenko.cozyandroid2.CozyCell
import ua.palamarenko.cozyandroid2.recycler.CozyViewHolder

abstract class CozyPagingLoadState :
    LoadStateAdapter<CozyViewHolder<CozyCell>>() {

    abstract val layout : Int
    abstract fun bind(view: View, loadState: LoadState)


    override fun onBindViewHolder(holder: CozyViewHolder<CozyCell>, loadState: LoadState) {
        bind(holder.itemView,loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): CozyViewHolder<CozyCell> {
        return CozyViewHolder(View.inflate(parent.context, layout, null))
    }

}