package com.ucne.registrodetecnicos.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ucne.registrodetecnicos.data.local.dao.ServicioDao
import com.ucne.registrodetecnicos.data.local.dao.TecnicoDao
import com.ucne.registrodetecnicos.data.local.dao.TipoTecnicoDao
import com.ucne.registrodetecnicos.data.local.entities.ServicioEntity
import com.ucne.registrodetecnicos.data.local.entities.TecnicoEntity
import com.ucne.registrodetecnicos.data.local.entities.TipoTecnicoEntity

@Database(
    entities = [
        TecnicoEntity::class,
        TipoTecnicoEntity::class,
        ServicioEntity::class
    ],
    version = 6,
    exportSchema = false
)
abstract class TecnicoDb: RoomDatabase() {
    abstract fun tecnicoDao(): TecnicoDao
    abstract fun tipoTecnicoDao(): TipoTecnicoDao
    abstract fun servicioDao(): ServicioDao
}