package com.example.lapaksantri.data.remote.response.address


import com.google.gson.annotations.SerializedName

data class AddressResponse(
    @SerializedName("area")
    val area: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("detail_address")
    val detailAddress: String,
    @SerializedName("district")
    val district: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("id_user")
    val idUser: Int,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("recipient")
    val recipient: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("village")
    val village: String
)