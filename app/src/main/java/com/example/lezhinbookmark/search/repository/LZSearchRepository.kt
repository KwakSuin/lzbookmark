package com.example.lezhinbookmark.search.repository

import com.example.lezhinbookmark.api.ApiResult
import com.example.lezhinbookmark.api.LZApiService
import com.example.lezhinbookmark.api.LZRestClient
import com.example.lezhinbookmark.api.checkApiResponse
import com.example.lezhinbookmark.bookmark.bean.LZBookmarkData
import com.example.lezhinbookmark.common.LZErrorMessage
import com.example.lezhinbookmark.common.LZUtils
import com.example.lezhinbookmark.search.bean.LZDocument
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class LZSearchRepository: LZISearchRepository {
    private val favorites = MutableStateFlow<Set<LZDocument>>(setOf())
    private val bookmarkMap = hashMapOf<String, Set<LZDocument?>>()

    override suspend fun getSearchImage(query: String): Triple<Boolean, List<LZDocument?>, LZErrorMessage?> {
        val restClient = LZRestClient<LZApiService>()

        return when (val result = checkApiResponse { restClient.getClient(LZApiService::class.java).getSearchImage(query = query) }) {
            is ApiResult.OnSuccess -> {
                Triple(first = true, second = result.responseDTO.document, third = null)
            }

            is ApiResult.OnFailure -> {
                Triple(first = false, second = arrayListOf(null), third = result.errorObject)
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
            }
        }
        bookmarkMap[keyword] = favorites.value
        LZUtils.bookmark = LZBookmarkData(bookmarkData = bookmarkMap)
    }
}