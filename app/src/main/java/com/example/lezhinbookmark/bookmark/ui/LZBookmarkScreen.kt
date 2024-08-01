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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.lezhinbookmark.R
import com.example.lezhinbookmark.bookmark.bean.LZBookmarkData
import com.example.lezhinbookmark.common.DefaultScreen

/**
 * Bookmark Screen
 *
 * @param bookmarkData              <Keyword, Document> Data
 * @param onUpdateFavorite          onUpdated Favorite
 *
 * @author si.kwak
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LZBookmarkScreen(
    bookmarkData: LZBookmarkData?,
    onUpdateFavorite: (List<String>) -> Unit
) {
    val deleteBookmarkData = rememberSaveable { mutableListOf("") }
    deleteBookmarkData.clear()

    if (bookmarkData == null || bookmarkData.bookmarkData.isEmpty()) {
        DefaultScreen()
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 10.dp, horizontal = 20.dp)
        ) {
            // 삭제 버튼 상단 고정
            stickyHeader {
                Button(
                    onClick = { onUpdateFavorite(deleteBookmarkData) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1F51B7),
                        contentColor = Color.White,
                    )
                ) {
                    Text(text = stringResource(id = R.string.delete))
                }
            }

            // 북마크 목록
            items(bookmarkData.bookmarkData.keys.reversed()) { keyword ->
                var checked by rememberSaveable { mutableStateOf(false) }

                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = checked,
                            onCheckedChange = {
                                checked = it
                                deleteBookmarkData.add(keyword)
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = MaterialTheme.colorScheme.secondary,
                                uncheckedColor = MaterialTheme.colorScheme.secondary
                            ),
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(text = "$keyword(${bookmarkData.bookmarkData[keyword]?.size ?: 0})")
                    }
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.surfaceContainer
                    )
                }
            }
        }
    }
}