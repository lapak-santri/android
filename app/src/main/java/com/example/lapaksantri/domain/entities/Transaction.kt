package com.example.lapaksantri.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Transaction(
    val id: Int,
    val priceTotal: Int,
    val name: String,
    val invoice: String,
    val districtName: String,
    val address: String,
    val midtransUrl: String = "",
    val status: String = "",
    val createdAt: String,
    val sendAt: String,
    val carts: @RawValue List<Cart>? = null
) : Parcelable
