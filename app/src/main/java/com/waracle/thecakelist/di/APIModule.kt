package com.waracle.thecakelist.di

import com.google.gson.GsonBuilder
import com.waracle.thecakelist.api.CakeListService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class APIModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://gist.githubusercontent.com/t-reed/739df99e9d96700f17604a3971e701fa/")
            .addConverterFactory(
                GsonConverterFactory.create(GsonBuilder().create())
            ).build()
    }

    @Singleton
    @Provides
    fun provideCakeListService(retrofit: Retrofit): CakeListService =
        retrofit.create(CakeListService::class.java)
}