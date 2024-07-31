package com.example.lezhinbookmark.bookmark.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.lezhinbookmark.R
import com.example.lezhinbookmark.bookmark.viewmodel.BookmarkUiState
import com.example.lezhinbookmark.bookmark.viewmodel.LZBookmarkViewModel
import com.example.lezhinbookmark.search.ui.DefaultScreen


@Composable
fun LZBookmarkRoute(viewModel: LZBookmarkViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LZBookmarkRoute(
        uiState = uiState,
        onUpdateFavorite = { viewModel.onUpdateFavoritesMap(it) }
    )
}

@Composable
fun LZBookmarkRoute(
    uiState: BookmarkUiState,
    onUpdateFavorite: (List<String>) -> Unit
) {
    LZBookmarkContents {
        when (uiState) {
            is BookmarkUiState.NoData -> {
                DefaultScreen()
            }
            is BookmarkUiState.HasData -> {
                LZBookmarkScreen(
                    bookmarkData = uiState.bookmarkData,
                    onUpdateFavorite = onUpdateFavorite
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LZBookmarkContents(
    contents: @Composable () -> Unit
) {
    Column {
        CenterAlignedTopAppBar(
            title = { Text(text = stringResource(id = R.string.bookmark_tab_name), color = MaterialTheme.colorScheme.background) },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                scrolledContainerColor = MaterialTheme.colorScheme.secondary
            )
        )

        contents()
    }
}