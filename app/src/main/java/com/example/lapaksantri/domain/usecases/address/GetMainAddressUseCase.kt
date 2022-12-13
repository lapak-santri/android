package com.example.lapaksantri.domain.usecases.address

import com.example.lapaksantri.domain.entities.Address
import com.example.lapaksantri.domain.repositories.AddressRepository
import com.example.lapaksantri.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMainAddressUseCase @Inject constructor(
    private val repository: AddressRepository,
) {
    operator fun invoke(): Flow<Resource<Address>> = repository.getMainAddress()
}