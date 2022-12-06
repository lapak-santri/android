package com.example.lapaksantri.domain.usecases.auth

import com.example.lapaksantri.domain.entities.User
import com.example.lapaksantri.domain.repositories.AuthRepository
import com.example.lapaksantri.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): Flow<Resource<User>> = repository.getUser()
}