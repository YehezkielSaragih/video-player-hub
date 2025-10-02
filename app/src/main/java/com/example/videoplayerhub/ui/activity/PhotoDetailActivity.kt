package com.example.videoplayerhub.ui.activity

import android.graphics.Matrix
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.bumptech.glide.Glide
import com.example.videoplayerhub.R

class PhotoDetailActivity : ComponentActivity() {

    private lateinit var ivPhoto: ImageView
    private lateinit var tvAuthor: TextView
    private lateinit var tvSize: TextView
    private lateinit var btnBack: Button
    private lateinit var btnAddFav: Button

    private lateinit var scaleDetector: ScaleGestureDetector
    private var scaleFactor = 1f
    private val matrix = Matrix()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photodetail)

        ivPhoto = findViewById(R.id.ivPhotoDetail)
        tvAuthor = findViewById(R.id.tvAuthorDetail)
        tvSize = findViewById(R.id.tvSizeDetail)
        btnBack = findViewById(R.id.btnBack)
        btnAddFav = findViewById(R.id.btnAddFav)

        // Terima data dari Intent
        val id = intent.getStringExtra("PHOTO_ID") ?: ""
        val author = intent.getStringExtra("PHOTO_AUTHOR") ?: "Unknown"
        val width = intent.getIntExtra("PHOTO_WIDTH", 0)
        val height = intent.getIntExtra("PHOTO_HEIGHT", 0)
        val url = intent.getStringExtra("PHOTO_URL") ?: ""
        val downloadUrl = intent.getStringExtra("PHOTO_DOWNLOAD_URL") ?: ""

        bindPhoto(author, width, height, downloadUrl)

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
        btnAddFav.setOnClickListener {
            // TODO: add to favorite logic
        }
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
}