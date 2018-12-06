package com.example.marty.feed_me.data

import android.arch.persistence.room.*

@Dao
interface RecipeDAO {

    @Query("SELECT * FROM recipe")
    fun findAllRecipes(): List<Recipe>

    @Insert
    fun insertRecipe(item: Recipe) : Long

    @Delete
    fun deleteRecipe(item: Recipe)

    @Query("DELETE FROM recipe")
    fun deleteAll()

}