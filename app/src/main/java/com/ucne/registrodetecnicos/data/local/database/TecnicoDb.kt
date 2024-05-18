package com.ucne.registrodetecnicos.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ucne.registrodetecnicos.data.local.dao.TecnicoDao
import com.ucne.registrodetecnicos.data.local.dao.TipoTecnicoDao
import com.ucne.registrodetecnicos.data.local.entities.TecnicoEntity

@Database(
    entities = [
        TecnicoEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class TecnicoDb: RoomDatabase() {
    abstract fun tecnicoDao(): TecnicoDao
}