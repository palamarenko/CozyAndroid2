package ua.palamarenko.cozyandroid2.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import ua.palamarenko.cozyandroid2.CozyCell
import ua.palamarenko.cozyandroid2.ViewBuilder
import ua.palamarenko.cozyandroid2.tools.dpToPx

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