package com.example.actividad2_ddam.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.actividad2_ddam.model.Tarea

@Database(entities = [Nota::class, Tarea::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notaDao(): NotaDao
    abstract fun tareaDao(): TareaDao
}