package com.example.lapaksantri.domain.usecases.order

import com.example.lapaksantri.domain.entities.Cart
import com.example.lapaksantri.domain.repositories.OrderRepository
import com.example.lapaksantri.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddCartsUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    operator fun invoke(carts: List<Cart>): Flow<Resource<String>> = repository.addCarts(carts)
}