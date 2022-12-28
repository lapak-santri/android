package com.example.lapaksantri.data.repositories

import com.example.lapaksantri.data.remote.network.AppApiService
import com.example.lapaksantri.data.remote.response.ErrorResponse
import com.example.lapaksantri.domain.entities.Article
import com.example.lapaksantri.domain.repositories.AppRepository
import com.example.lapaksantri.utils.Resource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.lapaksantri.domain.entities.Slider
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val appApiService: AppApiService
): AppRepository {
    override fun getSliders(): Flow<Resource<List<Slider>>> = flow {
        emit(Resource.Loading())
        try {
            val response = appApiService.getSliders()
            emit(Resource.Success(response.dataSliderResponse.data.map {
                Slider(
                    id = it.id,
                    imagePath = it.image[0]
                )
            }))
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

    override fun getArticles(): Flow<Resource<List<Article>>> = flow {
        emit(Resource.Loading())
        try {
            val response = appApiService.getArticles()
            emit(Resource.Success(response.dataArticleResponse.data.map {
                Article(
                    id = it.id,
                    title = it.title,
                    description = it.description,
                    publishedAt = it.createdAt,
                    imagePath = it.image[0],
                )
            }))
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