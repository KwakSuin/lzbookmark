package com.example.lezhinbookmark.search.bean

import com.google.gson.annotations.SerializedName

data class LZImageResp (
    @SerializedName("meta") val meta: LZMeta?,
    @SerializedName("documents") val document: List<LZDocument?>
)