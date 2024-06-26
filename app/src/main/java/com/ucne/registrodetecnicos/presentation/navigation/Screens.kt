package com.ucne.registrodetecnicos.presentation.navigation

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

    @Serializable
    class Servicio(val servicioId: Int) : Screen()

    @Serializable
    object ServicioListScreen : Screen()
}