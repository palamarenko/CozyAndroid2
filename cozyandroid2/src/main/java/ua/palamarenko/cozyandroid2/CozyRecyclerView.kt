package ua.palamarenko.cozyandroid2

import android.content.Context
import android.graphics.Canvas
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.recyclerview.widget.*
import kotlinx.android.synthetic.main.view_recycler.view.*
import ua.palamarenko.cozyandroid2.recycler.ButtonSwipeCallBack
import ua.palamarenko.cozyandroid2.recycler.CozyDiffCallBack
import ua.palamarenko.cozyandroid2.tools.dpToPx
import java.lang.Exception


fun RecyclerView.setCell(list: List<CozyCell>, layoutManager: RecyclerView.LayoutManager? = null) {

    if (layoutManager != null) {
        this.layoutManager = layoutManager
    }
    val adapter = CozyRecyclerView.CozyRecyclerAdapter()
    val comparatorItem: CompareItem = object : CompareItem {}

    this.adapter = adapter
    adapter.updateList(list, comparatorItem)

}


class CozyRecyclerView : FrameLayout {

    lateinit var adapter: CozyRecyclerAdapter


    private var comparatorItem: CompareItem = object : CompareItem {}


    private lateinit var view: View

    var needPlaceHolder = false


    constructor(context: Context) : this(context, null) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }


    private fun init(context: Context, attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.CozyRecyclerView, 0, 0)
        view = if (ta.hasValue(R.styleable.CozyRecyclerView_Sliding)) {
            if (ta.getBoolean(R.styleable.CozyRecyclerView_Sliding, false)) {
                View.inflate(context, R.layout.view_recycler_sliding, null)
            } else {
                View.inflate(context, R.layout.view_recycler, null)
            }
        } else {
            View.inflate(context, R.layout.view_recycler, null)
        }

        adapter = CozyRecyclerAdapter()

        addView(view)
        view.baseRecycler.init(adapter)

        view.srRefresh.isRefreshing = false
        view.srRefresh.isEnabled = false
    }

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

    @Deprecated("")
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
        setRefreshing(true)
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


    fun moveToPosition(position: Int) {

        if (view.baseRecycler.layoutManager is LinearLayoutManager) {
            (view.baseRecycler.layoutManager as LinearLayoutManager).scrollToPosition(position)
        }

        if (view.baseRecycler.layoutManager is GridLayoutManager) {
            (view.baseRecycler.layoutManager as GridLayoutManager).scrollToPosition(0)
        }

        if (view.baseRecycler.layoutManager is StaggeredGridLayoutManager) {
            (view.baseRecycler.layoutManager as StaggeredGridLayoutManager).scrollToPosition(0)
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

        if (adapter.itemCount == 0 && needPlaceHolder) {
            view.flPlaceHolder.visibility = View.VISIBLE
        } else {
            view.flPlaceHolder.visibility = View.GONE
        }

        view.progress.visibility = View.GONE
    }

    fun addProgressCell() {
        adapter.addProgressCell(DefaultProgressCell(), comparatorItem)
    }

    fun removeProgressCell() {
        adapter.removeProgressCell(comparatorItem)
    }


    fun setPlaceHolder(view: View) {
        needPlaceHolder = true
        view.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        this.view.flPlaceHolder.addView(view)
    }


    fun setPlaceHolder(id: Int) {
        needPlaceHolder = true
        val view = View.inflate(context, id, null)
        view.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        this.view.flPlaceHolder.addView(view)
    }

    fun getPlaceHolder(): FrameLayout {
        return view.flPlaceHolder
    }

    fun removePlaceHolder() {
        view.flPlaceHolder.removeAllViews()
    }


    class CozyRecyclerAdapter() : RecyclerView.Adapter<CozyViewHolder<CozyCell>>() {


        private var list = ArrayList<CozyCell>()
        private var listViewBuilder = ArrayList<ViewBuilder>()

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): CozyViewHolder<CozyCell> {
            return CozyViewHolder(
                listViewBuilder.find { it.viewType == viewType }!!.buildView(
                    parent
                )
            )
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: CozyViewHolder<CozyCell>, position: Int) {
            list[position].bind(holder.itemView)
        }

        override fun getItemId(position: Int): Long {
            return list[position].data.hashCode().toLong()
        }

        override fun getItemViewType(position: Int): Int {
            return list[position].getViewBuilder().viewType
        }


        fun updateList(data: List<CozyCell>, compareItem: CompareItem) {
            listViewBuilder.clear()
            data.forEach { cell ->
                if (listViewBuilder.find { it.viewType == cell.getViewBuilder().viewType } == null) {
                    listViewBuilder.add(cell.getViewBuilder())
                }
            }

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

    open fun getViewBuilder(): ViewBuilder {
        return object : ViewBuilder(layout) {
            override fun buildView(parent: ViewGroup): View {
                return LayoutInflater.from(parent.context).inflate(
                    layout,
                    parent,
                    false
                )
            }
        }
    }
}


abstract class ViewBuilder(val viewType: Int) {
    abstract fun buildView(parent: ViewGroup): View
}


abstract class SlidingCozyCell : CozyCell() {
    abstract val slidingLayout: Int

    override fun getViewBuilder(): ViewBuilder {
        return object : ViewBuilder(layout + slidingLayout) {
            override fun buildView(parent: ViewGroup): View {
                val visibleView = LayoutInflater.from(parent.context).inflate(layout, parent, false)
                val slidingView =
                    LayoutInflater.from(parent.context).inflate(slidingLayout, parent, false)
                visibleView.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                val mainView = LinearLayout(parent.context)
                mainView.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                mainView.orientation = LinearLayout.HORIZONTAL
                mainView.addView(visibleView)
                val mainFraim = FrameLayout(parent.context)
                mainFraim.tag = "SLIDING_VIEW"
                mainFraim.layoutParams =
                    LinearLayout.LayoutParams(dpToPx(1000f), LinearLayout.LayoutParams.MATCH_PARENT)
                mainView.addView(mainFraim)
                val firstFrame = FrameLayout(parent.context)
                firstFrame.addView(slidingView)
                mainFraim.addView(firstFrame)
                return mainView
            }
        }
    }

    fun stopSliding(view: View) {
        view.findViewWithTag<View>("SLIDING_VIEW").visibility = View.GONE

    }

    fun startSliding(view: View) {
        view.findViewWithTag<View>("SLIDING_VIEW").visibility = View.VISIBLE
    }
}


const val PROGRESS_CELL = "_PROGRESS_CELL"

class DefaultProgressCell(override val data: Any = PROGRESS_CELL) : CozyCell() {
    override val layout: Int = R.layout.cell_progress
    override fun bind(view: View) {}
}
