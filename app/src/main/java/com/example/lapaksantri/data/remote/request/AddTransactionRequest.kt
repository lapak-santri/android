package com.example.lapaksantri.data.remote.request


import com.google.gson.annotations.SerializedName

data class AddTransactionRequest(
    @SerializedName("address")
    val address: String,
    @SerializedName("detail")
    val detailRequest: List<DetailRequest>,
    @SerializedName("district")
    val district: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("gross_amount")
    val grossAmount: Int,
    @SerializedName("invoice")
    val invoice: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("transaction_code")
    val transactionCode: String,
    @SerializedName("village")
    val village: String
)