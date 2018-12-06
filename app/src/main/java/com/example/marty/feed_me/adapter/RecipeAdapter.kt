package com.example.marty.feed_me.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marty.feed_me.MainActivity
import com.example.marty.feed_me.R
import com.example.marty.feed_me.data.AppDatabase
import com.example.marty.feed_me.data.Recipe
import com.example.marty.feed_me.touch.RecipeTouchHelperAdapter
import kotlinx.android.synthetic.main.recipe_row.view.*
import java.util.*

class RecipeAdapter : RecyclerView.Adapter<RecipeAdapter.ViewHolder>, RecipeTouchHelperAdapter {

    var recipies = mutableListOf<Recipe>()
    companion object {
        val KEY_RECIPE_NAME = "KEY_RECIPE_NAME"
    }

    val context : Context
    constructor(context: Context, itemList: List<Recipe>) : super() {
        this.context = context
        this.recipies.addAll(itemList)  //the constructor now receives all items from database
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
        return recipies.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = recipies[position]

        holder.tvRecipe.text = item.recipeName
        holder.btnDelete.setOnClickListener{
            deleteRecipe(holder.adapterPosition)
        }
        holder.itemView.setOnClickListener{

        }
    }
    private fun deleteRecipe(adapterPosition: Int) {
        Thread {
            AppDatabase.getInstance(
                    context).recipeDao().deleteRecipe(recipies[adapterPosition])
            recipies.removeAt(adapterPosition)
            (context as MainActivity).runOnUiThread {
                notifyItemRemoved(adapterPosition)
            }
        }.start()
    }

    fun deleteAll(){
        if(recipies.isNotEmpty()){
            Thread{
                for(i in 0..recipies.size){
                    AppDatabase.getInstance(
                            context).recipeDao().deleteRecipe(recipies[i])
                    recipies.removeAt(i)
                    (context as MainActivity).runOnUiThread {
                        notifyItemRemoved(i)
                    }
                }
            }.start()
        }
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val tvRecipe = itemView.tvRecipe
        val btnDelete = itemView.ivDelete
    }

    fun addRecipe(item: Recipe){
        recipies.add(0, item)
        notifyItemInserted(0)
    }

    override fun onDismiss(position: Int) {
        deleteRecipe(position)
        deleteAll()
    }
    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(recipies, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }
}