package com.example.actividad2_ddam.auth.presentation.login

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.actividad2_ddam.auth.data.AuthRepository
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

data class LoginUiState(
    val loading : Boolean = false,
    val error : String? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo : AuthRepository) : ViewModel(){

        private val _ui = MutableStateFlow(LoginUiState())
        val ui : StateFlow<LoginUiState> = _ui

    sealed interface Event {
        data object Success : Event
    }

    private val _event = MutableSharedFlow<Event>()

    val event : SharedFlow<Event> = _event.asSharedFlow()

    fun signIn(email : String, pass : String){
        viewModelScope.launch {
            _ui.update { current ->
                current.copy(
                    loading = true,
                    error = null
                )
            }

            val r = repo.signIn(email.trim(), pass)

            if(r.isSuccess){
                _ui.update{ current ->
                    current.copy(
                        loading = false
                    )
                }

                _event.emit(Event.Success)
            } else{
                _ui.update { current ->
                    current.copy(
                        loading = false,
                        error = r.exceptionOrNull()?.toReadable()
                    )
                }
            }
        }
    }

    private fun Throwable.toReadable() : String =
        (this.message ?: "Error inespereado. Intentalo de nuevo!")

    // ====================================================================
    // NUEVA FUNCIÓN: INICIO DE SESIÓN CON GOOGLE
    // ====================================================================
    fun signInWithGoogle(context: Context) {
        viewModelScope.launch {
            // 1. Mostramos el estado de carga y limpiamos errores previos
            _ui.update { current ->
                current.copy(loading = true, error = null)
            }

            try {
                val credentialManager = CredentialManager.create(context)

                // 2. Configuramos Google. ¡RECUERDA PEGAR TU WEB CLIENT ID AQUÍ!
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId("385239760493-rlfgr3ke64vn7fripnpiehn81343v03o.apps.googleusercontent.com")
                    .setAutoSelectEnabled(true)
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                // 3. Lanzamos el Bottom Sheet de Google
                val result = credentialManager.getCredential(context, request)
                val credential = result.credential

                // 4. Verificamos el token
                if (credential is CustomCredential &&
                    credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {

                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    val authCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)

                    // 5. Autenticamos en Firebase
                    FirebaseAuth.getInstance().signInWithCredential(authCredential).await()

                    // 6. ¡Éxito! Ocultamos carga y emitimos el evento para navegar al Home
                    _ui.update { current -> current.copy(loading = false) }
                    _event.emit(Event.Success)

                } else {
                    // Si la credencial no es de Google, mostramos error
                    _ui.update { current ->
                        current.copy(loading = false, error = "Credencial no reconocida")
                    }
                }
            } catch (e: Exception) {
                // Manejo de errores (ej. el usuario cerró la ventana de Google)
                _ui.update { current ->
                    current.copy(loading = false, error = e.toReadable())
                }
            }
        }
    }
}