package com.example.lapaksantri.data.repositories

import com.example.lapaksantri.data.local.data_store.DataStoreManager
import com.example.lapaksantri.data.remote.network.AuthApiService
import com.example.lapaksantri.data.remote.request.LoginRequest
import com.example.lapaksantri.data.remote.request.RegisterRequest
import com.example.lapaksantri.domain.repositories.AuthRepository
import com.example.lapaksantri.utils.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class AuthRepositoryImpl(
    private val authApiService: AuthApiService,
    private val dataStoreManager: DataStoreManager
) : AuthRepository {
    override fun login(email: String, password: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val response = authApiService.login(
                LoginRequest(
                    email = email,
                    password = password
                )
            )
            dataStoreManager.saveName(response.name)
            dataStoreManager.saveToken(response.token)
        } catch (e: Exception) {
            emit(Resource.Error("Terjadi Kesalahan"))
        }
    }

    override fun checkToken(): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())
        try {
            delay(1000)
            val token = dataStoreManager.token.first()
            if (token != "") {
                emit(Resource.Success(true))
            } else {
                emit(Resource.Error("Token Tidak Ada", false))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Terjadi Kesalahan", false))
        }
    }

    override fun register(name: String, email: String, password: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val response = authApiService.register(
                RegisterRequest(
                    name = name,
                    email = email,
                    password = password
                )
            )
            emit(Resource.Success(response.message))
        } catch (e: Exception) {
            emit(Resource.Error("Terjadi Kesalahan"))
        }
    }
}