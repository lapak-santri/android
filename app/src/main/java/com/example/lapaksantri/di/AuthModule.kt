package com.example.lapaksantri.di

import com.example.lapaksantri.data.local.data_store.DataStoreManager
import com.example.lapaksantri.data.remote.network.AuthApiService
import com.example.lapaksantri.data.repositories.AuthRepositoryImpl
import com.example.lapaksantri.domain.repositories.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AuthModule {
    @Provides
    @Singleton
    fun provideAuthRepository(
        authApiService: AuthApiService,
        dataStoreManager: DataStoreManager,
    ): AuthRepository {
        return AuthRepositoryImpl(authApiService, dataStoreManager)
    }

    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService = retrofit.create(AuthApiService::class.java)
}