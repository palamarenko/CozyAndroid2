package ua.palamarenko.cozyandroid2

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.*
import kotlinx.android.synthetic.main.cell_default_loader.view.btRetry
import kotlinx.android.synthetic.main.view_cozy_recycler.view.*
import kotlinx.android.synthetic.main.view_recycler.view.baseRecycler
import kotlinx.android.synthetic.main.view_recycler.view.flPlaceHolder
import kotlinx.android.synthetic.main.view_recycler.view.flRoot
import kotlinx.android.synthetic.main.view_recycler.view.progress
import kotlinx.android.synthetic.main.view_recycler.view.srRefresh
import kotlinx.android.synthetic.main.view_recycler_place_holder.view.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ua.palamarenko.cozyandroid2.recycler.CozyPagingRecyclerAdapter
import ua.palamarenko.cozyandroid2.recycler.CozyRecyclerAdapter
import ua.palamarenko.cozyandroid2.recycler.layout_manager.CozyLinearLayoutManager
import ua.palamarenko.cozyandroid2.recycler.pagination.CozyDefaultLoaderView
import ua.palamarenko.cozyandroid2.recycler.pagination.CozyDefaultLoaderViewHeader
import ua.palamarenko.cozyandroid2.recycler.pagination.CozyDefaultLoaderViewSettings
import ua.palamarenko.cozyandroid2.recycler.pagination.CozyPagingLoadState
import ua.palamarenko.cozyandroid2.tools.LOG_EVENT
import ua.palamarenko.cozyandroid2.tools.click
import ua.palamarenko.cozyandroid2.tools.inflateView

class CozyRecyclerPagingView : FrameLayout {

    private val cozyAdapter = CozyPagingRecyclerAdapter()

    private lateinit var view: View

    var needPlaceHolder = false
    var showRefresh: Boolean = false


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
        view = View.inflate(context, R.layout.view_cozy_recycler, null)

        addView(view)
        view.baseRecycler.adapter = cozyAdapter
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

    fun setRefreshForPagingEnable() {
        refreshListener {
            showRefresh = true
            pagingRefresh()
        }
    }



    fun getRecyclerView(): RecyclerView {
        return view.baseRecycler
    }


    fun getAdapter(): CozyPagingRecyclerAdapter {
        return cozyAdapter
    }


    fun initPagingList(footer: CozyPagingLoadState? = CozyDefaultLoaderView { pagingRetry() },
                       header: CozyPagingLoadState? = null,
                       errorCallBack: (LoadState.Error) -> Unit = {}) {
        when {
            footer != null && header != null -> {
                view.baseRecycler.adapter =
                    cozyAdapter.withLoadStateHeaderAndFooter(footer = footer, header = header)
            }
            footer != null -> {
                view.baseRecycler.adapter = cozyAdapter.withLoadStateFooter(footer = footer)
            }
            else -> {
                view.baseRecycler.adapter = cozyAdapter
            }
        }
        cozyAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.NotLoading) {
                view.progress.visibility = View.GONE
            }

            if(showRefresh)
                view.srRefresh.isRefreshing = loadState.refresh is LoadState.Loading

            if (loadState.source.refresh is LoadState.Error) {
                view.flError.visibility = View.VISIBLE
                errorCallBack(loadState.source.refresh as LoadState.Error)
            } else {
                view.flError.visibility = View.GONE
            }
        }

    }


    @OptIn(ExperimentalPagingApi::class)
    fun submitData(
        lifecycle: Lifecycle,
        pagingData: PagingData<CozyCell>) {

        cozyAdapter.submitData(lifecycle, pagingData)

    }


    @OptIn(ExperimentalPagingApi::class)
    suspend fun submitData(
        pagingData: PagingData<CozyCell>) {
        cozyAdapter.submitData(pagingData)
    }


    fun setErrorState(view: View) {
        view.flError.addView(view)
    }


    fun pagingRefresh() {
        cozyAdapter.refresh()
    }

    fun pagingRetry() {
        cozyAdapter.retry()
    }


    fun getFrameLayout(): FrameLayout {
        return view.flRoot
    }


    fun setPlaceHolder(view: View) {
        needPlaceHolder = true
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

}