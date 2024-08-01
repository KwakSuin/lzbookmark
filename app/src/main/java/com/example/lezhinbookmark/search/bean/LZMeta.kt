package com.example.lezhinbookmark.search.bean

import com.google.gson.annotations.SerializedName

/**
 * Meta Response
 *
 * @property totalCount             totalCount
 * @property pageableCount          pageableCount
 * @property isEnd                  isEnd
 */
data class LZMeta (
    @SerializedName("total_count") val totalCount: Int?,
    @SerializedName("pageable_count") val pageableCount: Int?,
    @SerializedName("is_end") val isEnd: Boolean?,
)