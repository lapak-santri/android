package com.example.lapaksantri.domain.usecases.address

import com.example.lapaksantri.domain.repositories.AddressRepository
import com.example.lapaksantri.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteAddressUseCase @Inject constructor(
    private val repository: AddressRepository,
) {
    operator fun invoke(addressId: Int): Flow<Resource<String>> = repository.deleteAddress(addressId)
}