package com.example.marty.feed_me.touch

interface RecipeTouchHelperAdapter{
    fun onDismiss(position: Int)
    fun onItemMoved(fromPosition: Int, toPosition: Int)

}