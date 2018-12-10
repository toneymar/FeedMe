package com.example.marty.feed_me.adapter

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.marty.feed_me.MainActivity
import com.example.marty.feed_me.R
import com.example.marty.feed_me.data.AppDatabase
import com.example.marty.feed_me.data.Recipe
import com.example.marty.feed_me.touch.RecipeTouchHelperAdapter
import kotlinx.android.synthetic.main.recipe_row.view.*
import java.util.*


class FavoritesAdapter : RecyclerView.Adapter<FavoritesAdapter.ViewHolder>, RecipeTouchHelperAdapter {

    val context : Context
    var recipes = mutableListOf<Recipe>()

    companion object {
        val KEY_RECIPE_NAME = "KEY_RECIPE_NAME"
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val tvRecipe = itemView.tvRecipe
        val cbFavs = itemView.cbFavs
        val ivPreview = itemView.ivPreview
    }

    constructor(context: Context, itemList: List<Recipe>) : super() {
        this.context = context
        recipes.addAll(itemList)  //the constructor now receives all items from database
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

        //get values from firebase
        holder.tvRecipe.text = item.recipeName
        holder.cbFavs.isChecked = item.fvChecked

        Glide.with(context).load(item.recipePicURL).into(holder.ivPreview)

        holder.itemView.setOnClickListener {
            intentOpenURL(item.recipeURL)
        }
    }

    private fun intentOpenURL(webURL: String?) {
        val intentSearch = Intent(Intent.ACTION_WEB_SEARCH)
        intentSearch.putExtra(SearchManager.QUERY, webURL)
        context.startActivity(intentSearch)
    }

    private fun deleteRecipe(adapterPosition: Int) {

        //delete from firebase
        Thread {
            AppDatabase.getInstance(
                    context).recipeDao().deleteRecipe(recipes[adapterPosition])
            recipes.removeAt(adapterPosition)
            (context as MainActivity).runOnUiThread {
                notifyItemRemoved(adapterPosition)
            }
        }.start()
    }

    fun deleteAll() {
        recipes.clear()
        notifyDataSetChanged()
    }

    fun addRecipe(item: Recipe){
        recipes.add(0, item)
        notifyItemInserted(0)
    }

    override fun onDismiss(position: Int) {
        deleteRecipe(position)
        deleteAll()
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(recipes, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }
}

