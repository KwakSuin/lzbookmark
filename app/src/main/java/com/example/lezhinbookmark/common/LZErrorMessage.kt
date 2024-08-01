package com.example.lezhinbookmark.common

import androidx.annotation.StringRes

/**
 * Api ErrorMessage Object
 *
 * @property id                     Error Id
 * @property messageId              Error String Resource
 * @property messageString          Error String Message
 */
data class LZErrorMessage(val id: Long, @StringRes val messageId: Int? = null, val messageString: String? = null)