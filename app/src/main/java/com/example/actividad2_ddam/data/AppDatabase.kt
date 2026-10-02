package com.example.actividad2_ddam.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.actividad2_ddam.model.Tarea

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE tareas ADD COLUMN fecha TEXT NOT NULL DEFAULT ''")
    }
}

@Database(entities = [Nota::class, Tarea::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notaDao(): NotaDao
    abstract fun tareaDao(): TareaDao
}