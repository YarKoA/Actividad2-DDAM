package com.example.actividad2_ddam.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.actividad2_ddam.model.Tarea
import kotlinx.coroutines.flow.Flow

@Dao
interface TareaDao {
    @Query("SELECT * FROM tareas")
    fun getTodasLasTareas(): Flow<List<Tarea>>

    @Query("SELECT * FROM tareas WHERE id = :id")
    suspend fun getTareaPorId(id: Int): Tarea?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTarea(tarea: Tarea): Long

    @Update
    suspend fun actualizarTarea(tarea: Tarea): Int

    @Delete
    suspend fun eliminarTarea(tarea: Tarea): Int
}
