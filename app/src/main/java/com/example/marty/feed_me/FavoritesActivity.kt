package com.example.marty.feed_me

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import com.example.marty.feed_me.adapter.FavoritesAdapter
import com.example.marty.feed_me.data.AppDatabase
import com.example.marty.feed_me.data.Recipe
import com.example.marty.feed_me.touch.RecipeTouchHelperCallback
import kotlinx.android.synthetic.main.activity_favorites.*

class FavoritesActivity : AppCompatActivity() {

    private lateinit var favoritesAdapter: FavoritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        Thread {

            val recipes = AppDatabase.getInstance(this).recipeDao().findAllRecipes()

            favoritesAdapter = FavoritesAdapter(this@FavoritesActivity, recipes)

            runOnUiThread {
                rvFavorites.adapter = favoritesAdapter

                runOnUiThread {
                    rvFavorites.layoutManager = LinearLayoutManager(this@FavoritesActivity)
                    rvFavorites.adapter = favoritesAdapter
                    val callback = RecipeTouchHelperCallback(favoritesAdapter)
                    val touchHelper = ItemTouchHelper(callback)
                    touchHelper.attachToRecyclerView(rvFavorites)
                }
            }
        }.start()
    }

    fun onAddFavorite(item : Recipe) {
        Thread {
            val id = AppDatabase.getInstance (
                    this@FavoritesActivity).recipeDao().insertRecipe(item)

            item.recipeId = id

            runOnUiThread {
                favoritesAdapter.addRecipe(item)
            }
        }.start()
    }

}
