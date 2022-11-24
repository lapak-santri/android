package com.example.lapaksantri.domain.usecases

import com.example.lapaksantri.domain.repositories.AuthRepository
import com.example.lapaksantri.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckTokenUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): Flow<Resource<Boolean>> = repository.checkToken()
}