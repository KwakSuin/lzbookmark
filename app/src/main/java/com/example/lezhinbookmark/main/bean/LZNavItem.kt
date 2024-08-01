package com.example.lezhinbookmark.main.bean

import androidx.annotation.Keep

/**
 * Navigation Item
 *
 * @property screenType             Screen Type
 * @property screenId               Screen Id
 */
@Keep
data class LZNavItem(
    val screenType: String,
    val screenId: String
)