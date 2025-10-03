package com.example.videoplayerhub.ui.activity

import android.graphics.Matrix
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.videoplayerhub.R
import com.example.videoplayerhub.config.AppDatabase
import com.example.videoplayerhub.model.FavoritePhoto
import kotlinx.coroutines.launch

class PhotoDetailFragment : Fragment() {

    private lateinit var ivPhoto: ImageView
    private lateinit var tvAuthor: TextView
    private lateinit var tvSize: TextView
    private lateinit var btnAddFav: Button

    private lateinit var scaleDetector: ScaleGestureDetector
    private var scaleFactor = 1f
    private val matrix = Matrix()

    private var lastX = 0f
    private var lastY = 0f
    private var posX = 0f
    private var posY = 0f

    private lateinit var db: AppDatabase

    // Data photo
    private var photoId: String = ""
    private var photoAuthor: String = ""
    private var photoWidth: Int = 0
    private var photoHeight: Int = 0
    private var photoDownloadUrl: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_photodetail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ivPhoto = view.findViewById(R.id.ivPhotoDetail)
        tvAuthor = view.findViewById(R.id.tvAuthorDetail)
        tvSize = view.findViewById(R.id.tvSizeDetail)
        btnAddFav = view.findViewById(R.id.btnAddFav)

        ivPhoto.scaleType = ImageView.ScaleType.MATRIX

        db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java,
            "videoplayerhub_db"
        ).build()

        arguments?.let { args ->
            photoId = args.getString("PHOTO_ID", "")
            photoAuthor = args.getString("PHOTO_AUTHOR", "")
            photoWidth = args.getInt("PHOTO_WIDTH", 0)
            photoHeight = args.getInt("PHOTO_HEIGHT", 0)
            photoDownloadUrl = args.getString("PHOTO_DOWNLOAD_URL", "")
        }

        bindPhoto(photoAuthor, photoWidth, photoHeight, photoDownloadUrl)
        checkIfFavorite()

        // Pinch zoom
        scaleDetector = ScaleGestureDetector(requireContext(),
            object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                override fun onScale(detector: ScaleGestureDetector): Boolean {
                    scaleFactor *= detector.scaleFactor
                    scaleFactor = scaleFactor.coerceIn(1.0f, 5.0f)
                    matrix.setScale(
                        scaleFactor,
                        scaleFactor,
                        ivPhoto.width / 2f,
                        ivPhoto.height / 2f
                    )
                    matrix.postTranslate(posX, posY)
                    ivPhoto.imageMatrix = matrix
                    return true
                }
            })

        // Touch listener: zoom + drag
        ivPhoto.setOnTouchListener { _, event ->
            scaleDetector.onTouchEvent(event)

            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    lastX = event.x
                    lastY = event.y
                }
                MotionEvent.ACTION_MOVE -> {
                    if (!scaleDetector.isInProgress) {
                        val dx = event.x - lastX
                        val dy = event.y - lastY
                        posX += dx
                        posY += dy
                        matrix.postTranslate(dx, dy)
                        ivPhoto.imageMatrix = matrix
                        lastX = event.x
                        lastY = event.y
                    }
                }
            }
            true
        }

        btnAddFav.setOnClickListener { addToFavorites() }
    }

    private fun bindPhoto(author: String, width: Int, height: Int, downloadUrl: String) {
        Glide.with(this)
            .load(downloadUrl)
            .placeholder(android.R.color.darker_gray)
            .error(android.R.color.darker_gray)
            .into(ivPhoto)

        tvAuthor.text = getString(R.string.author, author)
        tvSize.text = getString(R.string.photo_size, width, height)
    }

    private fun checkIfFavorite() {
        viewLifecycleOwner.lifecycleScope.launch {
            val existing = db.favoritePhotoDao().getById(photoId)
            if (existing != null) {
                btnAddFav.isEnabled = false
                btnAddFav.text = getString(R.string.already_in_favorites)
            } else {
                btnAddFav.isEnabled = true
                btnAddFav.text = getString(R.string.btn_add_fav)
            }
        }
    }

    private fun addToFavorites() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val existing = db.favoritePhotoDao().getById(photoId)
                if (existing != null) {
                    Toast.makeText(requireContext(), "Already in favorites", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val favorite = FavoritePhoto(
                    id = photoId,
                    author = photoAuthor,
                    downloadUrl = photoDownloadUrl,
                    width = photoWidth,
                    height = photoHeight
                )

                db.favoritePhotoDao().insert(favorite)
                Toast.makeText(requireContext(), R.string.added_to_favorites, Toast.LENGTH_SHORT).show()

                btnAddFav.isEnabled = false
                btnAddFav.text = getString(R.string.already_in_favorites)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), R.string.failed_add_favorite, Toast.LENGTH_SHORT).show()
            }
        }
    }
}