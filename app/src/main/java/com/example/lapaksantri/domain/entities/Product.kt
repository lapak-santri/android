package com.example.lapaksantri.domain.entities

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val stock: Int,
    val unitName: String,
    val imagePath: String,
)