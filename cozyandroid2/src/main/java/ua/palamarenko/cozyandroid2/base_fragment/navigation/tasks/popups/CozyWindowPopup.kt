package ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.popups

import android.R.attr.startX
import android.R.attr.startY
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import kotlinx.android.synthetic.main.cell_defaultpopup.view.*
import kotlinx.android.synthetic.main.view_popup_window.view.*
import ua.palamarenko.cozyandroid2.CozyCell
import ua.palamarenko.cozyandroid2.CozyLibrary
import ua.palamarenko.cozyandroid2.R
import ua.palamarenko.cozyandroid2.setCell
import ua.palamarenko.cozyandroid2.tools.LOG_EVENT
import ua.palamarenko.cozyandroid2.tools.click
import ua.palamarenko.cozyandroid2.tools.dpToPx


class CozyWindowPopup {

    fun create(view: View, list: List<String>, click: (String) -> Unit) {
        val contentView = View.inflate(view.context, R.layout.view_popup_window, null)
        val popup = PopupWindow(
            contentView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        val dismissCallback: () -> Unit = {
            popup.dismiss()
        }

        val popupList = list.map { DefaultPopupCell(it, click) }
        popupList.forEach { it.dismissCallback = dismissCallback }
        popup.contentView.popupRecycler.setCell(popupList)
        popup.showAsDropDown(view)
    }

    fun createWithCell(view: View, list: List<PopupCell>) {
        val contentView = View.inflate(view.context, R.layout.view_popup_window, null)
        val popup =
            PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
        val dismissCallback: () -> Unit = {
            popup.dismiss()
        }

        list.forEach {
            it.dismissCallback = dismissCallback
        }

        popup.contentView.popupRecycler.setCell(list)
        popup.showAsDropDown(view)
    }

}


class DefaultPopupCell(override val data: String, val click: (String) -> Unit) : PopupCell() {
    override val layout = R.layout.cell_defaultpopup

    override fun bind(view: View) {
        view.tvPopupTitle.text = data
        view.click {
            click.invoke(data)
            dismissCallback.invoke()
        }
    }

}

abstract class PopupCell : CozyCell() {

    lateinit var dismissCallback: () -> Unit


}
