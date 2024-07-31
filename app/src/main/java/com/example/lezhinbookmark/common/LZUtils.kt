package com.example.lezhinbookmark.common

import com.example.lezhinbookmark.search.bean.LZDocument

object LZUtils {
    private val bookmarkMap = HashMap<String, Set<LZDocument?>>()

    fun getBookmarkMap(): HashMap<String, Set<LZDocument?>> {
        return bookmarkMap
    }
}