package com.ucne.registrodetecnicos.data.repository
import com.ucne.registrodetecnicos.data.local.dao.TecnicoDao
import com.ucne.registrodetecnicos.data.local.entities.TecnicoEntity

class TecnicoRepository (private val tecincoDao: TecnicoDao){
    suspend fun saveTecnico(tecnico: TecnicoEntity) = tecincoDao.save(tecnico)

    suspend fun deleteTecnico(tecnico: TecnicoEntity) = tecincoDao.delete(tecnico)

    suspend fun getTecnico(id: Int) = tecincoDao.find(id)

    fun getTecnicos() = tecincoDao.getAll()
}