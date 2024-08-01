package com.example.lezhinbookmark.bookmark.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lezhinbookmark.bookmark.bean.LZBookmarkData
import com.example.lezhinbookmark.bookmark.repository.LZBookmarkRepository
import com.example.lezhinbookmark.common.LZErrorMessage
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
    val errorMessages: List<LZErrorMessage>

    data class NoData(
        override val isLoading: Boolean,
        override val errorMessages: List<LZErrorMessage>
    ): BookmarkUiState

    data class HasData(
        override val isLoading: Boolean,
        override val errorMessages: List<LZErrorMessage>,
        val bookmark: LZBookmarkData?,
    ): BookmarkUiState
}

private data class BookmarkViewModelState(
    val isLoading: Boolean = false,
    val errorMessages: List<LZErrorMessage> = emptyList(),
    val bookmark: LZBookmarkData?,
) {
    fun toUiState(): BookmarkUiState =
        if (bookmark == null || bookmark.bookmarkData.isEmpty()) {
            BookmarkUiState.NoData(
                isLoading = isLoading,
                errorMessages = errorMessages,
            )
        } else {
            BookmarkUiState.HasData(
                isLoading = isLoading,
                errorMessages = errorMessages,
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

    fun errorShown(errorId: Long) {
        viewModelState.update { currentUiState ->
            val errorMessages = currentUiState.errorMessages.filterNot { it.id == errorId }
            currentUiState.copy(errorMessages = errorMessages)
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