package com.example.lezhinbookmark.api

import com.example.lezhinbookmark.search.bean.LZImageResp
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LZApiService {
    @GET("image")
    suspend fun getSearchImage(
        @Query("query") query: String,
        @Query("sort") sort: String = "accuracy",
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 50
    ): Response<LZImageResp?>
}