package com.ucne.registrodetecnicos.data.repository
import com.ucne.registrodetecnicos.data.local.dao.TecnicoDao
import com.ucne.registrodetecnicos.data.local.entities.TecnicoEntity

class TecnicoRepository (private val ticketDao: TecnicoDao){
    suspend fun saveTecnico(tecnico: TecnicoEntity) = ticketDao.save(tecnico)

    fun getTickets() = ticketDao.getAll()
}