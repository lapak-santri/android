package com.example.lapaksantri.domain.usecases.order

import com.example.lapaksantri.domain.entities.Address
import com.example.lapaksantri.domain.entities.Transaction
import com.example.lapaksantri.domain.repositories.AuthRepository
import com.example.lapaksantri.domain.repositories.OrderRepository
import com.example.lapaksantri.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val repository: OrderRepository
) {
    operator fun invoke(
        address: Address,
        totalPrice: Int,
    ) : Flow<Resource<Transaction>> = flow {
        authRepository.getUser().collect { result ->
            when(result) {
                is Resource.Success -> {
                    result.data?.let { user ->
                        repository.addTransactions(
                            name = user.name,
                            email = user.email,
                            address = address,
                            totalPrice = totalPrice
                        ).collect {
                            emit(it)
                        }
                    } ?: kotlin.run {
                        emit(Resource.Error("An unexpected error occurred"))
                    }
                }
                is Resource.Error -> {
                    emit(Resource.Error("An unexpected error occurred"))
                }
                is Resource.Loading -> {
                    emit(Resource.Loading())
                }
            }
        }
    }

}