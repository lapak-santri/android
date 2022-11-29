package com.example.lapaksantri.domain.repositories

import com.example.lapaksantri.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(email: String, password: String): Flow<Resource<String>>
    fun checkToken(): Flow<Resource<Boolean>>
    fun register(name: String, email: String, password: String): Flow<Resource<String>>
    fun getName(): Flow<Resource<String>>
}