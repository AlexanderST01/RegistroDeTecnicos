package com.ucne.registrodetecnicos.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    object TecnicoList : Screen()

    @Serializable
    data class Tecnico(val tecnicoId: Int) : Screen()

    @Serializable
    class TipoTecnico(val tipoId: Int) : Screen()

    @Serializable
    object TipoTecnicoList : Screen()


}