package com.example.lezhinbookmark.bookmark.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lezhinbookmark.bookmark.repository.LZBookmarkRepository
import com.example.lezhinbookmark.common.LZUtils
import com.example.lezhinbookmark.search.bean.LZDocument
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface BookmarkUiState {
    val isLoading: Boolean

    data class NoData(
        override val isLoading: Boolean
    ): BookmarkUiState

    data class HasData(
        override val isLoading: Boolean,
        val bookmarkData: HashMap<String, Set<LZDocument?>> = hashMapOf(),
    ): BookmarkUiState
}

private data class BookmarkViewModelState(
    val isLoading: Boolean = false,
    val bookmarkData: HashMap<String, Set<LZDocument?>> = hashMapOf(),
) {
    fun toUiState(): BookmarkUiState =
        if (LZUtils.getBookmarkMap().isEmpty()) {
            BookmarkUiState.NoData(
                isLoading = isLoading
            )
        } else {
            BookmarkUiState.HasData(
                isLoading = isLoading,
                bookmarkData = bookmarkData,
            )
        }
}

class LZBookmarkViewModel(
    private val bookmarkRepository: LZBookmarkRepository
): ViewModel() {
    private val viewModelState = MutableStateFlow(
        BookmarkViewModelState(
            isLoading = false,
            bookmarkData = hashMapOf(),
        )
    )

    val uiState = viewModelState
        .map(BookmarkViewModelState::toUiState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())

    init {
        val initialData = LZUtils.getBookmarkMap()
        viewModelState.update { it.copy(bookmarkData = initialData) }

        viewModelScope.launch {
            bookmarkRepository.observeBookmarkData().collect { favorites ->
                viewModelState.update { it.copy(bookmarkData = favorites) }
            }
        }
    }

    fun onUpdateFavoritesMap(keyword: String) {
        viewModelScope.launch {
            bookmarkRepository.onUpdateFavoritesMap(keyword = keyword)
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