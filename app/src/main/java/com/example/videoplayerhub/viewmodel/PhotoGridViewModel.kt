package com.example.videoplayerhub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.videoplayerhub.config.Client
import com.example.videoplayerhub.model.PicsumPhoto
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PhotoGridViewModel : ViewModel() {

    private val _photos = MutableStateFlow<List<PicsumPhoto>>(emptyList())
    val photos: StateFlow<List<PicsumPhoto>> = _photos

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var currentPage = 1
    private val limit = 30

    fun refreshPhotos(isFirstLoad: Boolean = false) {
        currentPage = 1
        loadPhotos(refresh = true, delayMillis = 0)
    }

    fun loadMorePhotos() {
        if (_isLoading.value) return
        currentPage++
        loadPhotos(refresh = false, delayMillis = 1000)
    }

    private fun loadPhotos(refresh: Boolean = false, delayMillis: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                if (delayMillis > 0) delay(delayMillis)
                val newPhotos = Client.appApi.getList(currentPage, limit)
                _photos.value = if (refresh) newPhotos else _photos.value + newPhotos
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}