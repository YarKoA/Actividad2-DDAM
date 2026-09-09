package com.example.actividad2_ddam.viewmodel

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.actividad2_ddam.model.Event
import com.example.actividad2_ddam.model.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor() : ViewModel() {

    // Lista observable de eventos sincronizada con el repositorio
    val events: SnapshotStateList<Event> = Repo.tareas

    // Función para añadir un nuevo evento
    fun addEvent(event: Event) {
        Repo.tareas.add(event)
    }

    // Función para eliminar un evento
    fun removeEvent(event: Event) {
        Repo.tareas.remove(event)
    }

    // Función para actualizar un evento existente
    fun updateEvent(updatedEvent: Event) {
        val index = Repo.tareas.indexOfFirst { it.id == updatedEvent.id }
        if (index != -1) {
            Repo.tareas[index] = updatedEvent
        }
    }
}
