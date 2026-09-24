package com.example.actividad2_ddam.auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.actividad2_ddam.auth.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterUiState(
    val loading : Boolean = false,
    val error : String? = null
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repo : AuthRepository
) : ViewModel(){
    private val _ui = MutableStateFlow(RegisterUiState())

    val ui : StateFlow<RegisterUiState> = _ui

    sealed interface RegisterEvent{
        data object Success : RegisterEvent
    }

    private val _events = MutableSharedFlow<RegisterEvent>(replay = 0)

    val event = _events.asSharedFlow()

    fun register(email : String, pass : String){
        viewModelScope.launch {
            _ui.update { current ->
                current.copy(
                    loading = true,
                    error = null
                )
            }

            val r = repo.register(email.trim(), pass)

            if(r.isSuccess){
                _ui.update { current ->
                    current.copy(
                        loading = false,
                        error = null
                    )
                }

                _events.emit(RegisterEvent.Success)
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
}