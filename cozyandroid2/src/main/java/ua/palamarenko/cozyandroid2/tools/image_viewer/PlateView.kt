package ua.palamarenko.cozyandroid2.tools.image_viewer

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.view_plate.view.*
import ua.palamarenko.cozyandroid2.R
import ua.palamarenko.cozyandroid2.tools.CozyTimer
import ua.palamarenko.cozyandroid2.tools.click
import java.util.*

class PlateView : FrameLayout {

    private lateinit var view: View
    private var maxCount = 0
    private var lastUse = 0L

    private val timer = CozyTimer()


    constructor(context: Context) : this(context, null) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }


    fun initView() {
        view = View.inflate(context, R.layout.view_plate, null)

        addView(view)
    }

    fun listen( listener : (Int) -> Unit) {
        view.ivNext.click {
            listener.invoke(1)
        }

        view.ivPrevious.click {
            listener.invoke(-1)
        }
    }


    fun setMaxCount(count: Int) {
        maxCount = count
    }


    fun showPlate(){
        view.visibility = View.VISIBLE
        timer.startTimer(1) {
            view.visibility = View.GONE
        }
    }

    fun currentItem(item: Int) {
        view.visibility = View.VISIBLE

        timer.startTimer(1) {
            view.visibility = View.GONE
        }

        lastUse = Date().time

        view.ivPrevious.visibility = if (item == 0) View.INVISIBLE else View.VISIBLE
        view.ivNext.visibility = if (item == maxCount - 1) View.INVISIBLE else View.VISIBLE
        view.tvCount.text = "${item + 1}/$maxCount"

    }
}