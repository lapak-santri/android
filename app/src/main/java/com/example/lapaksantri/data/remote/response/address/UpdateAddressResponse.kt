package com.example.lapaksantri.data.remote.response.address


import com.google.gson.annotations.SerializedName

data class UpdateAddressResponse(
    @SerializedName("data")
    val `data`: List<AddressResponse>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)