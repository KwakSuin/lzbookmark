package com.example.lezhinbookmark.common

import com.example.lezhinbookmark.bookmark.bean.LZBookmarkData

/**
 * Utils
 */
object LZUtils {
    var bookmark = LZBookmarkData(hashMapOf())

    fun getBookmarkData(): LZBookmarkData {
        return bookmark
    }
}