package com.ucne.registrodetecnicos.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TipoTecnico")
data class TipoTecnicoEntity(
    @PrimaryKey
    val tipoTecnicoId: Int? = null,
    var descripcion: String? = null,
)