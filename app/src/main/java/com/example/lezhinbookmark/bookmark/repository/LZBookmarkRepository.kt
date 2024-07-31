package com.example.lezhinbookmark.bookmark.repository

import com.example.lezhinbookmark.common.LZUtils
import com.example.lezhinbookmark.search.bean.LZDocument
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class LZBookmarkRepository: LZIBookmarkRepository {
    private val bookmarkData = MutableStateFlow<HashMap<String, Set<LZDocument?>>>(hashMapOf())

    override suspend fun onUpdateFavoritesMap(keyword: String) {
        val bookmarkMap = LZUtils.getBookmarkMap()

        bookmarkData.update {
            bookmarkMap.apply { remove(keyword) }
        }
    }

    override fun observeBookmarkData(): MutableStateFlow<HashMap<String, Set<LZDocument?>>> = bookmarkData
}