package com.example.lapaksantri.data.remote.response.cart


import com.google.gson.annotations.SerializedName

data class AddUpdateCartResponse(
    @SerializedName("data")
    val `data`: List<DataAddUpdateCartResponse>,
    @SerializedName("status")
    val status: String
)