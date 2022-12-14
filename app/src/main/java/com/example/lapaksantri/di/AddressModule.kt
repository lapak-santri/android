package com.example.lapaksantri.di

import com.example.lapaksantri.data.local.data_store.DataStoreManager
import com.example.lapaksantri.data.remote.network.AddressApiService
import com.example.lapaksantri.data.repositories.AddressRepositoryImpl
import com.example.lapaksantri.domain.repositories.AddressRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object AddressModule {
    @Provides
    @Singleton
    fun provideAddressApiService(retrofit: Retrofit): AddressApiService = retrofit.create(AddressApiService::class.java)

    @Provides
    @Singleton
    fun provideAddressRepository(
        addressApiService: AddressApiService,
        dataStoreManager: DataStoreManager,
    ): AddressRepository = AddressRepositoryImpl(
        addressApiService,
        dataStoreManager,
    )
}