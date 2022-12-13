package com.example.lapaksantri.domain.usecases.order

import com.example.lapaksantri.domain.repositories.OrderRepository
import com.example.lapaksantri.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteCartUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    operator fun invoke(cartId: Int): Flow<Resource<String>> = repository.deleteCarts(cartId)
}