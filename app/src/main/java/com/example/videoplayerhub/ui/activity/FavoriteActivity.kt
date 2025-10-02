package com.example.videoplayerhub.ui.activity

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.videoplayerhub.R
import com.example.videoplayerhub.adapter.FavoriteAdapter
import com.example.videoplayerhub.config.AppDatabase
import com.example.videoplayerhub.model.FavoritePhoto
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FavoriteActivity : ComponentActivity() {

    private lateinit var rvFavorites: RecyclerView
    private lateinit var tvEmptyFav: TextView
    private lateinit var btnBack: Button
    private lateinit var adapter: FavoriteAdapter

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        rvFavorites = findViewById(R.id.rvFavorites)
        tvEmptyFav = findViewById(R.id.tvEmptyFav)
        btnBack = findViewById(R.id.btnBack)

        adapter = FavoriteAdapter(mutableListOf()) { fav ->
            removeFromFavorites(fav)
        }

        rvFavorites.layoutManager = LinearLayoutManager(this)
        rvFavorites.adapter = adapter

        btnBack.setOnClickListener { finish() }

        // Initialize Room
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "videoplayerhub_db"
        ).build()

        loadFavoritesFromDb()
    }

    private fun loadFavoritesFromDb() {
        lifecycleScope.launch {
            db.favoritePhotoDao().getAllFavoritesFlow().collectLatest { list ->
                adapter.setData(list)
                showEmptyState(list.isEmpty())
            }
        }
    }

    private fun removeFromFavorites(fav: FavoritePhoto) {
        lifecycleScope.launch {
            db.favoritePhotoDao().delete(fav)
        }
    }

    private fun showEmptyState(isEmpty: Boolean) {
        tvEmptyFav.visibility = if (isEmpty) android.view.View.VISIBLE else android.view.View.GONE
        rvFavorites.visibility = if (isEmpty) android.view.View.GONE else android.view.View.VISIBLE
    }
}