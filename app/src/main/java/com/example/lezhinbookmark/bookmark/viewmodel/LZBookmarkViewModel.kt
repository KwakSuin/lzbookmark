package com.example.lezhinbookmark.bookmark.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lezhinbookmark.bookmark.repository.LZBookmarkRepository

class LZBookmarkViewModel(val bookmarkRepository: LZBookmarkRepository): ViewModel() {

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