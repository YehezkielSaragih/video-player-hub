package com.example.videoplayerhub.config

import android.content.Context

object Prefs {
    private const val PREF_NAME = "picsum_prefs"

    const val KEY_TOKEN = "KEY_TOKEN"
    private const val KEY_LAST_PAGE = "KEY_LAST_PAGE"
    private const val KEY_LIMIT = "KEY_LIMIT"
    private const val KEY_LAST_ITEM_COUNT = "KEY_LAST_ITEM_COUNT"

    // Token
    fun saveToken(ctx: Context, token: String) {
        ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit().putString(KEY_TOKEN, token).apply()
    }

    fun getToken(ctx: Context): String? =
        ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_TOKEN, null)

    fun clearToken(ctx: Context) {
        ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .remove(KEY_TOKEN)
            .apply()
    }

    // Limit
    fun saveLimit(ctx: Context, limit: Int) {
        ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit().putInt(KEY_LIMIT, limit).apply()
    }

    fun getLimit(ctx: Context, default: Int = 30): Int =
        ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getInt(KEY_LIMIT, default)

    // Last page
    fun saveLastPage(ctx: Context, page: Int) {
        ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit().putInt(KEY_LAST_PAGE, page).apply()
    }

    fun getLastPage(ctx: Context, default: Int = 1): Int =
        ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getInt(KEY_LAST_PAGE, default)

    // Last item count
    fun saveLastItemCount(ctx: Context, count: Int) {
        ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit().putInt(KEY_LAST_ITEM_COUNT, count).apply()
    }

    fun getLastItemCount(ctx: Context, default: Int = 0): Int =
        ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getInt(KEY_LAST_ITEM_COUNT, default)
}
