package com.example.actividad2_ddam.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// DAO (Data Access Object) para la entidad Nota
@Dao
interface NotaDao {

    @Query("SELECT * FROM notas ORDER BY id DESC")
    fun getTodasLasNotas(): Flow<List<Nota>>

    @Query("SELECT * FROM notas WHERE id = :id LIMIT 1")
    suspend fun getNotaPorId(id: Int): Nota?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarNota(nota: Nota)

    @Update
    suspend fun actualizarNota(nota: Nota)

    @Delete
    suspend fun eliminarNota(nota: Nota)
}
