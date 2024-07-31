package com.example.lezhinbookmark.bookmark.repository

import com.example.lezhinbookmark.search.bean.LZDocument

class LZBookmarkRepository: LZIBookmarkRepository {
    override suspend fun onUpdateFavoritesMap(bookmarkData: HashMap<String, Set<LZDocument?>>, keyword: List<String>): HashMap<String, Set<LZDocument?>> {
        return bookmarkData.apply {
            keyword.forEach { key ->
                remove(key)
            }
        }
    }
}