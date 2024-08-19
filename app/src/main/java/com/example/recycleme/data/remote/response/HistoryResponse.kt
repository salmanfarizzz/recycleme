package com.example.recycleme.data.remote.response

data class HistoryResponse(
    val category: String,
    val confidence: String,
    val error: Boolean,
    val message: String,
    val storage_url: String,
    val timestamp: String
)
