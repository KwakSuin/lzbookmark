package com.example.lezhinbookmark.bookmark.repository

import com.example.lezhinbookmark.search.bean.LZDocument
import kotlinx.coroutines.flow.MutableStateFlow

interface LZIBookmarkRepository {
    fun observeBookmarkData(): MutableStateFlow<HashMap<String, Set<LZDocument?>>>

    suspend fun onUpdateFavoritesMap(keyword: String, document: Set<LZDocument?>)
}