package com.example.lapaksantri.di

import com.example.lapaksantri.data.local.data_store.DataStoreManager
import com.example.lapaksantri.data.remote.network.AuthApiService
import com.example.lapaksantri.data.remote.network.OrderApiService
import com.example.lapaksantri.data.repositories.AuthRepositoryImpl
import com.example.lapaksantri.data.repositories.OrderRepositoryImpl
import com.example.lapaksantri.domain.repositories.AuthRepository
import com.example.lapaksantri.domain.repositories.OrderRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object OrderModule {
    @Provides
    @Singleton
    fun provideOrderRepository(
        authApiService: OrderApiService,
        dataStoreManager: DataStoreManager
    ): OrderRepository {
        return OrderRepositoryImpl(authApiService, dataStoreManager)
    }

    @Provides
    @Singleton
    fun provideOrderApiService(retrofit: Retrofit): OrderApiService = retrofit.create(OrderApiService::class.java)
}