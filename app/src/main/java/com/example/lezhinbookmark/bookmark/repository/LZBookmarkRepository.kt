package com.example.lezhinbookmark.bookmark.repository

import com.example.lezhinbookmark.common.LZUtils
import com.example.lezhinbookmark.search.bean.LZDocument
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class LZBookmarkRepository: LZIBookmarkRepository {
    private val bookmarkData = MutableStateFlow<HashMap<String, Set<LZDocument?>>>(hashMapOf())

    override suspend fun onUpdateFavoritesMap(keyword: String, document: Set<LZDocument?>) {
        val bookmarkMap = LZUtils.getBookmarkMap()
        bookmarkMap[keyword] = document

        bookmarkData.update {
            bookmarkMap.apply { remove(keyword, document) }
        }
    }

    override fun observeBookmarkData(): MutableStateFlow<HashMap<String, Set<LZDocument?>>> = bookmarkData
}