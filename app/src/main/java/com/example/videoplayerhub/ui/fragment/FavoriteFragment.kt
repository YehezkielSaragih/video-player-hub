package com.example.videoplayerhub.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.videoplayerhub.R
import com.example.videoplayerhub.adapter.FavoriteAdapter
import com.example.videoplayerhub.config.AppDatabase
import com.example.videoplayerhub.model.FavoritePhoto
import com.example.videoplayerhub.viewmodel.FavoriteViewModel
import androidx.fragment.app.viewModels
import androidx.appcompat.app.AlertDialog

class FavoriteFragment : Fragment() {

    private lateinit var rvFavorites: RecyclerView
    private lateinit var tvEmptyFav: TextView
    private lateinit var adapter: FavoriteAdapter
    private lateinit var db: AppDatabase

    private val favoriteViewModel: FavoriteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvFavorites = view.findViewById(R.id.rvFavorites)
        tvEmptyFav = view.findViewById(R.id.tvEmptyFav)

        // inisialisasi database
        db = Room.databaseBuilder(
            requireContext().applicationContext,
            AppDatabase::class.java,
            "videoplayerhub_db"
        ).build()
        favoriteViewModel.init(db)

        adapter = FavoriteAdapter(
            mutableListOf(),
            onRemove = { fav ->
                // tampilkan popup konfirmasi sebelum hapus
                AlertDialog.Builder(requireContext())
                    .setTitle("Delete Favorite")
                    .setMessage("Are you sure you want to remove this photo from your favorites list?")
                    .setPositiveButton("Yes") { _, _ ->
                        favoriteViewModel.removeFavorite(fav) // hapus dari DB
                        Toast.makeText(requireContext(), "Photo successfully removed from favorites", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            },
            onItemClick = { fav -> openDetail(fav) }
        )

        rvFavorites.layoutManager = LinearLayoutManager(requireContext())
        rvFavorites.adapter = adapter

        // observe data dari ViewModel
        favoriteViewModel.favorites.observe(viewLifecycleOwner) { list ->
            adapter.setData(list)
            showEmptyState(list.isEmpty())
        }
    }

    private fun openDetail(fav: FavoritePhoto) {
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
