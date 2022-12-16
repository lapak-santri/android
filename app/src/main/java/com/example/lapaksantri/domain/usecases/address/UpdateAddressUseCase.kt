package com.example.lapaksantri.domain.usecases.address

import com.example.lapaksantri.domain.repositories.AddressRepository
import com.example.lapaksantri.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateAddressUseCase @Inject constructor(
    private val repository: AddressRepository
) {
    operator fun invoke(
        id: Int,
        recipient: String,
        phone: String,
        village: String,
        district: String,
        detailAddress: String,
        area: String,
        isMain: Boolean,
    ): Flow<Resource<String>> = repository.updateAddress(
        id = id,
        recipient = recipient,
        phone = phone,
        village = village,
        district = district,
        detailAddress = detailAddress,
        area = area,
        isMain = isMain,
    )
}