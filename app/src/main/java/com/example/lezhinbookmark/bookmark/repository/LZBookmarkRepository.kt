package com.example.lezhinbookmark.bookmark.repository

import com.example.lezhinbookmark.bookmark.bean.LZBookmarkData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * BookmarkRepository
 *
 * @author si.kwak
 */
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