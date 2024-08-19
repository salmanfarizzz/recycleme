package com.example.recycleme.data.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PredictionResponse(
    val category: String,
    val confidence: String,
    val error: Boolean,
    val message: String,
    val storage_url: String
): Parcelable
