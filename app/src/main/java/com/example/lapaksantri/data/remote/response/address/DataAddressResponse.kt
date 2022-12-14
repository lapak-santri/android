package com.example.lapaksantri.data.remote.response.address


import com.google.gson.annotations.SerializedName

data class DataAddressResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("data")
    val `data`: List<AddressResponse>
)