package com.example.actividad2_ddam.auth.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthRepository @Inject constructor(
    private  val auth : FirebaseAuth
) : AuthRepository {

    override val authState: Flow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener{ fa ->
            trySend(fa.currentUser)
        }

        auth.addAuthStateListener(listener)
        trySend(auth.currentUser)
        awaitClose { auth.removeAuthStateListener(listener) }
    }.distinctUntilChanged()

    override suspend fun signIn(email: String, pass: String): Result<Unit> =
        kotlin.runCatching {
            auth.signInWithEmailAndPassword(email, pass).await()
            Unit
        }

    override suspend fun register(email: String, pass: String): Result<Unit> =
        kotlin.runCatching {
            auth.createUserWithEmailAndPassword(email, pass).await()
            Unit
        }

    override fun signOut(){
        auth.signOut()
    }

    override fun currentUser() : FirebaseUser? = auth.currentUser
}