package com.example.lezhinbookmark.bookmark.repository

import com.example.lezhinbookmark.bookmark.bean.LZBookmarkData

/**
 * BookmarkRepository Interface
 *
 * @author si.kwak
 */
interface LZIBookmarkRepository {
    suspend fun onUpdateFavoritesMap(bookmarkData: LZBookmarkData?, keyword: List<String>): LZBookmarkData?
}