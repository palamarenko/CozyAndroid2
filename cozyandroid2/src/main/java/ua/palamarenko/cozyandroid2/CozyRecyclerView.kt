package ua.palamarenko.cozyandroid2

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.paging.PagedList
import androidx.paging.PagingData
import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_recycler.view.*
import kotlinx.android.synthetic.main.view_recycler_place_holder.view.*
import ua.palamarenko.cozyandroid2.recycler.ButtonSwipeCallBack
import ua.palamarenko.cozyandroid2.recycler.CozyRecyclerAdapter
import ua.palamarenko.cozyandroid2.recycler.DragAndDropCallbackListener
import ua.palamarenko.cozyandroid2.recycler.layout_manager.CozyLinearLayoutManager
import ua.palamarenko.cozyandroid2.recycler.pagination.CozyPaginationAdapter

import ua.palamarenko.cozyandroid2.tools.LOG_EVENT
import ua.palamarenko.cozyandroid2.tools.inflateView


fun RecyclerView.setCell(list: List<CozyCell>, layoutManager: RecyclerView.LayoutManager? = null) {

    if (layoutManager != null) {
        this.layoutManager = layoutManager
    } else {
        this.layoutManager = LinearLayoutManager(this.context)
    }
    val adapter = CozyRecyclerAdapter()

    this.adapter = adapter
    adapter.updateList(list)
}








class CozyRecyclerView : FrameLayout {

    lateinit var adapter: CozyRecyclerAdapter

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

    fun addDragAndDrop(endListener: () -> Unit = {}) {
        val callback: ItemTouchHelper.Callback = DragAndDropCallbackListener(adapter, true)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(getRecyclerView())
        getRecyclerView().setOnTouchListener { v, event ->
            if (MotionEvent.ACTION_UP == event.action || MotionEvent.ACTION_CANCEL == event.action) {
                endListener.invoke()
            }
            return@setOnTouchListener false
        }
    }


    fun getRecyclerView(): RecyclerView {
        return view.baseRecycler
    }

    fun getFrameLayout(): FrameLayout {
        return view.flRoot
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
            CozyLinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, reverseLayout)
    }

    fun setVerticalLayoutManager(reverseLayout: Boolean = false) {
        view.baseRecycler.layoutManager =
            CozyLinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, reverseLayout)
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
            (view.baseRecycler.layoutManager as GridLayoutManager).scrollToPosition(position)
        }

        if (view.baseRecycler.layoutManager is StaggeredGridLayoutManager) {
            (view.baseRecycler.layoutManager as StaggeredGridLayoutManager).scrollToPosition(
                position
            )
        }

    }

    var loading = false
    var enableLoading = false
    var endlessListener: () -> Unit = {}
    @Deprecated("")
    fun checkForEndlessScroll() {
        if (loading) {
            return
        }

        val visibleItemCount = view.baseRecycler.layoutManager!!.childCount
        val totalItemCount = view.baseRecycler.layoutManager!!.itemCount
        val pastVisiblesItems =
            (view.baseRecycler.layoutManager!! as? LinearLayoutManager)?.findFirstVisibleItemPosition()
                ?: 0

        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
            loading = true
            endlessListener.invoke()
        }
    }
    @Deprecated("")
    fun loadMoreCallBack(listener: () -> Unit) {
        this.endlessListener = listener

        var pastVisiblesItems: Int
        var visibleItemCount: Int
        var totalItemCount: Int

        view.baseRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!enableLoading) {
                    return
                }

                if (dy > 0) {
                    visibleItemCount = view.baseRecycler.layoutManager!!.childCount
                    totalItemCount = view.baseRecycler.layoutManager!!.itemCount
                    pastVisiblesItems =
                        (view.baseRecycler.layoutManager!! as? LinearLayoutManager)?.findFirstVisibleItemPosition()
                            ?: 0
                    if (!loading) {
                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                            loading = true
                            listener.invoke()
                        }
                    }
                }
            }
        })
    }

    @Deprecated("")
    fun isVisibleLastItem(): Boolean {
        val visibleItemCount = view.baseRecycler.layoutManager!!.childCount
        val totalItemCount = getItemCount()
        val pastVisiblesItems =
            (view.baseRecycler.layoutManager!! as? LinearLayoutManager)?.findFirstVisibleItemPosition()
                ?: 0


        return visibleItemCount + pastVisiblesItems >= totalItemCount
    }

    @Deprecated("")
    fun listenEndList(listener: () -> Unit) {

        var pastVisiblesItems: Int
        var visibleItemCount: Int
        var totalItemCount: Int

        view.baseRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = view.baseRecycler.layoutManager!!.childCount
                    totalItemCount = view.baseRecycler.layoutManager!!.itemCount
                    pastVisiblesItems =
                        (view.baseRecycler.layoutManager!! as? LinearLayoutManager)?.findFirstVisibleItemPosition()
                            ?: 0
                    if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                        listener.invoke()
                    }
                }
            }
        })
    }


    fun refrshShow() {
        view.srRefresh.isRefreshing = true
    }


    fun setCell(data: List<CozyCell>) {
        adapter.updateList(data)

        if (adapter.itemCount == 0 && needPlaceHolder) {
            view.flPlaceHolder.visibility = View.VISIBLE
        } else {
            view.flPlaceHolder.visibility = View.GONE
        }

        view.progress.visibility = View.GONE
        refreshHide()
    }


    fun addCell(data: List<CozyCell>) {
        adapter.addList(data)

        if (adapter.itemCount == 0 && needPlaceHolder) {
            view.flPlaceHolder.visibility = View.VISIBLE
        } else {
            view.flPlaceHolder.visibility = View.GONE
        }

        view.progress.visibility = View.GONE
        refreshHide()
    }

    fun addProgressCell() {
        adapter.addProgressCell(DefaultProgressCell())
    }

    fun removeProgressCell() {
        adapter.removeProgressCell()
    }


    fun getItemCount(): Int {
        return adapter.itemCount
    }


    fun setPlaceHolder(view: View) {
        needPlaceHolder = true
        view.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        this.view.flPlaceHolder.addView(view)
    }

    fun setPlaceHolder(charSequence: CharSequence) {
        needPlaceHolder = true
        val view = inflateView(R.layout.view_recycler_place_holder)
        view.tvHolder.text = charSequence
        view.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        this.view.flPlaceHolder.addView(view)
    }


    fun setPlaceHolder(viewId: Int) {
        needPlaceHolder = true
        val view = View.inflate(context, viewId, null)
        view.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        this.view.flPlaceHolder.addView(view)
    }

    fun getPlaceHolder(): FrameLayout {
        return view.flPlaceHolder
    }

    fun removePlaceHolder() {
        view.flPlaceHolder.removeAllViews()
    }

}


abstract class CozyCell {
    abstract val data: Any
    abstract val layout: Int
    abstract fun bind(view: View)

    var position: Int = 0
    open var identifier: Long? = null
    open var contentHash: Long? = null

    open fun getIdentifier(): Long {
        if (identifier != null) {
            return identifier!!
        }

        return data.hashCode().toLong()
    }

    open fun getContentHash(): Long {
        if (contentHash != null) {
            return contentHash!!
        }

        return data.hashCode().toLong()
    }


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


const val PROGRESS_CELL = "_PROGRESS_CELL"

class DefaultProgressCell(override val data: Any = PROGRESS_CELL) : CozyCell() {
    override val layout: Int = R.layout.cell_progress
    override fun bind(view: View) {
        view.progress.visibility = View.VISIBLE
    }
}
