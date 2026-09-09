package com.example.actividad2_ddam.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Entidad Room + SQLite para almacenar notas
@Entity(tableName = "notas")
data class Nota(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titulo: String,
    val contenido: String,
    val fecha: String
)
