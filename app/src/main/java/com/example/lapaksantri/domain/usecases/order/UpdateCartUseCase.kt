package com.example.lapaksantri.domain.usecases.order

import com.example.lapaksantri.domain.repositories.OrderRepository
import com.example.lapaksantri.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateCartUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    operator fun invoke(
        cartId: Int,
        type: String,
    ): Flow<Resource<String>> = repository.updateCarts(cartId, type)
}