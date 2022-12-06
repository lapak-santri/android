package com.example.lapaksantri.domain.usecases.slider

import com.example.lapaksantri.domain.entities.Slider
import com.example.lapaksantri.domain.repositories.AppRepository
import com.example.lapaksantri.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSlidersUseCase @Inject constructor(
    private val repository: AppRepository
) {
    operator fun invoke(): Flow<Resource<List<Slider>>> = repository.getSliders()
}