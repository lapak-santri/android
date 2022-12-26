package com.example.lapaksantri.domain.usecases.order

import com.example.lapaksantri.domain.entities.Transaction
import com.example.lapaksantri.domain.repositories.OrderRepository
import com.example.lapaksantri.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    private val  repository: OrderRepository,
) {
    operator fun invoke(): Flow<Resource<List<Transaction>>> = repository.getTransactions()
}