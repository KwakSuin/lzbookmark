package com.example.lezhinbookmark.common

import com.example.lezhinbookmark.bookmark.bean.LZBookmarkData
import com.example.lezhinbookmark.search.bean.LZDocument

object LZUtils {
    var bookmark = LZBookmarkData(hashMapOf())

    fun getBookmarkData(): LZBookmarkData {
        return bookmark
    }
}