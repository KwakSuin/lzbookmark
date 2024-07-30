package com.example.lezhinbookmark.search

class LZSearchManager {
    companion object {
        private var instance: LZSearchManager? = null

        fun getInstance(): LZSearchManager {
            return instance ?: synchronized(this) {
                instance ?: LZSearchManager().also {
                    instance = it
                }
            }
        }
    }

}