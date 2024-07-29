package com.example.lezhinbookmark.main.common

import com.example.lezhinbookmark.main.bean.LZNavItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object NavSingleton {
    private val _navSharedFlow = MutableSharedFlow<LZNavItem>(extraBufferCapacity = 1)
    val navSharedFlow = _navSharedFlow.asSharedFlow()
    fun navigate(destinationScreenId: String, destinationScreenType: String) {
        _navSharedFlow.tryEmit(LZNavItem(destinationScreenType, destinationScreenId))
    }
}