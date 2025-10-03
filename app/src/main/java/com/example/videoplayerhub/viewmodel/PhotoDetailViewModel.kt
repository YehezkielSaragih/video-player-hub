package com.example.videoplayerhub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.videoplayerhub.config.AppDatabase
import com.example.videoplayerhub.model.FavoritePhoto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PhotoDetailViewModel : ViewModel() {

    private lateinit var db: AppDatabase

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> get() = _isFavorite

    fun init(database: AppDatabase) {
        db = database
    }

    fun checkFavorite(photoId: String) {
        viewModelScope.launch {
            val existing = db.favoritePhotoDao().getById(photoId)
            _isFavorite.value = existing != null
        }
    }

    fun addToFavorites(photo: FavoritePhoto, onError: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                val existing = db.favoritePhotoDao().getById(photo.id)
                if (existing == null) {
                    db.favoritePhotoDao().insert(photo)
                    _isFavorite.value = true
                } else {
                    _isFavorite.value = true
                }
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}