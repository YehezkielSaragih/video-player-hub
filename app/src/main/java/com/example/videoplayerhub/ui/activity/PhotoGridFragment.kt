package com.example.videoplayerhub.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.videoplayerhub.R
import com.example.videoplayerhub.adapter.PhotoAdapter
import com.example.videoplayerhub.config.Client
import com.example.videoplayerhub.model.PicsumPhoto
import kotlinx.coroutines.launch

class PhotoGridFragment : Fragment() {

    private lateinit var rvPhotos: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var tvEmptyState: TextView
    private lateinit var adapter: PhotoAdapter

    private val photos = mutableListOf<PicsumPhoto>()
    private var currentPage = 1
    private val limit = 30
    private var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_photogrid, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvPhotos = view.findViewById(R.id.rvPhotos)
        swipeRefresh = view.findViewById(R.id.swipeRefresh)
        tvEmptyState = view.findViewById(R.id.tvEmptyState)

        adapter = PhotoAdapter(photos) { photo ->
            val fragment = PhotoDetailFragment().apply {
                arguments = Bundle().apply {
                    putString("PHOTO_ID", photo.id)
                    putString("PHOTO_AUTHOR", photo.author)
                    putInt("PHOTO_WIDTH", photo.width)
                    putInt("PHOTO_HEIGHT", photo.height)
                    putString("PHOTO_DOWNLOAD_URL", photo.downloadUrl)
                }
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }

        rvPhotos.layoutManager = GridLayoutManager(requireContext(), 3)
        rvPhotos.adapter = adapter

        swipeRefresh.setOnRefreshListener { refreshPhotos() }

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

        viewLifecycleOwner.lifecycleScope.launch {
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