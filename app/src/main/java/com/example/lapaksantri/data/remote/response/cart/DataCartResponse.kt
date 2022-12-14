package com.example.lapaksantri.data.remote.response.cart


import com.google.gson.annotations.SerializedName

data class DataCartResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("data")
    val `data`: List<CartResponse>
)