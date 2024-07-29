package com.example.lezhinbookmark.common

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

object LZUiUtils {
    /**
     * Ripple Effect 제거한 클릭 메서드
     *
     * @param onClick               onClick
     */
    @SuppressLint("ModifierFactoryUnreferencedReceiver")
    inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier =
        composed {
            clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
                onClick()
            }
        }
}