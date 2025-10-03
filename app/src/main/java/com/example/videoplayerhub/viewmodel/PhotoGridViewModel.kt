package com.example.videoplayerhub.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.videoplayerhub.config.Client
import com.example.videoplayerhub.config.Prefs
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

    var currentPage = 1
    var limit = 30

    // Inisialisasi limit & last page dari Prefs
    fun init(context: Context) {
        limit = Prefs.getLimit(context)
        currentPage = Prefs.getLastPage(context)
    }

    fun refreshPhotos(context: Context) {
        currentPage = 1
        loadPhotos(context, refresh = true, delayMillis = 0)
    }

    fun loadMorePhotos(context: Context) {
        if (_isLoading.value) return
        currentPage++
        loadPhotos(context, refresh = false, delayMillis = 1000)
    }

    private fun loadPhotos(context: Context, refresh: Boolean, delayMillis: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                if (delayMillis > 0) delay(delayMillis)

                val newPhotos = Client.appApi.getList(currentPage, limit)
                _photos.value = if (refresh) newPhotos else _photos.value + newPhotos

                // Simpan state ke Prefs
                Prefs.saveLastPage(context, currentPage)
                Prefs.saveLimit(context, limit)
                Prefs.saveLastItemCount(context, _photos.value.size)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
