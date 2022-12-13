package com.example.lapaksantri.domain.repositories

import com.example.lapaksantri.domain.entities.Address
import com.example.lapaksantri.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AddressRepository {
    fun getMainAddress(): Flow<Resource<Address?>>
    fun setMainAddress(id: Int): Flow<Resource<String>>
    fun getAddresses(): Flow<Resource<List<Address>>>
}