package com.example.marty.feed_me.network

import com.example.marty.feed_me.data.RecipeResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

//https://www.food2fork.com/api/search?key=YOUR_API_KEY&q=chicken%20breast&page=1

interface RecipeAPI{

    @GET("/api/search")
    fun getRecipe(@Query("key") key : String,
                   @Query("q") q : String,
                   @Query("page") page : Int) : Call<RecipeResult>
}