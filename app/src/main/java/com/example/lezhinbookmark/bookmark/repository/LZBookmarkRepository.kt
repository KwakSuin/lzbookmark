package com.example.lezhinbookmark.bookmark.repository

import com.example.lezhinbookmark.bookmark.bean.LZBookmarkData
import com.example.lezhinbookmark.search.bean.LZDocument
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class LZBookmarkRepository: LZIBookmarkRepository {
    override suspend fun onUpdateFavoritesMap(bookmarkData: LZBookmarkData?, keyword: List<String>): LZBookmarkData? {
        if (bookmarkData != null) {
            withContext(Dispatchers.Default) {
                bookmarkData.bookmarkData.toMutableMap().apply {
                    keyword.forEach { key ->
                        remove(key)
                    }
                }
            }
        }

        return bookmarkData
    }
}