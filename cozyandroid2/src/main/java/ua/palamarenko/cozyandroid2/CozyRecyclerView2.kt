package ua.palamarenko.cozyandroid2

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.paging.PagingData
import androidx.recyclerview.widget.*
import kotlinx.android.synthetic.main.view_recycler.view.baseRecycler
import kotlinx.android.synthetic.main.view_recycler.view.flPlaceHolder
import kotlinx.android.synthetic.main.view_recycler.view.flRoot
import kotlinx.android.synthetic.main.view_recycler.view.progress
import kotlinx.android.synthetic.main.view_recycler.view.srRefresh
import kotlinx.android.synthetic.main.view_recycler_place_holder.view.*
import ua.palamarenko.cozyandroid2.recycler.CozyRecyclerAdapter
import ua.palamarenko.cozyandroid2.recycler.layout_manager.CozyLinearLayoutManager
import ua.palamarenko.cozyandroid2.recycler.pagination.CozyPagingLoadState
import ua.palamarenko.cozyandroid2.tools.inflateView

class CozyRecyclerView2 : FrameLayout {

    private val cozyAdapter = CozyRecyclerAdapter()

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
        view =  View.inflate(context, R.layout.view_cozy_recycler, null)

        addView(view)
        view.baseRecycler.layoutManager = LinearLayoutManager(this.context)

        view.srRefresh.isRefreshing = false
        view.srRefresh.isEnabled = false
    }

    fun setRefreshing(refreshing: Boolean) {
        view.srRefresh.isEnabled = refreshing
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

    fun refreshShow() {
        view.srRefresh.isRefreshing = true
    }


    fun getRecyclerView(): RecyclerView {
        return view.baseRecycler
    }

    fun submitData(data: List<CozyCell>) {
        cozyAdapter.updateList(data)

        if (cozyAdapter.itemCount == 0 && needPlaceHolder) {
            view.flPlaceHolder.visibility = View.VISIBLE
        } else {
            view.flPlaceHolder.visibility = View.GONE
        }

        view.progress.visibility = View.GONE
        refreshHide()
    }


    fun getPagingAdapter(): CozyRecyclerAdapter {
        return cozyAdapter
    }


    fun submitData(
        lifecycle: Lifecycle,
        pagingData: PagingData<CozyCell>,
        state: CozyPagingLoadState?
    ) {

        if (state != null) {
            view.baseRecycler.adapter = cozyAdapter.withLoadStateFooter(footer = state)
        } else {
            view.baseRecycler.adapter = cozyAdapter
        }

        cozyAdapter.submitData(lifecycle, pagingData)


        if (cozyAdapter.itemCount == 0 && needPlaceHolder) {
            view.flPlaceHolder.visibility = View.VISIBLE
        } else {
            view.flPlaceHolder.visibility = View.GONE
        }

        view.progress.visibility = View.GONE
        refreshHide()
    }


    fun getFrameLayout(): FrameLayout {
        return view.flRoot
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

    fun firstProgressEnable() {
        view.progress.visibility = View.VISIBLE
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
}