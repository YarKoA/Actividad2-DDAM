package com.example.actividad2_ddam.model

import com.example.actividad2_ddam.interfaces.Gestionable

class Tarea(
    val titulo: String,
    val descripcion: String,
    val hora: String,
    val dia: String,
    var completada: Boolean = false
) : Gestionable {

    override fun mostrarInformacion(): String {
        return "$titulo - $dia a las $hora"
    }

    override fun estaPendiente(): Boolean {
        return !completada
    }
}
