package com.example.lapaksantri.data.remote.response.order


import com.google.gson.annotations.SerializedName

data class GetTransactionsResponse(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("status")
    val status: String
)