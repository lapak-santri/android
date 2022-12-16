package com.example.lapaksantri.data.remote.response.address


import com.google.gson.annotations.SerializedName

data class AddAddressResponse(
    @SerializedName("data")
    val `data`: AddressResponse,
    @SerializedName("status")
    val status: String
)