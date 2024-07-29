package com.example.lezhinbookmark.main.bean

import android.os.Parcelable
import androidx.annotation.Keep

@Keep
data class LZNavItem(
    val screenType: String,                 // Screen UiType
    val screenId: String                    // Screen Id
)