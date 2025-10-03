package com.example.videoplayerhub.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.videoplayerhub.R
import com.example.videoplayerhub.adapter.PhotoAdapter
import com.example.videoplayerhub.model.PicsumPhoto
import com.example.videoplayerhub.viewmodel.PhotoGridViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PhotoGridFragment : Fragment() {

    private lateinit var rvPhotos: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var tvEmptyState: TextView
    private lateinit var adapter: PhotoAdapter
    private val photoList = mutableListOf<PicsumPhoto>()

    private val viewModel: PhotoGridViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_photogrid, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvPhotos = view.findViewById(R.id.rvPhotos)
        swipeRefresh = view.findViewById(R.id.swipeRefresh)
        tvEmptyState = view.findViewById(R.id.tvEmptyState)

        adapter = PhotoAdapter(photoList) { photo ->
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

        // Swipe to refresh
        swipeRefresh.setOnRefreshListener {
            viewModel.refreshPhotos()
        }

        // Observe photos
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.photos.collectLatest { list ->
                val startPos = photoList.size
                if (viewModel.isLoading.value && startPos > 0) {
                    // Append new items
                    val newItems = list.takeLast(list.size - startPos)
                    photoList.addAll(newItems)
                    adapter.notifyItemRangeInserted(startPos, newItems.size)
                } else {
                    // Refresh
                    photoList.clear()
                    photoList.addAll(list)
                    adapter.notifyDataSetChanged()
                }

                tvEmptyState.visibility = if (photoList.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        // Observe loading state
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collectLatest { loading ->
                swipeRefresh.isRefreshing = loading
            }
        }

        // Infinite scroll
        rvPhotos.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!viewModel.isLoading.value &&
                    (visibleItemCount + firstVisibleItemPosition) >= totalItemCount &&
                    firstVisibleItemPosition >= 0
                ) {
                    viewModel.loadMorePhotos()
                }
            }
        })

        // Initial load
        viewModel.refreshPhotos()
    }
}
