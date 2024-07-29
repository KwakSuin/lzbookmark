package com.example.lezhinbookmark.bookmark.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.lezhinbookmark.common.LZUtils
import com.example.lezhinbookmark.search.bean.LZDocument

@Composable
@Preview
fun LZBookmarkScreen() {
    val bookmarkData = LZUtils.getBookmarkMap()

    if (bookmarkData.isEmpty()) {

    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(LZUtils.getBookmarkMap().keys.toList()) { keyword ->
                val bookmarkItem = bookmarkData[keyword]

                Column {
                    ImageBox(imageList = bookmarkItem?.toList() ?: emptyList())
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = keyword)
                }
            }
        }
    }
}

@Composable
fun ImageBox(imageList: List<LZDocument?>) {
    if (imageList.isEmpty()) return

    Box(modifier = Modifier.size(150.dp * 2)) {
        imageList.forEachIndexed { index, image ->
            val alignment = when (index) {
                0 -> Alignment.TopStart
                1 -> Alignment.TopEnd
                2 -> Alignment.BottomStart
                3 -> Alignment.BottomEnd
                else -> Alignment.TopStart // Default alignment (shouldn't be used)
            }

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(image?.url)
                    .build(),
                contentDescription = "groupImage",
                modifier = Modifier.align(alignment),
                contentScale = ContentScale.Crop,
                clipToBounds = true
            )
        }
    }
}