package com.example.actividad2_ddam.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.actividad2_ddam.data.SettingsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsManager: SettingsManager
) : ViewModel() {

    fun setModoOscuro(enabled: Boolean) {
        viewModelScope.launch {
            settingsManager.setModoOscuro(enabled)
        }
    }

    fun setLetraGrande(enabled: Boolean) {
        viewModelScope.launch {
            settingsManager.setLetraGrande(enabled)
        }
    }

    fun setGrosorGrueso(enabled: Boolean) {
        viewModelScope.launch {
            settingsManager.setGrosorGrueso(enabled)
        }
    }
}
