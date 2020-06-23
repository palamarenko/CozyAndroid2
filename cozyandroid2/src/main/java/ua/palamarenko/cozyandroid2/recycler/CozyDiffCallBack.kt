package ua.palamarenko.cozyandroid2.recycler

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import ua.palamarenko.cozyandroid2.CozyCell

class CozyDiffCallBack(
    private val oldList: List<CozyCell>,
    private val newList: List<CozyCell>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].getIdentifier() == newList[newItemPosition].getIdentifier()
    }

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        return oldList[oldPosition].getContentHash() == newList[newPosition].getContentHash()
    }

    @Nullable
    override fun getChangePayload(oldPosition: Int, newPosition: Int): Any? {
        return newList.get(newPosition)
    }
}