package com.example.actividad2_ddam.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// DAO (Data Access Object) para la entidad Nota
@Dao
interface NotaDao {
    @Query("SELECT * FROM notas")
    fun getTodasLasNotas(): Flow<List<Nota>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarNota(nota: Nota): Long // <-- Agrega : Long

    @Update
    suspend fun actualizarNota(nota: Nota): Int // <-- Agrega : Int

    @Delete
    suspend fun eliminarNota(nota: Nota): Int // <-- Agrega : Int

    @Query("SELECT * FROM notas WHERE id = :id")
    fun getNotaPorId(id: Int): Nota?
}