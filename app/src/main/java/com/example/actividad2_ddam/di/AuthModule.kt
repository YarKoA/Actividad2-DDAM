package com.example.actividad2_ddam.di

import com.example.actividad2_ddam.auth.data.AuthRepository
import com.example.actividad2_ddam.auth.data.FirebaseAuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule{

    @Provides
    @Singleton
    fun provideFirebaseAuth() : FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun provideAuthRepository(auth : FirebaseAuth) : AuthRepository =
        FirebaseAuthRepository(auth)

}