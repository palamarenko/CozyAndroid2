package ua.palamarenko.cozyandroid2

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_recycler.view.*

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

    lateinit var adapter: CjRecyclerAdapter

    private var adapterListener: (CozyCell) -> Unit = {}


    private lateinit var view: View

    var needPlaceHolder = false

    constructor(context: Context) : this(context, null) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }


    private fun init() {
        adapter = CjRecyclerAdapter(adapterListener)
        view = View.inflate(context, R.layout.view_recycler, null)
        addView(view)
        view.baseRecycler.init(adapter)

        view.srRefresh.isRefreshing = false
        view.srRefresh.isEnabled = false
    }

    fun needProgress(){
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


    fun scrollToPosition(position: Int) {

        val smoothScroller = object : androidx.recyclerview.widget.LinearSmoothScroller(context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }
        smoothScroller.targetPosition = position


        (view.baseRecycler.layoutManager as LinearLayoutManager).startSmoothScroll(smoothScroller);


    }

    fun refrshShow() {
        view.srRefresh.isRefreshing = true
    }


    fun setCell(data: List<CozyCell>) {
        adapter.updateList(data)
        view.tvPlaceHolder.visibility = if (adapter.itemCount == 0 && needPlaceHolder) View.VISIBLE else View.GONE
        view.progress.visibility = View.GONE
    }

    fun setPlaceHolder(id: Int) {
        view.tvPlaceHolder.setText(id)
    }


    fun click(adapterListener: (CozyCell) -> Unit) {
        this.adapterListener = adapterListener
    }


    class CjRecyclerAdapter(val adapterListener: (CozyCell) -> Unit) :
        RecyclerView.Adapter<CozyViewHolder<CozyCell>>() {


        private var list = ArrayList<CozyCell>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CozyViewHolder<CozyCell> {
            return CozyViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
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


        fun updateList(data: List<CozyCell>) {
            this.list.clear()
            this.list.addAll(data)
            notifyDataSetChanged()
        }
    }

    class CozyViewHolder<T : CozyCell>(itemView: View) : RecyclerView.ViewHolder(itemView)
}


abstract class CozyCell(val data: Any) {
    abstract val layout: Int
    abstract fun bind(view: View)
}
