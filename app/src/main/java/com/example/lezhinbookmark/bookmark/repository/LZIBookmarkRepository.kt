package com.example.lezhinbookmark.bookmark.repository

import com.example.lezhinbookmark.search.bean.LZDocument
import kotlinx.coroutines.flow.MutableStateFlow

interface LZIBookmarkRepository {
    suspend fun onUpdateFavoritesMap(bookmarkData: HashMap<String, Set<LZDocument?>>, keyword: List<String>): HashMap<String, Set<LZDocument?>>
}