package com.example.lezhinbookmark.common

import com.example.lezhinbookmark.search.bean.LZDocument

object LZUtils {
    private val bookmarkMap = HashMap<String, MutableSet<LZDocument?>>()

    fun getBookmarkMap(): HashMap<String, MutableSet<LZDocument?>> {
        return bookmarkMap
    }

    fun toggleBookmark(query: String, image: LZDocument?) {
        if (image == null) return

        val currentBookmarks = bookmarkMap[query]?.toMutableSet() ?: mutableSetOf()
        if (currentBookmarks.contains(image)) {
            currentBookmarks.remove(image)
            if (currentBookmarks.isEmpty()) {
                bookmarkMap.remove(query)
            } else {
                bookmarkMap[query] = currentBookmarks
            }
        } else {
            currentBookmarks.add(image)
            bookmarkMap[query] = currentBookmarks
        }
    }

    fun isBookmarked(query: String, image: LZDocument?): Boolean {
        return bookmarkMap[query]?.contains(image) ?: false
    }
}