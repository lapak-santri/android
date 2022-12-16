package com.example.lapaksantri.data.remote.request


import com.google.gson.annotations.SerializedName

data class AddUpdateAddressRequest(
    @SerializedName("area")
    val area: String,
    @SerializedName("detail_address")
    val detailAddress: String,
    @SerializedName("district")
    val district: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("recipient")
    val recipient: String,
    @SerializedName("village")
    val village: String
)