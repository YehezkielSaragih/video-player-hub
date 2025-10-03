package com.example.videoplayerhub.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.videoplayerhub.R
import com.example.videoplayerhub.config.Prefs
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

        // default fragment pertama (Home)
        if (savedInstanceState == null) {
            toolbar.title = getString(R.string.title_home)
            replaceFragment(PhotoGridFragment(), "HOME")
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navHome -> {
                    toolbar.title = getString(R.string.title_home)
                    replaceFragment(PhotoGridFragment(), "HOME")
                    true
                }
                R.id.navFavorite -> {
                    toolbar.title = getString(R.string.title_favorite)
                    replaceFragment(FavoriteFragment(), "FAVORITE")
                    true
                }
                R.id.navLogout -> {
//                    val intent = Intent(this, LoginActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                    true
                    showLogoutDialog()
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment, tag: String) {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)

        // Jika fragment tujuan sama dengan yang sedang tampil → jangan replace
        if (currentFragment != null && currentFragment::class == fragment::class) {
            return
        }

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
            .replace(R.id.fragmentContainer, fragment, tag)
            .commit()
    }

    private fun showLogoutDialog() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.title_logout))
        builder.setMessage("Are you sure you want to logout?")

        builder.setPositiveButton("Yes") { dialog, _ ->
            // Aksi logout
            Prefs.clearToken(this)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

}