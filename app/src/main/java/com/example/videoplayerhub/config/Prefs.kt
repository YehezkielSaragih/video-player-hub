package com.example.videoplayerhub.config

import android.content.Context

object Prefs {
    private const val PREF_NAME = "picsum_prefs"

    const val KEY_TOKEN = "KEY_TOKEN"
    const val KEY_GRID_COLS = "KEY_GRID_COLS"       // 2 or 3
    const val KEY_LAST_PAGE = "KEY_LAST_PAGE"       // Int

    // 🔹 Key baru untuk detail photo
    private const val KEY_PHOTO_ID = "PHOTO_ID"
    private const val KEY_PHOTO_AUTHOR = "PHOTO_AUTHOR"
    private const val KEY_PHOTO_WIDTH = "PHOTO_WIDTH"
    private const val KEY_PHOTO_HEIGHT = "PHOTO_HEIGHT"
    private const val KEY_PHOTO_URL = "PHOTO_URL"

    fun saveToken(ctx: Context, token: String) {
        ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit().putString(KEY_TOKEN, token).apply()
    }
    fun getToken(ctx: Context): String? =
        ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_TOKEN, null)

    fun saveGridCols(ctx: Context, cols: Int) {
        ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit().putInt(KEY_GRID_COLS, cols).apply()
    }
    fun getGridCols(ctx: Context, defaultCols: Int = 3): Int =
        ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getInt(KEY_GRID_COLS, defaultCols)

    fun saveLastPage(ctx: Context, page: Int) {
        ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit().putInt(KEY_LAST_PAGE, page).apply()
    }
    fun getLastPage(ctx: Context, defaultPage: Int = 1): Int =
        ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getInt(KEY_LAST_PAGE, defaultPage)

    // 🔹 Simpan detail photo ke prefs
    fun savePhotoDetail(ctx: Context, id: String, author: String, width: Int, height: Int, url: String) {
        ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_PHOTO_ID, id)
            .putString(KEY_PHOTO_AUTHOR, author)
            .putInt(KEY_PHOTO_WIDTH, width)
            .putInt(KEY_PHOTO_HEIGHT, height)
            .putString(KEY_PHOTO_URL, url)
            .apply()
    }

    // 🔹 Ambil detail photo dari prefs
    fun getPhotoDetail(ctx: Context): Map<String, Any?> {
        val sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return mapOf(
            "id" to sp.getString(KEY_PHOTO_ID, ""),
            "author" to sp.getString(KEY_PHOTO_AUTHOR, "Unknown"),
            "width" to sp.getInt(KEY_PHOTO_WIDTH, 0),
            "height" to sp.getInt(KEY_PHOTO_HEIGHT, 0),
            "url" to sp.getString(KEY_PHOTO_URL, "")
        )
    }
}
