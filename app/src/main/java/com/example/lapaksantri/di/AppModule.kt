package com.example.lapaksantri.di

import android.content.Context
import com.example.lapaksantri.data.local.data_store.DataStoreManager
import com.example.lapaksantri.data.remote.network.AppApiService
import com.example.lapaksantri.data.remote.network.AuthApiService
import com.example.lapaksantri.data.repositories.AppRepositoryImpl
import com.example.lapaksantri.data.repositories.AuthRepositoryImpl
import com.example.lapaksantri.domain.repositories.AppRepository
import com.example.lapaksantri.domain.repositories.AuthRepository
import com.example.lapaksantri.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    fun provideAppRepository(
        authApiService: AppApiService,
    ): AppRepository {
        return AppRepositoryImpl(authApiService)
    }

    @Provides
    @Singleton
    fun provideAppApiService(retrofit: Retrofit): AppApiService = retrofit.create(AppApiService::class.java)

    @Singleton
    @Provides
    fun provideDataStoreManager(@ApplicationContext context: Context): DataStoreManager {
        return DataStoreManager(context)
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}