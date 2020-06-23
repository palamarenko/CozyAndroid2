package ua.palamarenko.cozyandroid2.cozy_view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_item_nav_bar.view.*
import ua.palamarenko.cozyandroid2.R
import ua.palamarenko.cozyandroid2.tools.click
import ua.palamarenko.cozyandroid2.tools.dpToPx


class NavigationItem(
    override val id: Int,
    val iconSelect: Int,
    val iconUnSelect: Int,
    val title: TitleItem? = null
) : BaseNavigationItem(id)


class CustomViewItem(
    override val id: Int,
    val view: View
) : BaseNavigationItem(id)


abstract class BaseNavigationItem(open val id: Int)


class TitleItem(val select: CharSequence, val unSelect: CharSequence)


class NavItemWithView(val item: BaseNavigationItem, val view: View)


class CozyNavigateView : FrameLayout {

    lateinit var view: LinearLayout

    var itemList = ArrayList<NavItemWithView>()
    private var lastItem: NavItemWithView? = null
    var listnener: (NavigationItem) -> Unit = {}


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)


    fun initView(list: List<BaseNavigationItem>) {
        itemList.clear()
        view = LinearLayout(context)
        view.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

        list.forEach {
            when (it) {
                is NavigationItem -> inflateNavigationItem(it)
                is CustomViewItem -> inflateCustomItem(it)
            }

        }
        addView(view)
    }

    public fun getNavBarItems() : ArrayList<NavItemWithView>{
        return itemList
    }



    private fun inflateCustomItem(it: CustomViewItem) {
        val params =
            LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        params.weight = 1f
        it.view.layoutParams = params

        view.addView(it.view)
        itemList.add(NavItemWithView(it, it.view))
    }


    private fun inflateNavigationItem(it: NavigationItem) {
        val itemView = View.inflate(context, R.layout.view_item_nav_bar, null)
        itemView.ivIcon.setImageResource(it.iconUnSelect)
        itemView.flClick.click(false) { setItem(true, it.id) }

        val params =
            LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        params.weight = 1f
        itemView.layoutParams = params
        if (it.title != null) {
            itemView.tvTitle.visibility = View.VISIBLE
            itemView.tvTitle.text = it.title!!.unSelect
        } else {
            itemView.tvTitle.visibility = View.GONE
        }


        view.addView(itemView)
        itemList.add(NavItemWithView(it, itemView))
    }

    fun setIconSize(dpSize: Int) {
        itemList.forEach {
            if (it.item is NavigationItem) {
                val lp = it.view.ivIcon.layoutParams as LinearLayout.LayoutParams
                lp.height = dpToPx(dpSize.toFloat())
                lp.width = dpToPx(dpSize.toFloat())
                it.view.ivIcon.layoutParams = lp
            }

        }
    }


    fun setItem(needCallBack: Boolean = false, id: Int) {
        if (lastItem?.item?.id == id) {
            return
        }
        itemList.forEach {
            if (it.item is NavigationItem) {
                if (it.item.id != id) {
                    it.view.ivIcon.setImageResource(it.item.iconUnSelect)
                    if (it.item.title != null) {
                        it.view.tvTitle.text = it.item.title.unSelect
                    }
                    animateView(it.view.llAnimate, false)
                } else {
                    it.view.ivIcon.setImageResource(it.item.iconSelect)
                    if (it.item.title != null) {
                        it.view.tvTitle.text = it.item.title.select
                    }
                    animateView(it.view.llAnimate, true)
                    lastItem = it
                    if (needCallBack) {
                        listnener.invoke(it.item)
                    }
                }
            }
        }
    }


    private fun animateView(view: View, selected: Boolean) {
        if (selected) {
            view.animate()
                .scaleX(1.1f)
                .scaleY(1.1f)
                .setDuration(100)
                .start()
        } else {
            view.animate()
                .scaleX(1.0f)
                .scaleY(1.0f)
                .setDuration(100)
                .start()
        }


    }


}