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
