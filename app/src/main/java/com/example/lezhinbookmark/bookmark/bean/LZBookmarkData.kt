package com.example.lezhinbookmark.bookmark.bean

import androidx.annotation.Keep
import com.example.lezhinbookmark.search.bean.LZDocument
import com.google.gson.annotations.SerializedName

/**
 * Bookmark Data
 *
 * @property bookmarkData           <Keyword, Document>
 */
@Keep
data class LZBookmarkData (
    @SerializedName("bookmarkData") val bookmarkData: HashMap<String, Set<LZDocument?>>
)