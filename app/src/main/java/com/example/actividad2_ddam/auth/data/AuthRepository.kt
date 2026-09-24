package com.example.actividad2_ddam.auth.data

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val authState : Flow<FirebaseUser?>

    suspend fun signIn(email : String, pass : String) : Result<Unit>

    suspend fun register (email : String, pass : String) : Result<Unit>

    fun signOut()

    fun currentUser() : FirebaseUser?

}