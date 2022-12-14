package com.example.lapaksantri.data.remote.response.order


import com.google.gson.annotations.SerializedName

data class AddCartsResponse(
    @SerializedName("status")
    val status: String
)