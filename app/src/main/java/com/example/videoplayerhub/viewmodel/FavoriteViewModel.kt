package com.example.videoplayerhub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.videoplayerhub.config.AppDatabase
import com.example.videoplayerhub.model.FavoritePhoto
import kotlinx.coroutines.launch

class FavoriteViewModel : ViewModel() {

    private lateinit var db: AppDatabase

    private lateinit var _favorites: LiveData<List<FavoritePhoto>>
    val favorites: LiveData<List<FavoritePhoto>>
        get() = _favorites

    // inisialisasi db, panggil dari fragment
    fun init(database: AppDatabase) {
        db = database
        _favorites = db.favoritePhotoDao().getAllFavoritesFlow().asLiveData()
    }

    fun removeFavorite(fav: FavoritePhoto) {
        viewModelScope.launch {
            db.favoritePhotoDao().delete(fav)
        }
    }
}