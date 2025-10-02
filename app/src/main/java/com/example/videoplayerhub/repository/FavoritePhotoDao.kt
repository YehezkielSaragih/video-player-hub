package com.example.videoplayerhub.repository

import androidx.room.*
import com.example.videoplayerhub.model.FavoritePhoto
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritePhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(f: FavoritePhoto)

    @Delete
    suspend fun delete(f: FavoritePhoto)

    @Query("SELECT * FROM favorites ORDER BY id DESC")
    fun getAllFavoritesFlow(): Flow<List<FavoritePhoto>>

    @Query("SELECT COUNT(*) FROM favorites")
    suspend fun count(): Int

    @Query("SELECT * FROM favorites WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): FavoritePhoto?
}
