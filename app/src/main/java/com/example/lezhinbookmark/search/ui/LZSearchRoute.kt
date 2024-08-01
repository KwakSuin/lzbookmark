package com.example.lezhinbookmark.search.ui

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.lezhinbookmark.R
import com.example.lezhinbookmark.search.bean.LZDocument
import com.example.lezhinbookmark.search.viewmodel.LZSearchViewModel
import com.example.lezhinbookmark.search.viewmodel.SearchUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun LZSearchRoute(
    viewModel: LZSearchViewModel,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LZSearchRoute(
        uiState = uiState,
        snackbarHostState =  snackbarHostState,
        onUpdateFavorite = { keyword, document -> viewModel.onUpdateFavorites(keyword, document) },
        onUpdateSearchInput = { viewModel.onSearchKeywordChanged(it) },
        onErrorDismiss = { viewModel.errorShown(it) }
    )
}

@Composable
fun LZSearchRoute(
    uiState: SearchUiState,
    snackbarHostState: SnackbarHostState,
    onUpdateFavorite: (String, LZDocument?) -> Unit,
    onUpdateSearchInput: (String) -> Unit,
    onErrorDismiss: (Long) -> Unit,
) {
    LZSearchContents(
        onUpdateSearchInput = onUpdateSearchInput
    ) {
        when (uiState) {
            is SearchUiState.NoData -> {
                DefaultScreen()
            }
            is SearchUiState.HasData -> {
                LZSearchScreen(
                    images = uiState.images,
                    favorites = uiState.favorites,
                    searchKeyword = uiState.searchInput,
                    onUpdateFavorite = onUpdateFavorite,
                )
            }
        }
    }

    if (uiState.errorMessages.isNotEmpty()) {
        val errorMessage = remember(uiState) { uiState.errorMessages[0] }
        val errorMessageText = when {
            errorMessage?.messageId != null -> stringResource(errorMessage.messageId)
            errorMessage?.messageString != null -> errorMessage.messageString
            else -> stringResource(R.string.error_default)
        }
        val onErrorDismissState by rememberUpdatedState(onErrorDismiss)

        LaunchedEffect(errorMessageText, snackbarHostState) {
            snackbarHostState.showSnackbar(
                message = errorMessageText,
            )
            onErrorDismissState(errorMessage?.id ?: 0)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LZSearchContents(
    onUpdateSearchInput: (String) -> Unit,
    contents: @Composable (String) -> Unit
) {
    var searchKeyword by rememberSaveable { mutableStateOf("") }

    Column {
        CenterAlignedTopAppBar(
            title = { Text(text = stringResource(id = R.string.search_tab_name), color = MaterialTheme.colorScheme.background) },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                scrolledContainerColor = MaterialTheme.colorScheme.secondary
            )
        )

        SearchBar(
            searchQuery = searchKeyword,
            onQueryChanged = {
                    newText -> searchKeyword = newText

                CoroutineScope(Dispatchers.Default).launch {
                    delay(1000L)
                }
                onUpdateSearchInput(newText)
            }
        )

        contents(searchKeyword)
    }
}