package com.example.lezhinbookmark.search.repository

import com.example.lezhinbookmark.search.bean.LZDocument

interface LZISearchRepository {
    suspend fun getSearchImage(query: String): List<LZDocument?>
}