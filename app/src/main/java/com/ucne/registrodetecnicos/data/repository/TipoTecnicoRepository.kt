package com.ucne.registrodetecnicos.data.repository
import com.ucne.registrodetecnicos.data.local.dao.TipoTecnicoDao
import com.ucne.registrodetecnicos.data.local.entities.TipoTecnicoEntity

class TipoTecnicoRepository (private val tipoTecnicoDao: TipoTecnicoDao){
    suspend fun saveTecnico(tipoTecnico: TipoTecnicoEntity) = tipoTecnicoDao.save(tipoTecnico)

    suspend fun deleteTecnico(tipoTecnico: TipoTecnicoEntity) = tipoTecnicoDao.delete(tipoTecnico)

    suspend fun getTiposTecnicos(id: Int) = tipoTecnicoDao.find(id)

    fun getTiposTecnicos() = tipoTecnicoDao.getAll()
}