package com.example.lezhinbookmark.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lezhinbookmark.common.LZErrorMessage
import com.example.lezhinbookmark.search.bean.LZDocument
import com.example.lezhinbookmark.search.repository.LZSearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed interface SearchUiState {
    val isLoading: Boolean
    val searchInput: String
    val errorMessages: List<LZErrorMessage?>

    data class NoData(
        override val isLoading: Boolean,
        override val searchInput: String,
        override val errorMessages: List<LZErrorMessage?>
    ): SearchUiState

    data class HasData(
        override val isLoading: Boolean,
        override val searchInput: String,
        override val errorMessages: List<LZErrorMessage?>,
        val images: List<LZDocument?>,
        val favorites: Set<LZDocument?> = emptySet(),
    ): SearchUiState
}

private data class SearchViewModelState(
    val images: List<LZDocument?> = emptyList(),
    val favorites: Set<LZDocument?> = emptySet(),
    val isLoading: Boolean = false,
    val searchInput: String = "",
    val errorMessages: List<LZErrorMessage?> = emptyList()
) {
    fun toUiState(): SearchUiState =
        if (images.isEmpty()) {
            SearchUiState.NoData(
                isLoading = isLoading,
                searchInput = searchInput,
                errorMessages = errorMessages
            )
        } else {
            SearchUiState.HasData(
                images = images,
                isLoading = isLoading,
                searchInput = searchInput,
                favorites = favorites,
                errorMessages = errorMessages
            )
        }
}

class LZSearchViewModel(
    private val searchRepository: LZSearchRepository
): ViewModel() {

    private val viewModelState = MutableStateFlow(
        SearchViewModelState(
            isLoading = false,
            searchInput = ""
        )
    )

    val uiState = viewModelState
        .map(SearchViewModelState::toUiState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())

    init {
        viewModelScope.launch {
            searchRepository.observeFavorites().collect { favorites ->
                viewModelState.update { it.copy(favorites = favorites) }
            }
        }
    }

    fun onUpdateFavorites(keyword: String, document: LZDocument?) {
        viewModelScope.launch {
            if (document != null) searchRepository.updateFavorite(keyword, document)
        }
    }

    fun onSearchKeywordChanged(searchInput: String) {
        viewModelScope.launch {
            viewModelState.update {
                it.copy(searchInput = searchInput)
            }

            if (searchInput.isBlank()) {
                viewModelState.update { it.copy(images = emptyList(), isLoading = false) }
            } else {
                viewModelState.update { it.copy(isLoading = true) }
                val (isSuccess, images, errors) = withContext(Dispatchers.IO) {
                    searchRepository.getSearchImage(searchInput)
                }

                if (isSuccess) {
                    viewModelState.update { it.copy(images = images, isLoading = false) }
                } else {
                    viewModelState.update { it.copy(errorMessages = it.errorMessages + errors, isLoading = false) }
                }
            }
        }
    }

    fun errorShown(errorId: Long) {
        viewModelState.update { currentUiState ->
            val errorMessages = currentUiState.errorMessages.filterNot { it?.id == errorId }
            currentUiState.copy(errorMessages = errorMessages)
        }
    }

    companion object {
        fun provideFactory(
            searchRepository: LZSearchRepository,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LZSearchViewModel(searchRepository) as T
            }
        }
    }
}