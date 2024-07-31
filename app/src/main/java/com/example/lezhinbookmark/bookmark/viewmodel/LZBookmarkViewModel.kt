package com.example.lezhinbookmark.bookmark.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lezhinbookmark.bookmark.bean.LZBookmarkData
import com.example.lezhinbookmark.bookmark.repository.LZBookmarkRepository
import com.example.lezhinbookmark.common.LZUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed interface BookmarkUiState {
    val isLoading: Boolean

    data class NoData(
        override val isLoading: Boolean
    ): BookmarkUiState

    data class HasData(
        override val isLoading: Boolean,
        val bookmark: LZBookmarkData?,
    ): BookmarkUiState
}

private data class BookmarkViewModelState(
    val isLoading: Boolean = false,
    val bookmark: LZBookmarkData?,
) {
    fun toUiState(): BookmarkUiState =
        if (bookmark == null || bookmark.bookmarkData.isEmpty()) {
            BookmarkUiState.NoData(
                isLoading = isLoading
            )
        } else {
            BookmarkUiState.HasData(
                isLoading = isLoading,
                bookmark = bookmark,
            )
        }
}

class LZBookmarkViewModel(
    private val bookmarkRepository: LZBookmarkRepository
): ViewModel() {
    private val viewModelState = MutableStateFlow(
        BookmarkViewModelState(
            isLoading = false,
            bookmark = null,
        )
    )

    val uiState = viewModelState
        .map(BookmarkViewModelState::toUiState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())

    init {
        viewModelScope.launch {
            viewModelState.update { it.copy(bookmark = LZUtils.getBookmarkData()) }
        }
    }

    fun onUpdateFavoritesMap(keyword: List<String>) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.Default) {
                bookmarkRepository.onUpdateFavoritesMap(bookmarkData = viewModelState.value.bookmark, keyword = keyword)
            }

            viewModelState.update {
                it.copy(bookmark = result)
            }
        }
    }

    companion object {
        fun provideFactory(
            searchRepository: LZBookmarkRepository,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LZBookmarkViewModel(searchRepository) as T
            }
        }
    }
}