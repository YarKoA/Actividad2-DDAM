package com.example.actividad2_ddam.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.actividad2_ddam.data.Nota
import com.example.actividad2_ddam.data.NotaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotaViewModel @Inject constructor(
    private val repository: NotaRepository
) : ViewModel() {

    // 1. Estado de las notas
    val notas: StateFlow<List<Nota>> = repository.todasLasNotas
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // 2. Estado para el mensaje de error (Inicia en null porque no hay error)
    private val _mensajeError = MutableStateFlow<String?>(null)
    val mensajeError: StateFlow<String?> = _mensajeError.asStateFlow()

    // 3. Función para limpiar el error una vez que la pantalla ya lo mostró
    fun borrarError() {
        _mensajeError.value = null
    }

    fun agregarNota(titulo: String, contenido: String, fecha: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val nuevaNota = Nota(titulo = titulo, contenido = contenido, fecha = fecha)
                repository.insertarNota(nuevaNota)
            } catch (e: Exception) {
                // Atrapamos el error y enviamos el texto a la interfaz
                _mensajeError.value = "Error al guardar la nota. Inténtalo de nuevo."
            }
        }
    }

    fun eliminarNota(nota: Nota) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.eliminarNota(nota)
            } catch (e: Exception) {
                _mensajeError.value = "No se pudo eliminar la nota."
            }
        }
    }

    fun actualizarNota(nota: Nota) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.actualizarNota(nota)
            } catch (e: Exception) {
                _mensajeError.value = "Error al actualizar la nota."
            }
        }
    }
}






















/*
package com.example.actividad2_ddam.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.actividad2_ddam.data.Nota
import com.example.actividad2_ddam.data.NotaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

// ViewModel reactivo para gestionar la lógica de Notas con Room
@HiltViewModel
class NotaViewModel @Inject constructor(
    private val repository: NotaRepository
) : ViewModel() {

    // Lista en tiempo real de notas obtenida de SQLite
    val notas: StateFlow<List<Nota>> = repository.todasLasNotas
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun agregarNota(titulo: String, contenido: String, fecha: String) {
        viewModelScope.launch {
            val nuevaNota = Nota(
                titulo = titulo,
                contenido = contenido,
                fecha = fecha
            )
            repository.insertarNota(nuevaNota)
        }
    }

    fun eliminarNota(nota: Nota) {
        viewModelScope.launch {
            repository.eliminarNota(nota)
        }
    }

    fun actualizarNota(nota: Nota) {
        viewModelScope.launch {
            repository.actualizarNota(nota)
        }
    }
}


 */