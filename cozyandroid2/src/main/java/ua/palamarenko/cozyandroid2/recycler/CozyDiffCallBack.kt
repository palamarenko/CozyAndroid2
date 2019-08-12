package ua.palamarenko.cozyandroid2.recycler

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import ua.palamarenko.cozyandroid2.CozyCell

class CozyDiffCallBack(private val oldList: List<CozyCell>, private val newList: List<CozyCell>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].data.hashCode() == newList[newItemPosition].data.hashCode()
    }

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        return oldList[oldPosition].data.hashCode() == newList[newPosition].data.hashCode()
    }

    @Nullable
    override fun getChangePayload(oldPosition: Int, newPosition: Int): Any? {
        return super.getChangePayload(oldPosition, newPosition)
    }
}