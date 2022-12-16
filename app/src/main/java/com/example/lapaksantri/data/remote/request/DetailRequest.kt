package com.example.lapaksantri.data.remote.request


import com.google.gson.annotations.SerializedName

data class DetailRequest(
    @SerializedName("id_cart")
    val idCart: Int,
    @SerializedName("id_product")
    val idProduct: Int,
    @SerializedName("quantity")
    val quantity: Int
)