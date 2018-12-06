package com.example.marty.feed_me.data

// result generated from /json

data class RecipeResult(val count: Number?,
                val recipes: List<Recipes>?)

data class Recipes(val publisher: String?, val f2f_url: String?, val title: String?,
                   val source_url: String?, val recipe_id: String?, val image_url: String?,
                   val social_rank: Number?, val publisher_url: String?)
