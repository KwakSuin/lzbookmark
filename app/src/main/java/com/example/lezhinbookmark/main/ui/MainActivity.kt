package com.example.lezhinbookmark.main.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.example.lezhinbookmark.ui.theme.LezhinBookmarkTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // 디바 이스 가로 너비
            val widthSizeClass = calculateWindowSizeClass(activity = this@MainActivity).widthSizeClass
            LezhinBookmarkTheme {
                LZMain()
            }
        }
    }
}