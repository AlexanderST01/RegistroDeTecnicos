package com.ucne.registrodetecnicos.data.repository

import com.ucne.registrodetecnicos.data.local.dao.ServicioDao
import com.ucne.registrodetecnicos.data.local.entities.ServicioEntity

class ServicioRepository(private val servicioDao: ServicioDao) {
    suspend fun saveServicio(servicio: ServicioEntity) = servicioDao.save(servicio)
    suspend fun deleteServicio(servicio: ServicioEntity) = servicioDao.delete(servicio)
    fun getServicios() = servicioDao.getAll()
    suspend fun getServicio(id: Int) = servicioDao.find(id)
}