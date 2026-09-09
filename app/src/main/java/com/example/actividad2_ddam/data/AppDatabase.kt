package com.example.actividad2_ddam.data

import androidx.room.Database
import androidx.room.RoomDatabase

// Base de datos SQLite gestionada por Room
@Database(entities = [Nota::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notaDao(): NotaDao
}
