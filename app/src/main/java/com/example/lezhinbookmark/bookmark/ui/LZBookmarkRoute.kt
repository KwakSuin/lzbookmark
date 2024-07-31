package com.example.lezhinbookmark.bookmark.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.lezhinbookmark.R
import com.example.lezhinbookmark.common.LZUtils
import com.example.lezhinbookmark.search.ui.DefaultScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LZBookmarkRoute() {
    val bookmarkData = LZUtils.getBookmarkMap()

    Column {
        CenterAlignedTopAppBar(
            title = { Text(text = stringResource(id = R.string.bookmark_tab_name), color = MaterialTheme.colorScheme.background) },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                scrolledContainerColor = MaterialTheme.colorScheme.secondary
            )
        )

        when {
            bookmarkData.isEmpty() -> { DefaultScreen() }
            else -> { LZBookmarkScreen() }
        }
    }
}