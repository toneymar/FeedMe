package com.example.marty.feed_me.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "todo")
data class Todo(
        @PrimaryKey(autoGenerate = true) var todoId: Long?,
        @ColumnInfo(name = "city name") var itemName: String
) : Serializable