package com.example.lapaksantri.data.remote.request


import com.google.gson.annotations.SerializedName

data class AddCartRequest(
    @SerializedName("id_product")
    val idProduct: Int,
    @SerializedName("quantity")
    val quantity: Int
)