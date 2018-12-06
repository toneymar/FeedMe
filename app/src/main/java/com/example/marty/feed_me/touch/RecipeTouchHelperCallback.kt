package com.example.marty.feed_me.touch

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper

class RecipeTouchHelperCallback(private val recipeTouchHelperAdapter: RecipeTouchHelperAdapter)
    : ItemTouchHelper.Callback() {

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END   //supports left and right swiping
        return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        recipeTouchHelperAdapter.onItemMoved(
                viewHolder.adapterPosition,
                target.adapterPosition)
        return true
    }
    //calls onDismiss to delete the item
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        recipeTouchHelperAdapter.onDismiss(viewHolder.adapterPosition)
    }
}