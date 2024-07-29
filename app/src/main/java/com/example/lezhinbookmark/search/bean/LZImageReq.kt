package com.example.lezhinbookmark.search.bean

import com.google.gson.annotations.SerializedName

data class LZImageReq (
    @SerializedName ("query") val query: String,
    @SerializedName("sort") val sort: String = "accuracy",
    @SerializedName("page") val page: Int = 1,
    @SerializedName("size") val size: Int = 50
)