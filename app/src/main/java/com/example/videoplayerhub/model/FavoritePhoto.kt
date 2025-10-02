package com.example.videoplayerhub.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoritePhoto(
    @PrimaryKey val id: String,
    val author: String,
    val downloadUrl: String,
    val width: Int,
    val height: Int
)
