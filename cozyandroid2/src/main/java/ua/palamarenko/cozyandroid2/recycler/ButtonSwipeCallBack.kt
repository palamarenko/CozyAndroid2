package ua.palamarenko.cozyandroid2.recycler

import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ua.palamarenko.cozyandroid2.tools.LOG_EVENT
import android.view.MotionEvent
import androidx.recyclerview.widget.ItemTouchHelper.*
import android.graphics.*
import android.graphics.Bitmap
import androidx.annotation.NonNull
import android.view.ViewGroup
import ua.palamarenko.cozyandroid2.tools.dpToPx




class ButtonSwipeCallBack(
    val view: View,
    val click: (view: View, position: Int) -> Unit
) : ItemTouchHelper.Callback() {

    private var mainY = 0
    private var mainX = 0
    private var buttonWidth = 0.0f


    private var swipeBack = false

    private var buttonShowedState = false


    private var currentItemViewHolder: RecyclerView.ViewHolder? = null

    init {
        view.measure(0, 0)
        buttonWidth = view.measuredWidth.toFloat()
    }


    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(0, LEFT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if (swipeBack) {
            swipeBack = buttonShowedState != false
            return 0
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        var dX = dX
        if (actionState == ACTION_STATE_SWIPE) {
            if (buttonShowedState) {
//               dX = Math.max(dX, buttonWidth)
               dX = Math.min(dX, -buttonWidth)
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            } else {
                setTouchListener(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }

        if (!buttonShowedState) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
        currentItemViewHolder = viewHolder
    }

    private fun setTouchListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        recyclerView.setOnTouchListener { v, event ->
            swipeBack =
                event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP
            if (swipeBack) {
                buttonShowedState = true

                if (buttonShowedState) {
                    setTouchDownListener(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                    setItemsClickable(recyclerView, false)
                }
            }
            false
        }
    }

    private fun setTouchDownListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        recyclerView.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                setTouchUpListener(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

            false
        }

    }

    private fun setTouchUpListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        recyclerView.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    0f,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                recyclerView.setOnTouchListener { v, event -> false }
                setItemsClickable(recyclerView, true)
                swipeBack = false



                findClickView(
                    (mainX - event.x).toInt() * -1,
                    (event.y - mainY).toInt(),
                    viewHolder.adapterPosition
                )
                buttonShowedState = false
                currentItemViewHolder = null
            }

            viewHolder.itemView.elevation = 0.0f
            false
        }
    }

    private fun findClickView(x: Int, y: Int, position: Int) {
        val clickView = findViewAt(view as ViewGroup, x, y)
        if (clickView != null) {
            click.invoke(clickView, position)
        }
    }


    private fun findViewAt(viewGroup: ViewGroup, x: Int, y: Int): View? {
        for (i in 0 until viewGroup.childCount) {
            val child = viewGroup.getChildAt(i)
            if (child is ViewGroup) {
                val foundView = findViewAt(child, x, y)
                if (foundView != null && foundView.isShown) {
                    return foundView
                }
            } else {
                val location = IntArray(2)
                child.getLocationOnScreen(location)
                val rect = Rect(
                    location[0],
                    location[1],
                    location[0] + child.width,
                    location[1] + child.height
                )
                if (rect.contains(x, y)) {
                    return child
                }
            }
        }

        return null
    }


    private fun setItemsClickable(recyclerView: RecyclerView, isClickable: Boolean) {
        for (i in 0 until recyclerView.childCount) {
            recyclerView.getChildAt(i).isClickable = isClickable
        }
    }
    private fun drawButtons(c: Canvas, viewHolder: RecyclerView.ViewHolder) {
        val buttonWidthWithoutPadding = buttonWidth - dpToPx(5f)
        val itemView = viewHolder.itemView
        val bm = createBitmapFromView(view)

        c.drawBitmap(bm, itemView.right - buttonWidthWithoutPadding, itemView.top.toFloat() + itemView.height/2 - bm.height/2, null)
        mainY = (itemView.top.toFloat() + itemView.height/2 - bm.height/2).toInt()
        mainX = itemView.right - buttonWidth.toInt()

    }
    private fun createBitmapFromView(@NonNull view: View): Bitmap {

        view.layout(0, 0, view.measuredWidth, view.measuredHeight)

        val bitmap = Bitmap.createBitmap(
            view.measuredWidth,
            view.measuredHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        val background = view.background

        background?.draw(canvas)
        view.draw(canvas)

        return bitmap
    }
    fun onDraw(c: Canvas) {
        if (currentItemViewHolder != null) {
            drawButtons(c, currentItemViewHolder!!)
        }
    }
}