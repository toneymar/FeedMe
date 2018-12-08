package com.example.marty.feed_me.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.marty.feed_me.R
import com.example.marty.feed_me.data.SearchItem
import kotlinx.android.synthetic.main.recipe_row.view.*

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.ViewHolder>{

    var recipes = mutableListOf<SearchItem?>()
    val context : Context

    companion object {
        val KEY_RECIPE_NAME = "KEY_RECIPE_NAME"
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val tvRecipe = itemView.tvRecipe
    }

    constructor(context: Context, itemList: MutableList<SearchItem?>) : super() {
        this.context = context
        this.recipes = itemList  //the constructor now receives all items from database
    }
    constructor(context: Context) : super(){
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
                R.layout.recipe_row, parent, false
        )
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return recipes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = recipes[position]

        holder.tvRecipe.text = item?.title

        holder.itemView.setOnClickListener{
            Toast.makeText(context,
                    "Clicking on this should open up web page with the recipe. Need to Implement",
                    Toast.LENGTH_LONG)
                    .show()

            //TODO: implement url feature
        }
    }

    fun deleteAll() {
        recipes.clear()
        notifyDataSetChanged()
    }
}


