package com.example.lapaksantri.domain.entities

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val stock: Int,
    val imagePath: String,
    var quantityInCart: Int = 0
)