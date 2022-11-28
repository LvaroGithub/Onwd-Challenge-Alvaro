package com.onwd.challenge.devicelist.presentation.layoutmanager

import android.content.Context
import android.view.View
import androidx.cardview.widget.CardView
import androidx.core.view.updatePaddingRelative
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import kotlin.math.abs
import kotlin.math.min

class ScaleLinearLayoutManager(context: Context?, private val onDeviceSelected: (index: Int) -> Unit) :
    LinearLayoutManager(context) {

    private val maxScale = 0.15f
    private lateinit var recyclerView: RecyclerView

    override fun scrollHorizontallyBy(dx: Int, recycler: Recycler?, state: RecyclerView.State?): Int {
        val orientation = orientation
        return if (orientation == HORIZONTAL) {
            val scrolled = super.scrollHorizontallyBy(dx, recycler, state)
            scaleItems()
            scrolled
        } else {
            0
        }
    }
    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        if (childCount == 0 && state.itemCount > 0) {
            val firstChild = recycler.getViewForPosition(0)
            measureChildWithMargins(firstChild, 0, 0)
            recycler.recycleView(firstChild)
        }
        super.onLayoutChildren(recycler, state)
    }

    override fun measureChildWithMargins(child: View, widthUsed: Int, heightUsed: Int) {
        val lp = (child.layoutParams as RecyclerView.LayoutParams).absoluteAdapterPosition
        super.measureChildWithMargins(child, widthUsed, heightUsed)
        if (lp != 0 && lp != itemCount - 1) return
        when (orientation) {
            RecyclerView.HORIZONTAL -> {
                val hPadding = ((width - child.measuredWidth) / 2).coerceAtLeast(0)
                if (!reverseLayout) {
                    if (lp == 0) recyclerView.updatePaddingRelative(start = hPadding)
                    if (lp == itemCount - 1) recyclerView.updatePaddingRelative(end = hPadding)
                } else {
                    if (lp == 0) recyclerView.updatePaddingRelative(end = hPadding)
                    if (lp == itemCount - 1) recyclerView.updatePaddingRelative(start = hPadding)
                }
            }
        }
    }

    override fun onAttachedToWindow(view: RecyclerView) {
        recyclerView = view
        super.onAttachedToWindow(view)
    }

    override fun onLayoutCompleted(state: RecyclerView.State?) {
        super.onLayoutCompleted(state)
        scaleItems()
    }


    private fun scaleItems(){
        val midpoint = width / 2f
        val s0 = 1f
        for (i in 0 until childCount) {
            val child: View? = getChildAt(i)
            child?.let { child ->
                val childMidpoint = (getDecoratedRight(child) + getDecoratedLeft(child)) / 2f
                val halfChildWidth = child.width / 2f
                val d = min(halfChildWidth, abs(midpoint - childMidpoint))
                val scale = s0 + (maxScale * (1 - (d / halfChildWidth)))
                if(scale > 1) {
                    (child as CardView).cardElevation = 4f
                    onDeviceSelected(getPosition(child))
                } else (child as CardView).cardElevation = 0f
                child.scaleX = scale
                child.scaleY = scale
            }
        }
    }
}