package com.example.lapaksantri.domain.usecases

import com.example.lapaksantri.domain.repositories.AuthRepository
import com.example.lapaksantri.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(email: String, password: String): Flow<Resource<String>> = repository.login(email, password)
}