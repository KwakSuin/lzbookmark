package com.example.lezhinbookmark.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lezhinbookmark.common.LZUtils
import com.example.lezhinbookmark.search.bean.LZDocument
import com.example.lezhinbookmark.search.repository.LZSearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LZSearchViewModel(private val searchRepository: LZSearchRepository): ViewModel() {
    private val _images = MutableStateFlow<List<LZDocument?>>(emptyList())
    val images: StateFlow<List<LZDocument?>> get() = _images

    private val _bookmarkedImages = MutableStateFlow<Map<String, Set<LZDocument?>>>(emptyMap())
    val bookmarkedImages: StateFlow<Map<String, Set<LZDocument?>>> get() = _bookmarkedImages

    private val _isBookmarked = MutableStateFlow<Boolean>(false)
    val isBookmarked: StateFlow<Boolean> get() = _isBookmarked

    suspend fun doSearchImage(query: String) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                searchRepository.getSearchImage(query)
            }

            _images.value = result
        }
    }

    private fun updateBookmarkedImages() {
        val bookmarkMap = LZUtils.getBookmarkMap()
        val updatedMap = bookmarkMap.mapValues { it.value.toSet() }
        _bookmarkedImages.value = updatedMap
    }

    fun toggleBookmark(query: String, image: LZDocument?) {
        LZUtils.toggleBookmark(query, image)
        updateBookmarkedImages()
    }

    fun isBookmarked(query: String, image: LZDocument?): Boolean {
        return LZUtils.isBookmarked(query, image)
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