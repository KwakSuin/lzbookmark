package com.example.lezhinbookmark.search.ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.lezhinbookmark.R
import com.example.lezhinbookmark.common.LZUiUtils.noRippleClickable
import com.example.lezhinbookmark.search.bean.LZDocument
import com.example.lezhinbookmark.search.viewmodel.LZSearchViewModel
import com.example.lezhinbookmark.search.viewmodel.SearchUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LZSearchRoute(viewModel: LZSearchViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LZSearchRoute(
        uiState = uiState,
        onUpdateFavorite = { keyword, document -> viewModel.onUpdateFavorites(keyword, document) },
        onUpdateSearchInput = { viewModel.onSearchKeywordChanged(it) }
    )
}

@Composable
fun LZSearchRoute(
    uiState: SearchUiState,
    onUpdateFavorite: (String, LZDocument?) -> Unit,
    onUpdateSearchInput: (String) -> Unit,
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
                    onUpdateFavorite = onUpdateFavorite
                )
            }
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

@Composable
fun LZSearchScreen(
    images: List<LZDocument?>,
    favorites: Set<LZDocument?>,
    searchKeyword: String,
    onUpdateFavorite: (String, LZDocument?) -> Unit,
) {
    val configuration = LocalConfiguration.current
    val halfScreenWidth = configuration.screenWidthDp.dp / 2

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .drawBehind { drawRect(color = Color.White) }
    ) {
        if (images.isEmpty()) {
            DefaultScreen()
        } else {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 5.dp,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                items(images.filter { it != null && !it.url.isNullOrBlank() }) { image ->
                    ImageItem(
                        image = image,
                        halfScreenWidth = halfScreenWidth,
                        isBookmarked = favorites.contains(image),
                        onUpdateFavorite = { onUpdateFavorite.invoke(searchKeyword, it) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    searchQuery: String,
    onQueryChanged: (String) -> Unit
) {
    BasicTextField(
        value = searchQuery,
        onValueChange = onQueryChanged,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .drawBehind {
                drawRoundRect(
                    color = Color.LightGray,
                    cornerRadius = CornerRadius(x = 10f, y = 40f)
                )
            },
        decorationBox = { textField ->
            OutlinedTextFieldDefaults.DecorationBox(
                value = searchQuery,
                innerTextField = textField,
                enabled = true,
                singleLine = true,
                placeholder = { if (searchQuery.isBlank()) Text(text = "Search") },
                visualTransformation = VisualTransformation.None,
                interactionSource = remember { MutableInteractionSource() },
                contentPadding = PaddingValues(
                    horizontal = 10.dp,
                    vertical = 8.dp,
                ),
            )
        }
    )
}

@Composable
fun ImageItem(
    image: LZDocument?,
    halfScreenWidth: Dp,
    isBookmarked: Boolean,
    onUpdateFavorite: (LZDocument?) -> Unit,
) {
    var isErrorImage by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .width(halfScreenWidth)
            .padding(5.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(image?.url)
                .build(),
            contentDescription = "searchImage",
            contentScale = ContentScale.Crop,
            modifier = Modifier.width(halfScreenWidth),
            onError = {
                isErrorImage = false
                Modifier.size(0.dp)
            }
        )

        if (!isErrorImage) {
            Icon(
                imageVector = if (isBookmarked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "bookmark",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .noRippleClickable { onUpdateFavorite(image) },
                tint = if (isBookmarked) Color.Red else Color.White
            )
        }
    }
}

@Composable
fun DefaultScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(imageVector = Icons.Default.Clear, contentDescription = "no data")
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "No Data")
    }
}