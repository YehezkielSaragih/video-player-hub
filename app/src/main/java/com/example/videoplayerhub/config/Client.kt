package com.example.videoplayerhub.config

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
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

    // Interceptor untuk menambahkan header otomatis
    private val headerInterceptor = Interceptor { chain ->
        val request: Request = chain.request().newBuilder()
            .addHeader("x-api-key", "reqres-free-v1")
            .addHeader("Content-Type", "application/json")
            .build()
        chain.proceed(request)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logger)
        .addInterceptor(headerInterceptor)
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