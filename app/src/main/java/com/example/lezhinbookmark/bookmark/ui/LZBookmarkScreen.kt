package com.example.lezhinbookmark.bookmark.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
            bookmarkData.isEmpty() -> { DefaultScreen()}
            else -> { LZBookmarkScreen() }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LZBookmarkScreen() {
    val bookmarkData = LZUtils.getBookmarkMap()
    val deleteBookmarkData = remember { mutableStateListOf<String>() }

    // 초기화
    deleteBookmarkData.clear()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 10.dp, horizontal = 20.dp)
    ) {
        stickyHeader {
            Button(
                onClick = {
                    deleteBookmarkData.forEach { deleteItem ->
                        LZUtils.getBookmarkMap().remove(deleteItem)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1F51B7),
                    contentColor = Color.White,
                )
            ) {
                Text(text = stringResource(id = R.string.delete))
            }
        }

        items(LZUtils.getBookmarkMap().keys.toList().reversed()) { keyword ->
            val bookmarkItem = bookmarkData[keyword]
            var checked by remember { mutableStateOf(false) }

            Column(modifier = Modifier.fillMaxSize()) {
                Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = checked,
                        onCheckedChange = {
                            checked = it
                            deleteBookmarkData.add(keyword)
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF8BBDFF),
                            uncheckedColor = Color(0xFF8BBDFF)
                        ),
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(text = "$keyword(${bookmarkItem?.size ?: 0})")
                }
                HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
            }
        }
    }
}