package com.example.videoplayerhub.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.videoplayerhub.R
import com.example.videoplayerhub.adapter.PhotoAdapter
import com.example.videoplayerhub.config.Client
import com.example.videoplayerhub.model.PicsumPhoto
import kotlinx.coroutines.launch

class PhotoGridActivity : ComponentActivity() {

    private lateinit var rvPhotos: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var btnLoadMore: Button
    private lateinit var tvEmptyState: TextView
    private lateinit var adapter: PhotoAdapter

    private val photos = mutableListOf<PicsumPhoto>()
    private var currentPage = 1
    private val limit = 30

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photogrid)

        rvPhotos = findViewById(R.id.rvPhotos)
        swipeRefresh = findViewById(R.id.swipeRefresh)
        btnLoadMore = findViewById(R.id.btnLoadMore)
        tvEmptyState = findViewById(R.id.tvEmptyState)

        adapter = PhotoAdapter(photos) { photo ->
            // Klik item → bisa buka detail page
        }

        // Set grid column
        val spanCount = 3
        rvPhotos.layoutManager = GridLayoutManager(this, spanCount)
        rvPhotos.adapter = adapter

        swipeRefresh.setOnRefreshListener {
            refreshPhotos()
        }

        btnLoadMore.setOnClickListener {
            loadMorePhotos()
        }

        // Load initial data
        loadPhotos(currentPage)
    }

    private fun refreshPhotos() {
        photos.clear()
        adapter.notifyDataSetChanged()
        currentPage = 1
        loadPhotos(currentPage)
    }

    private fun loadMorePhotos() {
        currentPage++
        loadPhotos(currentPage)
    }

    private fun loadPhotos(page: Int) {
        swipeRefresh.isRefreshing = true
        tvEmptyState.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val newPhotos = Client.appApi.getList(page, limit)
                swipeRefresh.isRefreshing = false

                if (newPhotos.isEmpty() && photos.isEmpty()) {
                    tvEmptyState.visibility = View.VISIBLE
                } else {
                    photos.addAll(newPhotos)
                    adapter.notifyItemRangeInserted(photos.size - newPhotos.size, newPhotos.size)
                }
            } catch (e: Exception) {
                swipeRefresh.isRefreshing = false
                if (photos.isEmpty()) {
                    tvEmptyState.text = getString(R.string.empty_state)
                    tvEmptyState.visibility = View.VISIBLE
                }
                e.printStackTrace()
            }
        }
    }
}
