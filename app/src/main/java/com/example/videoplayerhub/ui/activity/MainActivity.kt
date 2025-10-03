package com.example.videoplayerhub.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.videoplayerhub.R
import com.example.videoplayerhub.ui.fragment.FavoriteFragment
import com.example.videoplayerhub.ui.fragment.PhotoGridFragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: MaterialToolbar
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar_main)
        bottomNav = findViewById(R.id.bottomNav)

        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            replaceFragment(PhotoGridFragment())
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navHome -> {
                    replaceFragment(PhotoGridFragment())
                    true
                }
                R.id.navFavorite -> {
                    replaceFragment(FavoriteFragment())
                    true
                }
                R.id.navLogout -> {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}