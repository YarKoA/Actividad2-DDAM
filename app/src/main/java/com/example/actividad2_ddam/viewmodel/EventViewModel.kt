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

    // 1. Calculamos el día actual
    private val diasMap = mapOf(
        DayOfWeek.MONDAY to "Lun", DayOfWeek.TUESDAY to "Mar", DayOfWeek.WEDNESDAY to "Mie",
        DayOfWeek.THURSDAY to "Jue", DayOfWeek.FRIDAY to "Vie", DayOfWeek.SATURDAY to "Sab", DayOfWeek.SUNDAY to "Dom"
    )
    private val diaActual = diasMap[LocalDate.now().dayOfWeek] ?: "Lun"

    // 2. Estado reactivo del día seleccionado
    private val _diaSeleccionado = MutableStateFlow(diaActual)
    val diaSeleccionado: StateFlow<String> = _diaSeleccionado.asStateFlow()

    // 3. Mantenemos una lista global en caché para búsquedas rápidas (como getEventById)
    private val _todasLasTareas = repository.todasLasTareas
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // 4. Magia reactiva: Combina la BD y el día seleccionado para filtrar automáticamente
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
                    repository.insertarTarea(Tarea(id = 1, titulo = "Salir a trotar", desc = "Jogging por 30 minutos", hora = "10:30 am", dia = diaActual))
                    repository.insertarTarea(Tarea(id = 2, titulo = "Junta a la 1", desc = "Reunión de equipo en Zoom", hora = "1:00 pm", dia = diaActual))
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















/*
package com.example.actividad2_ddam.viewmodel

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.actividad2_ddam.model.Tarea
import com.example.actividad2_ddam.model.Repo

class EventViewModel : ViewModel() {

    // Lista observable de tareas sincronizada con el repositorio
    val events: SnapshotStateList<Tarea> = Repo.tareas

    // Función para añadir una nueva tarea
    fun addEvent(event: Tarea) {
        Repo.tareas.add(event)
    }

    // Función para eliminar una tarea
    fun removeEvent(event: Tarea) {
        Repo.tareas.remove(event)
    }

    // Función para actualizar una tarea existente
    fun updateEvent(updatedEvent: Tarea) {
        val index = Repo.tareas.indexOfFirst { it.id == updatedEvent.id }
        if (index != -1) {
            Repo.tareas[index] = updatedEvent
        }
    }

    // Función para obtener una tarea por su ID
    fun getEventById(id: Int): Tarea? {
        return Repo.tareas.find { it.id == id }
    }
}


 */