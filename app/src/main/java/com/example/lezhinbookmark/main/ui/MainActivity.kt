package com.example.lezhinbookmark.main.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.lezhinbookmark.ui.theme.LezhinBookmarkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LezhinBookmarkTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    LZMain()
                }
            }
        }
    }
}