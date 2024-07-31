package com.example.lezhinbookmark.bookmark.repository

import com.example.lezhinbookmark.bookmark.bean.LZBookmarkData
import com.example.lezhinbookmark.search.bean.LZDocument
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface LZIBookmarkRepository {
    suspend fun onUpdateFavoritesMap(bookmarkData: LZBookmarkData?, keyword: List<String>): LZBookmarkData?
}