package com.example.videoplayerhub.config

import android.content.Context

object Prefs {
    private const val PREF_NAME = "picsum_prefs"
    const val KEY_TOKEN = "KEY_TOKEN"
    const val KEY_GRID_COLS = "KEY_GRID_COLS"       // 2 or 3
    const val KEY_LAST_PAGE = "KEY_LAST_PAGE"       // Int

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
}
