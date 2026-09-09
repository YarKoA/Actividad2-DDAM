package com.example.actividad2_ddam.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// Repositorio para coordinar el acceso a los datos de Notas con Room
@Singleton
class NotaRepository @Inject constructor(
    private val notaDao: NotaDao
) {
    val todasLasNotas: Flow<List<Nota>> = notaDao.getTodasLasNotas()

    suspend fun insertarNota(nota: Nota) = notaDao.insertarNota(nota)

    suspend fun actualizarNota(nota: Nota) = notaDao.actualizarNota(nota)

    suspend fun eliminarNota(nota: Nota) = notaDao.eliminarNota(nota)

    suspend fun getNotaPorId(id: Int): Nota? = notaDao.getNotaPorId(id)
}
