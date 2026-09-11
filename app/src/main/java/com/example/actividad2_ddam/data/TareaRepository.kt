package com.example.actividad2_ddam.data

import com.example.actividad2_ddam.model.Tarea
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TareaRepository @Inject constructor(
    private val tareaDao: TareaDao
) {
    val todasLasTareas: Flow<List<Tarea>> = tareaDao.getTodasLasTareas()

    suspend fun insertarTarea(tarea: Tarea) = tareaDao.insertarTarea(tarea)

    suspend fun actualizarTarea(tarea: Tarea) = tareaDao.actualizarTarea(tarea)

    suspend fun eliminarTarea(tarea: Tarea) = tareaDao.eliminarTarea(tarea)

    suspend fun getTareaPorId(id: Int): Tarea? = tareaDao.getTareaPorId(id)
}
