package com.insanoid.whatsapp.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
  @Provides
  @Singleton
  fun provideFirebaseAuth(): FirebaseAuth{
      return FirebaseAuth.getInstance()
  }

    @Provides
    @Singleton
    fun provideFirebaseDatabase() :FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

}