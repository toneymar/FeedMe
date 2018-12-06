package com.example.marty.feed_me.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "recipe")
data class Recipe(
        @PrimaryKey(autoGenerate = true) var recipeId: Long?,
        @ColumnInfo(name = "recipename") var recipeName: String
) : Serializable