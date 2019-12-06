package ua.palamarenko.cozyandroid2

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.recyclerview.widget.*
import kotlinx.android.synthetic.main.view_recycler.view.*
import ua.palamarenko.cozyandroid2.recycler.ButtonSwipeCallBack
import ua.palamarenko.cozyandroid2.recycler.CozyDiffCallBack
import androidx.recyclerview.widget.ItemTouchHelper

fun RecyclerView.init(
    adapter: RecyclerView.Adapter<*>,
    manager: RecyclerView.LayoutManager = LinearLayoutManager(this.context),
    divider: Boolean = false
) {
    this.adapter = adapter
    this.layoutManager = manager
    if (divider) {
        while (this.itemDecorationCount > 0) {
            this.removeItemDecorationAt(0)
        }
        this.addItemDecoration(
            DividerItemDecoration(
                this.context,
                DividerItemDecoration.VERTICAL
            )
        )
    }
}


class CozyRecyclerView : FrameLayout {

    lateinit var adapter: CozyRecyclerAdapter

    private var adapterListener: (CozyCell) -> Unit = {}

    private var comparatorItem: CompareItem = object : CompareItem {}


    private lateinit var view: View

    var needPlaceHolder = false


    constructor(context: Context) : this(context, null) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }


    private fun init() {
        adapter = CozyRecyclerAdapter(adapterListener)
        view = View.inflate(context, R.layout.view_recycler, null)
        addView(view)
        view.baseRecycler.init(adapter)

        view.srRefresh.isRefreshing = false
        view.srRefresh.isEnabled = false
    }

    fun needProgress() {
        view.progress.visibility = View.VISIBLE
    }


    fun getRecyclerView(): RecyclerView {
        return view.baseRecycler
    }

    fun setDivider(
        need: Boolean = true,
        decorator: RecyclerView.ItemDecoration = DividerItemDecoration(
            this.context,
            DividerItemDecoration.VERTICAL
        )
    ) {
        while (view.baseRecycler.itemDecorationCount > 0) {
            view.baseRecycler.removeItemDecorationAt(0)
        }
        if (need) {

            view.baseRecycler.addItemDecoration(decorator)
        }
    }


    fun setSwipeView(view: View, click: (view: View, position: Int) -> Unit) {
        val swipeController = ButtonSwipeCallBack(view, click)
        val itemTouchHelper = ItemTouchHelper(swipeController)
        itemTouchHelper.attachToRecyclerView(getRecyclerView())
        getRecyclerView().addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                swipeController.onDraw(c)
            }
        })
    }


    fun setRefreshing(refreshing: Boolean): CozyRecyclerView {
        view.srRefresh.isEnabled = refreshing
        return this
    }

    fun refreshListener(refreshListener: () -> Unit) {
        view.srRefresh.setOnRefreshListener {
            refreshListener.invoke()
        }
    }

    fun refreshHide() {
        view.srRefresh.isRefreshing = false
    }


    fun setHorizontalLayoutManager(reverseLayout: Boolean = false) {
        view.baseRecycler.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, reverseLayout)
    }

    fun setVerticalLayoutManager(reverseLayout: Boolean = false) {
        view.baseRecycler.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, reverseLayout)
    }

    fun setGridLayoutManager(spanCount: Int) {
        view.baseRecycler.layoutManager = GridLayoutManager(view.context, spanCount)

    }

    fun setLayoutManager(layoutManager: RecyclerView.LayoutManager) {
        view.baseRecycler.layoutManager = layoutManager
    }


    fun scrollToPosition(position: Int) {

        if (view.baseRecycler.layoutManager is LinearLayoutManager) {

            val smoothScroller =
                object : androidx.recyclerview.widget.LinearSmoothScroller(context) {
                    override fun getVerticalSnapPreference(): Int {
                        return SNAP_TO_START
                    }
                }
            smoothScroller.targetPosition = position


            (view.baseRecycler.layoutManager as LinearLayoutManager).startSmoothScroll(
                smoothScroller
            )
        }


        if (view.baseRecycler.layoutManager is GridLayoutManager) {
            (view.baseRecycler.layoutManager as GridLayoutManager).smoothScrollToPosition(
                view.baseRecycler,
                RecyclerView.State(),
                0
            )
        }


        if (view.baseRecycler.layoutManager is StaggeredGridLayoutManager) {
            (view.baseRecycler.layoutManager as StaggeredGridLayoutManager).smoothScrollToPosition(
                view.baseRecycler,
                RecyclerView.State(),
                0
            )
        }

    }

    fun listenEndList(listener: () -> Unit) {
        view.baseRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    listener.invoke()
                }
            }
        })
    }

    fun refrshShow() {
        view.srRefresh.isRefreshing = true
    }

    fun setComparator(comparatorItem: CompareItem) {
        this.comparatorItem = comparatorItem
    }


    fun setCell(data: List<CozyCell>) {
        adapter.updateList(data, comparatorItem)
        view.flPlaceHolder.visibility =
            if (adapter.itemCount == 0 && needPlaceHolder) View.VISIBLE else View.GONE
        view.progress.visibility = View.GONE
    }

    fun addProgressCell() {
        adapter.addProgressCell(DefaultProgressCell(), comparatorItem)
    }

    fun removeProgressCell() {
        adapter.removeProgressCell(comparatorItem)

    }


    fun setPlaceHolder(view: View) {
        view.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        this.view.flPlaceHolder.addView(view)
    }

    fun removePlaceHolder() {
        view.flPlaceHolder.removeAllViews()
    }


    fun click(adapterListener: (CozyCell) -> Unit) {
        this.adapterListener = adapterListener
    }


    class CozyRecyclerAdapter(val adapterListener: (CozyCell) -> Unit) :
        RecyclerView.Adapter<CozyViewHolder<CozyCell>>() {


        private var list = ArrayList<CozyCell>()

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): CozyViewHolder<CozyCell> {
            return CozyViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    viewType,
                    parent,
                    false
                )
            )
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: CozyViewHolder<CozyCell>, position: Int) {
            holder.itemView.setOnClickListener {
                adapterListener.invoke(list[position])
            }

            list[position].bind(holder.itemView)
        }

        override fun getItemId(position: Int): Long {
            return list[position].data.hashCode().toLong()
        }

        override fun getItemViewType(position: Int): Int {
            return list[position].layout
        }


        fun updateList(data: List<CozyCell>, compareItem: CompareItem) {
            val callBack = CozyDiffCallBack(list, data, compareItem)
            val diffResult = DiffUtil.calculateDiff(callBack)
            this.list.clear()
            this.list.addAll(data)
            diffResult.dispatchUpdatesTo(this)
        }

        fun addProgressCell(cell: CozyCell, compareItem: CompareItem) {
            val newList = ArrayList<CozyCell>()
            newList.addAll(list)
            newList.add(cell)
            val callBack = CozyDiffCallBack(list, newList, compareItem)
            val diffResult = DiffUtil.calculateDiff(callBack)
            this.list.clear()
            this.list.addAll(newList)
            diffResult.dispatchUpdatesTo(this)
        }

        fun removeProgressCell(compareItem: CompareItem) {
            val newList = ArrayList<CozyCell>()
            newList.addAll(list)
            newList.removeAt(newList.size - 1)
            val callBack = CozyDiffCallBack(list, newList, compareItem)
            val diffResult = DiffUtil.calculateDiff(callBack)
            this.list.clear()
            this.list.addAll(newList)
            diffResult.dispatchUpdatesTo(this)
        }

    }

    class CozyViewHolder<T : CozyCell>(itemView: View) : RecyclerView.ViewHolder(itemView)
}

interface CompareItem {
    fun isSameId(data1: Any, data2: Any): Boolean {
        return data1.hashCode() == data2.hashCode()
    }

    fun isSameContent(data1: Any, data2: Any): Boolean {
        return data1.hashCode() == data2.hashCode()
    }
}


abstract class CozyCell {
    abstract val data: Any
    abstract val layout: Int
    abstract fun bind(view: View)
}


const val PROGRESS_CELL = "_PROGRESS_CELL"

class DefaultProgressCell(override val data: Any = PROGRESS_CELL) : CozyCell() {
    override val layout: Int = R.layout.cell_progress
    override fun bind(view: View) {}
}
