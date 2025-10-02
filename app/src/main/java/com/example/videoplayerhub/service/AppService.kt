package com.example.videoplayerhub.service

import com.example.videoplayerhub.model.PicsumPhoto
import retrofit2.http.GET
import retrofit2.http.Query

interface AppService {
    @GET("v2/list?page={n}&limit={m}")
    suspend fun getList(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): List<PicsumPhoto>
}

// Foto: GET https://picsum.photos/v2/list?page={n}&limit={m}
// thumbnail bisa pakai https://picsum.photos/id/{id}/200/200, full pakai download_url