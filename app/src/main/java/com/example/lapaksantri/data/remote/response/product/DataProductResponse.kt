package com.example.lapaksantri.data.remote.response.product


import com.google.gson.annotations.SerializedName

data class DataProductResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("data")
    val `data`: List<ProductResponse>
)