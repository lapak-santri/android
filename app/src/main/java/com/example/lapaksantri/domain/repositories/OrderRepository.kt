package com.example.lapaksantri.domain.repositories

import com.example.lapaksantri.domain.entities.Cart
import com.example.lapaksantri.domain.entities.Product
import com.example.lapaksantri.utils.Resource
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getProducts(): Flow<Resource<List<Product>>>
    fun addCarts(
        carts: List<Cart>
    ): Flow<Resource<String>>
}