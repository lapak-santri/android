package com.example.lapaksantri.data.remote.response.product


import com.google.gson.annotations.SerializedName

data class GetProductsResponse(
    @SerializedName("data")
    val dataProductResponse: DataProductResponse,
    @SerializedName("status")
    val status: String
)