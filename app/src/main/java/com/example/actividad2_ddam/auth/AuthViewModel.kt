package com.example.actividad2_ddam.auth.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(repo : AuthRepository) : ViewModel(){
    val user : StateFlow<FirebaseUser?> = repo.authState.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        null
    )
}