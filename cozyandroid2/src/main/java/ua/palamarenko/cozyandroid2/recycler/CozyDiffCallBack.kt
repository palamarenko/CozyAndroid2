package ua.palamarenko.cozyandroid2.recycler

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import ua.palamarenko.cozyandroid2.CompareItem
import ua.palamarenko.cozyandroid2.CozyCell

class CozyDiffCallBack(
    private val oldList: List<CozyCell>,
    private val newList: List<CozyCell>,
    private val comparator : CompareItem
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return comparator.isSameId(oldList[oldItemPosition].getIdentifier(),newList[newItemPosition].getIdentifier())
    }

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        return comparator.isSameContent(oldList[oldPosition].data,newList[newPosition].data)
    }

    @Nullable
    override fun getChangePayload(oldPosition: Int, newPosition: Int): Any? {
        return super.getChangePayload(oldPosition, newPosition)
    }
}