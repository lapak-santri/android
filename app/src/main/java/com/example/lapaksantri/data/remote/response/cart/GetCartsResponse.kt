package com.example.lapaksantri.data.remote.response.cart


import com.google.gson.annotations.SerializedName

data class GetCartsResponse(
    @SerializedName("data")
    val dataCartResponse: DataCartResponse,
    @SerializedName("status")
    val status: String
)