package com.example.lezhinbookmark.bookmark.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.lezhinbookmark.R
import com.example.lezhinbookmark.bookmark.viewmodel.BookmarkUiState
import com.example.lezhinbookmark.bookmark.viewmodel.LZBookmarkViewModel
import com.example.lezhinbookmark.common.DefaultScreen


/**
 * Bookmark Route
 *
 * @param viewModel                 Bookmark ViewModel
 * @param snackbarHostState         SnackbarHostState
 */
@Composable
fun LZBookmarkRoute(
    viewModel: LZBookmarkViewModel,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LZBookmarkRoute(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onErrorDismiss = { viewModel.errorShown(it) },
        onUpdateFavorite = { viewModel.onUpdateFavoritesMap(it) }
    )
}

/**
 * Bookmark Route
 *
 * @param uiState                   Bookmark UiState
 * @param snackbarHostState         SnackbarHostState
 * @param onErrorDismiss            onErrorDismiss
 * @param onUpdateFavorite          onUpdateFavorite
 */
@Composable
fun LZBookmarkRoute(
    uiState: BookmarkUiState,
    snackbarHostState: SnackbarHostState,
    onErrorDismiss: (Long) -> Unit,
    onUpdateFavorite: (List<String>) -> Unit
) {
    LZBookmarkContents {
        when (uiState) {
            is BookmarkUiState.NoData -> {
                DefaultScreen()
            }
            is BookmarkUiState.HasData -> {
                LZBookmarkScreen(
                    bookmarkData = uiState.bookmark,
                    onUpdateFavorite = onUpdateFavorite
                )
            }
        }
    }


    if (uiState.errorMessages.isNotEmpty()) {
        val errorMessage = remember(uiState) { uiState.errorMessages[0] }
        val errorMessageText = when {
            errorMessage.messageId != null -> stringResource(errorMessage.messageId)
            errorMessage.messageString != null -> errorMessage.messageString
            else -> stringResource(R.string.error_default)
        }
        val onErrorDismissState by rememberUpdatedState(onErrorDismiss)

        LaunchedEffect(errorMessageText, snackbarHostState) {
            snackbarHostState.showSnackbar(
                message = errorMessageText,
            )
            onErrorDismissState(errorMessage.id)
        }
    }
}

/**
 * Bookmark Common Contents
 *
 * @param contents                  Composable Contents
 */
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