package com.example.videoplayerhub.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.videoplayerhub.R
import com.example.videoplayerhub.config.Prefs
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val token = Prefs.getToken(applicationContext)

        if (!token.isNullOrEmpty()) {
            // Token ada → langsung buka MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            // Token tidak ada → tampilkan animasi dulu
            val logo = findViewById<ImageView>(R.id.splashLogo)

            // Animasi fade-in
            val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
            logo.startAnimation(fadeIn)

            // Delay 3 detik → pindah ke LoginActivity
            lifecycleScope.launch {
                delay(3000)
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
            }
        }
    }
}
