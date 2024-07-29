package com.example.lezhinbookmark.search.bean

import com.google.gson.annotations.SerializedName

data class LZDocument (
    @SerializedName("display_sitename") val title: String?,
    @SerializedName("thumbnail_url") val thumbnail: String?,
    @SerializedName("image_url") val url: String?,
    @SerializedName("datetime") val datetime: String?,
)