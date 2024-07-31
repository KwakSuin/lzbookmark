package com.example.lezhinbookmark.common

import com.example.lezhinbookmark.search.bean.LZDocument

object LZUtils {
    val bookmarkMaps = HashMap<String, Set<LZDocument?>>()

    fun getBookmarkMap(): HashMap<String, Set<LZDocument?>> {
        return bookmarkMaps
    }
}