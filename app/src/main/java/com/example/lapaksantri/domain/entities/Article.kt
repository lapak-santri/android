package com.example.lapaksantri.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    val id: Int,
    val publishedAt: String,
    val description: String,
    val title: String,
    val imagePath: String
) : Parcelable
