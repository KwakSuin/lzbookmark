package com.example.lezhinbookmark.search.bean

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * Image Response Object
 *
 * @property meta                   meta
 * @property document               document
 */
@Keep
data class LZImageResp (
    @SerializedName("meta") val meta: LZMeta?,
    @SerializedName("documents") val document: List<LZDocument?>
)