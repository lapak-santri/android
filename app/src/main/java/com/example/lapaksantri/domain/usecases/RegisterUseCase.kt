package com.example.lapaksantri.domain.usecases

import com.example.lapaksantri.domain.repositories.AuthRepository
import com.example.lapaksantri.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(
        name: String,
        email: String,
        password: String
    ): Flow<Resource<String>> = repository.register(name, email, password)
}