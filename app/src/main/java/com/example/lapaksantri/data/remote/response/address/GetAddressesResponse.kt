package com.example.lapaksantri.data.remote.response.address


import com.google.gson.annotations.SerializedName

data class GetAddressesResponse(
    @SerializedName("data")
    val dataAddressResponse: DataAddressResponse,
    @SerializedName("status")
    val status: String
)