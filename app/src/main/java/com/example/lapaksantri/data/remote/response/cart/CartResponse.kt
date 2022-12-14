package com.example.lapaksantri.data.remote.response.cart


import com.example.lapaksantri.data.remote.response.product.ProductResponse
import com.google.gson.annotations.SerializedName

data class CartResponse(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("id_product")
    val idProduct: Int,
    @SerializedName("id_user")
    val idUser: Int,
    @SerializedName("Product")
    val product: ProductResponse,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("updatedAt")
    val updatedAt: String,
)