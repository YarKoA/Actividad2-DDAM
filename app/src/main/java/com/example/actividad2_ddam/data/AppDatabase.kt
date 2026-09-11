package com.example.actividad2_ddam.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.actividad2_ddam.model.Tarea

// 1. Agregamos Tarea::class a la lista
@Database(entities = [Nota::class, Tarea::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notaDao(): NotaDao
    // 2. Agregamos el DAO de tareas
    abstract fun tareaDao(): TareaDao
}