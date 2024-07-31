package com.example.lezhinbookmark.common

import com.example.lezhinbookmark.bookmark.bean.LZBookmarkData
import com.example.lezhinbookmark.search.bean.LZDocument

object LZUtils {
    val bookmarkMaps = HashMap<String, Set<LZDocument?>>()
    var bookmark = LZBookmarkData(hashMapOf())

    fun getBookmarkMap(): HashMap<String, Set<LZDocument?>> {
        return bookmarkMaps
    }

    fun getBookmarkData(): LZBookmarkData {
        return bookmark
    }
}