package com.example.marty.feed_me.adapter

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.marty.feed_me.R
import com.example.marty.feed_me.data.Favorite
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.recipe_row.view.*


class FavoritesAdapter(var context: Context, var uid: String) : RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    var favorites = mutableListOf<Favorite>()
    var keys = mutableListOf<String>()

    private var lastPosition = -1

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRecipe = itemView.tvRecipe
        val cbFavs = itemView.cbFavs
        val ivPreview = itemView.ivPreview
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
                R.layout.recipe_row, parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return favorites.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = favorites[holder.adapterPosition]

        //get values from firebase
        holder.tvRecipe.text = item.recipeName
        holder.cbFavs.isChecked = true

        Glide.with(context).load(item.imageURL).into(holder.ivPreview)

        holder.cbFavs.setOnClickListener {
            removeFavorite(holder.adapterPosition)
        }

        holder.itemView.setOnClickListener {
            intentOpenURL(item.recipeURL)
        }
    }

    private fun intentOpenURL(webURL: String?) {
        val intentSearch = Intent(Intent.ACTION_WEB_SEARCH)
        intentSearch.putExtra(SearchManager.QUERY, webURL)
        context.startActivity(intentSearch)
    }

    fun deleteAll() {
       /* recipes.clear()
        notifyDataSetChanged()*/
    }

    fun addRecipe(item: Favorite, key: String) {
        favorites.add(item)
        keys.add(key)
        notifyDataSetChanged()
    }

    fun removeFavorite(position: Int) {
        FirebaseFirestore.getInstance().collection("favorites" + uid).document(
                keys[position]
        ).delete()

        favorites.removeAt(position)
        keys.removeAt(position)
        notifyItemRemoved(position)
    }

    fun removeFavoriteByKey(key: String) {
        val index = keys.indexOf(key)
        if (index != -1) {
            favorites.removeAt(index)
            keys.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}

