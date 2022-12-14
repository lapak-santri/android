package com.example.lapaksantri.domain.entities

data class Cart(
    val id: Int,
    val cartId: ArrayList<Int>,
    val name: String,
    val price: Double,
    val imagePath: String,
    var quantity: Int,
)