package com.example.videoplayerhub.ui.activity

import android.os.Bundle
import android.widget.TextView
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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class FavoriteFragment : Fragment() {

    private lateinit var rvFavorites: RecyclerView
    private lateinit var tvEmptyFav: TextView
    private lateinit var adapter: FavoriteAdapter
    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout khusus untuk FavoriteFragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvFavorites = view.findViewById(R.id.rvFavorites)
        tvEmptyFav = view.findViewById(R.id.tvEmptyFav)

        adapter = FavoriteAdapter(
            mutableListOf(),
            onRemove = { fav -> removeFromFavorites(fav) },
            onItemClick = { fav -> openDetail(fav) }
        )

        rvFavorites.layoutManager = LinearLayoutManager(requireContext())
        rvFavorites.adapter = adapter

        db = Room.databaseBuilder(
            requireContext().applicationContext,
            AppDatabase::class.java,
            "videoplayerhub_db"
        ).build()

        loadFavoritesFromDb()
    }

    private fun loadFavoritesFromDb() {
        viewLifecycleOwner.lifecycleScope.launch {
            db.favoritePhotoDao().getAllFavoritesFlow().collectLatest { list ->
                adapter.setData(list)
                showEmptyState(list.isEmpty())
            }
        }
    }

    private fun removeFromFavorites(fav: FavoritePhoto) {
        viewLifecycleOwner.lifecycleScope.launch {
            db.favoritePhotoDao().delete(fav)
        }
    }

    private fun openDetail(fav: FavoritePhoto) {
        // Panggil PhotoDetailFragment, bukan Activity lagi
        val fragment = PhotoDetailFragment().apply {
            arguments = Bundle().apply {
                putString("PHOTO_ID", fav.id)
                putString("PHOTO_AUTHOR", fav.author)
                putInt("PHOTO_WIDTH", fav.width)
                putInt("PHOTO_HEIGHT", fav.height)
                putString("PHOTO_DOWNLOAD_URL", fav.downloadUrl)
            }
        }

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showEmptyState(isEmpty: Boolean) {
        tvEmptyFav.visibility = if (isEmpty) View.VISIBLE else View.GONE
        rvFavorites.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }
}