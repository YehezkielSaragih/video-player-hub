package com.example.videoplayerhub.ui.activity

import android.content.Intent
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
    private lateinit var btnViewFavorites: Button
    private lateinit var tvEmptyState: TextView
    private lateinit var adapter: PhotoAdapter

    private val photos = mutableListOf<PicsumPhoto>()
    private var currentPage = 1
    private val limit = 30
    private var isLoading = false   // Flag untuk mencegah load ganda

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photogrid)

        rvPhotos = findViewById(R.id.rvPhotos)
        swipeRefresh = findViewById(R.id.swipeRefresh)
        btnViewFavorites = findViewById(R.id.btnViewFavorites)
        tvEmptyState = findViewById(R.id.tvEmptyState)

        adapter = PhotoAdapter(photos) { photo ->
            val intent = Intent(this, PhotoDetailActivity::class.java)
            intent.putExtra("PHOTO_ID", photo.id)
            intent.putExtra("PHOTO_AUTHOR", photo.author)
            intent.putExtra("PHOTO_WIDTH", photo.width)
            intent.putExtra("PHOTO_HEIGHT", photo.height)
            intent.putExtra("PHOTO_DOWNLOAD_URL", photo.downloadUrl)
            startActivity(intent)
        }

        rvPhotos.layoutManager = GridLayoutManager(this, 3)
        rvPhotos.adapter = adapter

        swipeRefresh.setOnRefreshListener { refreshPhotos() }
        btnViewFavorites.setOnClickListener {
            startActivity(Intent(this, FavoriteActivity::class.java))
        }

        // Infinite scroll listener
        rvPhotos.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    loadMorePhotos()
                }
            }
        })

        swipeRefresh.isRefreshing = true
        rvPhotos.post { loadPhotos(currentPage) }
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
        isLoading = true
        swipeRefresh.isRefreshing = true
        tvEmptyState.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val newPhotos = Client.appApi.getList(page, limit)
                swipeRefresh.isRefreshing = false
                isLoading = false

                if (newPhotos.isEmpty() && photos.isEmpty()) {
                    tvEmptyState.visibility = View.VISIBLE
                } else {
                    val startPos = photos.size
                    photos.addAll(newPhotos)
                    adapter.notifyItemRangeInserted(startPos, newPhotos.size)
                }
            } catch (e: Exception) {
                swipeRefresh.isRefreshing = false
                isLoading = false
                if (photos.isEmpty()) {
                    tvEmptyState.text = getString(R.string.empty_state)
                    tvEmptyState.visibility = View.VISIBLE
                }
                e.printStackTrace()
            }
        }
    }
}
