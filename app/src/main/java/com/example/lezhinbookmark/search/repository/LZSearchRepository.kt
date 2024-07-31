package com.example.lezhinbookmark.search.repository

import com.example.lezhinbookmark.api.ApiResult
import com.example.lezhinbookmark.api.LZApiService
import com.example.lezhinbookmark.api.LZRestClient
import com.example.lezhinbookmark.api.checkApiResponse
import com.example.lezhinbookmark.common.LZUtils
import com.example.lezhinbookmark.search.bean.LZDocument
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class LZSearchRepository: LZISearchRepository {
    private val favorites = MutableStateFlow<Set<LZDocument>>(setOf())

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

    override fun observeFavorites(): Flow<Set<LZDocument>> = favorites

    override suspend fun updateFavorite(keyword: String, document: LZDocument) {
        favorites.update {
            it.toMutableSet().apply {
                if (contains(document)) {
                    remove(document)
                } else {
                    add(document)
                }

                LZUtils.bookmarkMaps.apply {
                    put(keyword, it)
                }
            }
        }
    }
}