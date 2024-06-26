package com.ucne.registrodetecnicos.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TiposTecnico")
data class TipoTecnicoEntity(
    @PrimaryKey
    val tipoId: Int? = null,
    var descripcion: String? = null,
)
