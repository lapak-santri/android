package com.example.lapaksantri.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    val id: Int,
    val recipient: String,
    val phone: String,
    val detailAddress: String,
    val area: String,
    val district: String,
    val village: String,
    var isMain: Boolean,
) : Parcelable