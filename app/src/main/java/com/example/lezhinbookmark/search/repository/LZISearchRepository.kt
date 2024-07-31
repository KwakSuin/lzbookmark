package com.example.lezhinbookmark.search.repository

import com.example.lezhinbookmark.search.bean.LZDocument
import kotlinx.coroutines.flow.Flow

interface LZISearchRepository {
    fun observeFavorites(): Flow<Set<LZDocument>>

    suspend fun updateFavorite(keyword: String, document: LZDocument)

    suspend fun getSearchImage(query: String): List<LZDocument?>
}