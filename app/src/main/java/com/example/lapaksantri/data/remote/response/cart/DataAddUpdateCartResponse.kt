package com.example.lapaksantri.data.remote.response.cart


import com.google.gson.annotations.SerializedName

data class DataAddUpdateCartResponse(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("id_product")
    val idProduct: Int,
    @SerializedName("id_user")
    val idUser: Int,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("updatedAt")
    val updatedAt: String
)