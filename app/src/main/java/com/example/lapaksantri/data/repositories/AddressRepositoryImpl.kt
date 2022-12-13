package com.example.lapaksantri.data.repositories

import com.example.lapaksantri.data.local.data_store.DataStoreManager
import com.example.lapaksantri.data.remote.network.AddressApiService
import com.example.lapaksantri.data.remote.response.ErrorResponse
import com.example.lapaksantri.domain.entities.Address
import com.example.lapaksantri.domain.repositories.AddressRepository
import com.example.lapaksantri.utils.Resource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(
    private val addressApiService: AddressApiService,
    private val dataStoreManager: DataStoreManager
) : AddressRepository {
    override fun getMainAddress(): Flow<Resource<Address?>> = flow {
        emit(Resource.Loading())
        try {
            val token = dataStoreManager.token.first()
            if (token != "") {
                val response = addressApiService.getAddresses(
                    token = "Bearer $token"
                )
                val addressId = dataStoreManager.addressId.first()
                if (addressId == -1) {
                    emit(Resource.Success(null))
                } else {
                    val addressResponse = response.dataAddressResponse.data.first { it.id == addressId }
                    emit(Resource.Success(
                        Address(
                            id = addressResponse.id,
                            recipient = addressResponse.recipient,
                            phone = addressResponse.phone,
                            detailAddress = addressResponse.detailAddress,
                            area = addressResponse.area,
                            district = addressResponse.district,
                            village = addressResponse.village,
                            isMain = true,
                        )
                    ))
                }
            } else {
                emit(Resource.Error("Token Not Exist"))
            }
        } catch (e: Exception) {
            when(e) {
                is HttpException -> {
                    val errorMessageResponseType = object : TypeToken<ErrorResponse>() {}.type
                    val error: ErrorResponse = Gson().fromJson(e.response()?.errorBody()?.charStream(), errorMessageResponseType)
                    emit(Resource.Error(error.errorMessageResponse.message))
                }
                else -> {
                    emit(Resource.Error("An unexpected error occurred"))
                }
            }
        }
    }

    override fun setMainAddress(id: Int): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            dataStoreManager.saveAddressId(id)
            emit(Resource.Success("Success"))
        } catch (e: Exception) {
            emit(Resource.Error("An unexpected error occurred"))
        }
    }

    override fun getAddresses(): Flow<Resource<List<Address>>> = flow {
        emit(Resource.Loading())
        try {
            val token = dataStoreManager.token.first()
            if (token != "") {
                val response = addressApiService.getAddresses(
                    token = "Bearer $token"
                )
                val addressId = dataStoreManager.addressId.first()
                emit(Resource.Success(response.dataAddressResponse.data.map {
                    Address(
                        id = it.id,
                        recipient = it.recipient,
                        phone = it.phone,
                        detailAddress = it.detailAddress,
                        area = it.area,
                        district = it.district,
                        village = it.village,
                        isMain = it.id == addressId,
                    )
                }))
            } else {
                emit(Resource.Error("Token Not Exist"))
            }
        } catch (e: Exception) {
            when(e) {
                is HttpException -> {
                    val errorMessageResponseType = object : TypeToken<ErrorResponse>() {}.type
                    val error: ErrorResponse = Gson().fromJson(e.response()?.errorBody()?.charStream(), errorMessageResponseType)
                    emit(Resource.Error(error.errorMessageResponse.message))
                }
                else -> {
                    emit(Resource.Error("An unexpected error occurred"))
                }
            }
        }
    }
}