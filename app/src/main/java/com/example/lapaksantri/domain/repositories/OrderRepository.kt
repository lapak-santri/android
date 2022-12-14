package com.example.lapaksantri.domain.repositories

import com.example.lapaksantri.domain.entities.Product
import com.example.lapaksantri.utils.Resource
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getProducts(): Flow<Resource<List<Product>>>
    fun getCarts(): Flow<Resource<List<Cart>>>
    fun addCarts(
        carts: List<Cart>
    ): Flow<Resource<String>>
    fun updateCarts(
        cartId: Int,
        type: String
    ): Flow<Resource<String>>
    fun deleteCarts(cartId: Int): Flow<Resource<String>>
    fun addCarts(
        carts: List<Cart>
    ): Flow<Resource<String>>
}