package com.example.lapaksantri.data.remote.response.order


import com.google.gson.annotations.SerializedName

data class DetailTransactionResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: Int,
    @SerializedName("quantity")
    val quantity: Int
)