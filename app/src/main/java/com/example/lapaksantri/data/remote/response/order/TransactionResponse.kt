package com.example.lapaksantri.data.remote.response.order


import com.google.gson.annotations.SerializedName

data class TransactionResponse(
    @SerializedName("address")
    val address: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("district")
    val district: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("gross_amount")
    val grossAmount: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("id_user")
    val idUser: Int,
    @SerializedName("invoice")
    val invoice: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("redirect_url")
    val redirectUrl: String,
    @SerializedName("token")
    val token: String,
    @SerializedName("transaction_code")
    val transactionCode: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("village")
    val village: String
)