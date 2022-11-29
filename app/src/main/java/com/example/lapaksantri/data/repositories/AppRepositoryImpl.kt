package com.example.lapaksantri.data.repositories

import com.example.lapaksantri.data.remote.network.AppApiService
import com.example.lapaksantri.data.remote.response.ErrorResponse
import com.example.lapaksantri.domain.entities.Article
import com.example.lapaksantri.domain.repositories.AppRepository
import com.example.lapaksantri.utils.Resource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.lapaksantri.domain.entities.Slider
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
//            val response = appApiService.getSliders()
//            emit(Resource.Success(response.map { it.toEntity() }))
            val listSliders = listOf(
                Slider(
                    1,
                    "https://parfumalyamani44342.com/wp-content/uploads/2020/11/air-santri.jpg",
                ),
                Slider(
                    2,
                    "https://parfumalyamani44342.com/wp-content/uploads/2020/11/air-santri.jpg",
                )
            )
            emit(Resource.Success(listSliders))
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
//            val response = appApiService.getSliders()
//            emit(Resource.Success(response.map { it.toEntity() }))
            val listArticles = listOf(
                Article(
                    1,
                    "",
                    "Deskripsi",
                "Judul Artikel",
                    "https://parfumalyamani44342.com/wp-content/uploads/2020/11/air-santri.jpg",
                ),
                Article(
                    2,
                    "",
                    "Deskripsi",
                "Judul Artikel",
                    "https://parfumalyamani44342.com/wp-content/uploads/2020/11/air-santri.jpg",
                ),
                Article(
                    3,
                    "",
                    "Deskripsi",
                "Judul Artikel",
                    "https://parfumalyamani44342.com/wp-content/uploads/2020/11/air-santri.jpg",
                ),
            )
            emit(Resource.Success(listArticles))
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