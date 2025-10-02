package com.example.videoplayerhub.config

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.videoplayerhub.service.AuthService
import com.example.videoplayerhub.service.AppService

object Client {

    private const val AUTH_URL = "https://reqres.in/"
    private const val APP_URL = "https://picsum.photos/"

    private val logger = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logger)
        .build()

    // Auth API
    val authApi: AuthService by lazy {
        Retrofit.Builder()
            .baseUrl(AUTH_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthService::class.java)
    }

    // Photo API
    val appApi: AppService by lazy {
        Retrofit.Builder()
            .baseUrl(APP_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AppService::class.java)
    }
}