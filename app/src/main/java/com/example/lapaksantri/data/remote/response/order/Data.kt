package com.example.lapaksantri.data.remote.response.order


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("count")
    val count: Int,
    @SerializedName("data")
    val `data`: List<TransactionResponse>
)