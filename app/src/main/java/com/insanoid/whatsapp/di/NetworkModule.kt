package com.insanoid.whatsapp.di

import com.insanoid.whatsapp.model.remote.AIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideAIService(): AIService {
        return Retrofit.Builder()
            .baseUrl("https://your-ai-api.com/") // Replace with actual API URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AIService::class.java)
    }
}
