package com.example.marty.feed_me.adapter

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.provider.Settings.Global.getString
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.marty.feed_me.FavoritesActivity
import com.example.marty.feed_me.MainActivity
import com.example.marty.feed_me.R
import com.example.marty.feed_me.data.Recipe
import com.example.marty.feed_me.data.SearchItem
import kotlinx.android.synthetic.main.recipe_row.view.*

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    var recipes = mutableListOf<SearchItem?>()
    val context: Context

    companion object {
        val KEY_RECIPE_NAME = "KEY_RECIPE_NAME"
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRecipe = itemView.tvRecipe
        val ivPreview = itemView.ivPreview
        val cbFavs = itemView.cbFavs
    }

    constructor(context: Context, itemList: MutableList<SearchItem?>) : super() {
        this.context = context
        this.recipes = itemList  //the constructor now receives all items from database
    }

    constructor(context: Context) : super() {
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
        Glide.with(context)
                .load(item?.picURL)
                .into(holder.ivPreview)

        holder.itemView.setOnClickListener {
            intentOpenURL(item?.webURL)
        }

        holder.cbFavs.setOnClickListener {
            if (holder.cbFavs.isChecked) {
                (context as FavoritesActivity).onAddFavorite(Recipe(
                        null, item?.title.toString(), item?.webURL.toString(), item?.picURL.toString(), true))
            }
            else {
                //if in favorites -> remove
            }
        }
    }

    private fun intentOpenURL(webURL: String?) {
        val intentSearch = Intent(Intent.ACTION_WEB_SEARCH)
        intentSearch.putExtra(SearchManager.QUERY, webURL)
        context.startActivity(intentSearch)
    }

    fun deleteAll() {
        recipes.clear()
        notifyDataSetChanged()
    }
}


