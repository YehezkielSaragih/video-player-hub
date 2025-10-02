package com.example.videoplayerhub.ui.activity

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.videoplayerhub.R
import com.example.videoplayerhub.adapter.FavoriteAdapter
import com.example.videoplayerhub.model.FavoritePhoto

class FavoriteActivity : ComponentActivity() {

    private lateinit var rvFavorites: RecyclerView
    private lateinit var tvEmptyFav: TextView
    private lateinit var btnBack: Button
    private lateinit var adapter: FavoriteAdapter

    // sementara dummy data (ganti dengan Room/DB)
    private val favoriteList = mutableListOf<FavoritePhoto>()

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

        loadFavorites()
    }

    private fun loadFavorites() {
        // TODO: Ambil dari Room. Untuk sekarang dummy
        if (favoriteList.isEmpty()) {
            showEmptyState(true)
        } else {
            adapter.setData(favoriteList)
            showEmptyState(false)
        }
    }

    private fun removeFromFavorites(fav: FavoritePhoto) {
        // TODO: hapus dari Room juga
        favoriteList.remove(fav)
        adapter.setData(favoriteList)
        showEmptyState(favoriteList.isEmpty())
    }

    private fun showEmptyState(isEmpty: Boolean) {
        tvEmptyFav.visibility = if (isEmpty) android.view.View.VISIBLE else android.view.View.GONE
        rvFavorites.visibility = if (isEmpty) android.view.View.GONE else android.view.View.VISIBLE
    }
}
