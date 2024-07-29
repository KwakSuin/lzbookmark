package com.example.lezhinbookmark.search.bean

import com.google.gson.annotations.SerializedName

data class LZMeta (
    @SerializedName("total_count") val totalCount: Int?,
    @SerializedName("pageable_count") val pageableCount: Int?,
    @SerializedName("is_end") val isEnd: Boolean?,
)