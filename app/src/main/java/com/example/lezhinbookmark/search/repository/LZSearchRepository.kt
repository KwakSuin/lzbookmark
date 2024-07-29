package com.example.lezhinbookmark.search.repository

import android.content.Context
import com.example.lezhinbookmark.api.ApiResult
import com.example.lezhinbookmark.api.LZApiService
import com.example.lezhinbookmark.api.LZRestClient
import com.example.lezhinbookmark.api.checkApiResponse
import com.example.lezhinbookmark.search.bean.LZDocument

class LZSearchRepository(val context: Context): LZISearchRepository {
    override suspend fun getSearchImage(query: String): List<LZDocument?> {
        val restClient = LZRestClient<LZApiService>()

        return when (val result = checkApiResponse { restClient.getClient(LZApiService::class.java).getSearchImage(query = query) }) {
            is ApiResult.OnSuccess -> {
                result.responseDTO.document
            }

            is ApiResult.OnFailure -> {
                arrayListOf(null)
            }
        }
    }
}