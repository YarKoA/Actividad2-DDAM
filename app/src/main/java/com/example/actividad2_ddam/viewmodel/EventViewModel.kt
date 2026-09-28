package com.example.actividad2_ddam.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.actividad2_ddam.data.TareaRepository
import com.example.actividad2_ddam.model.Tarea
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val repository: TareaRepository
) : ViewModel() {

    private val diasMap = mapOf(
        DayOfWeek.MONDAY to "Lun", DayOfWeek.TUESDAY to "Mar", DayOfWeek.WEDNESDAY to "Mie",
        DayOfWeek.THURSDAY to "Jue", DayOfWeek.FRIDAY to "Vie", DayOfWeek.SATURDAY to "Sab", DayOfWeek.SUNDAY to "Dom"
    )
    private val diaActual = diasMap[LocalDate.now().dayOfWeek] ?: "Lun"

    private val _diaSeleccionado = MutableStateFlow(diaActual)
    val diaSeleccionado: StateFlow<String> = _diaSeleccionado.asStateFlow()

    // Exposed for CalendarScreen badges
    val todasLasTareas: Flow<List<Tarea>> = repository.todasLasTareas

    private val _todasLasTareas = repository.todasLasTareas
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val tareasFiltradas: StateFlow<List<Tarea>> = combine(
        repository.todasLasTareas,
        _diaSeleccionado
    ) { todas, dia ->
        todas
            .filter { it.dia.equals(dia, ignoreCase = true) }
            .sortedByDescending { it.esAnclada }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.todasLasTareas.firstOrNull()?.let { lista ->
                if (lista.isEmpty()) {
                    val hoy = LocalDate.now().toString()
                    repository.insertarTarea(Tarea(id = 1, titulo = "Salir a trotar", desc = "Jogging por 30 minutos", hora = "10:30 am", dia = diaActual, fecha = hoy))
                    repository.insertarTarea(Tarea(id = 2, titulo = "Junta a la 1", desc = "Reunión de equipo en Zoom", hora = "1:00 pm", dia = diaActual, fecha = hoy))
                }
            }
        }
    }

    fun actualizarDiaSeleccionado(nuevoDia: String) {
        _diaSeleccionado.value = nuevoDia
    }

    fun addEvent(event: Tarea) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertarTarea(event)
        }
    }

    fun removeEvent(event: Tarea) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.eliminarTarea(event)
        }
    }

    fun updateEvent(event: Tarea) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.actualizarTarea(event)
        }
    }

    fun toggleAnclar(event: Tarea) {
        viewModelScope.launch(Dispatchers.IO) {
            val tareaActualizada = event.copy(esAnclada = !event.esAnclada)
            repository.actualizarTarea(tareaActualizada)
        }
    }

    fun getEventById(id: Int): Tarea? {
        return _todasLasTareas.value.find { it.id == id }
    }
}