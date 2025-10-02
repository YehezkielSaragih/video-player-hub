package com.example.videoplayerhub.service

import com.example.videoplayerhub.dto.LoginRequest
import com.example.videoplayerhub.dto.LoginResponse
import retrofit2.http.POST
import retrofit2.http.Body

interface AuthService {
    @POST("api/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}

// POST https://reqres.in/api/login
