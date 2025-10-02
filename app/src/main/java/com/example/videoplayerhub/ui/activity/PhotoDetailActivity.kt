package com.example.videoplayerhub.ui.activity

import android.graphics.Matrix
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.videoplayerhub.R
import com.example.videoplayerhub.model.FavoritePhoto
import com.example.videoplayerhub.config.AppDatabase
import kotlinx.coroutines.launch

class PhotoDetailActivity : ComponentActivity() {

    private lateinit var ivPhoto: ImageView
    private lateinit var tvAuthor: TextView
    private lateinit var tvSize: TextView
    private lateinit var btnBack: Button
    private lateinit var btnAddFav: Button

    private lateinit var scaleDetector: ScaleGestureDetector
    private var scaleFactor = 1f
    private val matrix = Matrix()

    // Room database
    private lateinit var db: AppDatabase

    // Photo data
    private lateinit var photoId: String
    private lateinit var photoAuthor: String
    private var photoWidth = 0
    private var photoHeight = 0
    private lateinit var photoDownloadUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photodetail)

        ivPhoto = findViewById(R.id.ivPhotoDetail)
        tvAuthor = findViewById(R.id.tvAuthorDetail)
        tvSize = findViewById(R.id.tvSizeDetail)
        btnBack = findViewById(R.id.btnBack)
        btnAddFav = findViewById(R.id.btnAddFav)

        // Initialize Room database
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "videoplayerhub_db"
        ).build()

        // Receive data from Intent
        photoId = intent.getStringExtra("PHOTO_ID") ?: ""
        photoAuthor = intent.getStringExtra("PHOTO_AUTHOR") ?: "Unknown"
        photoWidth = intent.getIntExtra("PHOTO_WIDTH", 0)
        photoHeight = intent.getIntExtra("PHOTO_HEIGHT", 0)
        photoDownloadUrl = intent.getStringExtra("PHOTO_DOWNLOAD_URL") ?: ""

        bindPhoto(photoAuthor, photoWidth, photoHeight, photoDownloadUrl)

        // Pinch zoom
        scaleDetector = ScaleGestureDetector(this, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                scaleFactor *= detector.scaleFactor
                scaleFactor = scaleFactor.coerceIn(1.0f, 5.0f)
                matrix.setScale(scaleFactor, scaleFactor, ivPhoto.width / 2f, ivPhoto.height / 2f)
                ivPhoto.imageMatrix = matrix
                return true
            }
        })

        btnBack.setOnClickListener { onBackPressed() }

        // Add to favorite
        btnAddFav.setOnClickListener { addToFavorites() }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let { scaleDetector.onTouchEvent(it) }
        return true
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

    private fun addToFavorites() {
        lifecycleScope.launch {
            try {
                val existing = db.favoritePhotoDao().getById(photoId)
                if (existing != null) {
                    Toast.makeText(this@PhotoDetailActivity, "Already in favorites", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this@PhotoDetailActivity, "Added to favorites", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@PhotoDetailActivity, "Failed to add favorite", Toast.LENGTH_SHORT).show()
            }
        }
    }
}