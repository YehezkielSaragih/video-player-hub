package com.example.videoplayerhub.config

import android.content.Context

object Prefs {
    private const val PREF_NAME = "picsum_prefs"

    const val KEY_TOKEN = "KEY_TOKEN"

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
}
