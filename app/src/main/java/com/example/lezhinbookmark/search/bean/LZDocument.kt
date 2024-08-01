package com.example.lezhinbookmark.search.bean

import com.google.gson.annotations.SerializedName

/**
 * Document Data
 *
 * @property title                  title
 * @property thumbnail              thumbnail
 * @property url                    uri
 * @property datetime               dateTime
 */
data class LZDocument (
    @SerializedName("display_sitename") val title: String?,
    @SerializedName("thumbnail_url") val thumbnail: String?,
    @SerializedName("image_url") val url: String?,
    @SerializedName("datetime") val datetime: String?,
)