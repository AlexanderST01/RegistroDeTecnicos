package com.ucne.registrodetecnicos.presentation.Tecnico

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucne.registrodetecnicos.data.local.entities.TecnicoEntity
import com.ucne.registrodetecnicos.data.repository.TecnicoRepository
import com.ucne.registrodetecnicos.data.repository.TipoTecnicoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class TecnicoViewModel(
    private val repository: TecnicoRepository,
    private val tecnicoId: Int,
    private val tipoRepository: TipoTecnicoRepository
) : ViewModel() {

    var uiState = MutableStateFlow(TecnicoUIState())
        private set

    val tecnico = repository.getTecnicos()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val tipoTecnico = tipoRepository.getTiposTecnicos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    fun onNombreChanged(nombre: String) {
        uiState.update {
            it.copy(nombre = nombre)
        }
    }
    fun onTipoTecnicoChanged(tipoTecnico: Int) {
        uiState.update {
            it.copy(tipoTecnico = tipoTecnico)
        }
    }
    fun getTipoTecnico(tipoId: Int?): String {
        val tipo = tipoTecnico.value.find { it.tipoId == tipoId }
        return  tipo?.descripcion?: ""
    }
    fun onSueldoHoraChanged(sueldoHora: String) {
        val letras = Regex("[a-zA-Z ]+")
        val numeros= sueldoHora.replace(letras, "").toDouble()
        uiState.update {
            it.copy(sueldoHora = numeros)
        }
    }
    init {
        viewModelScope.launch {
            val tecnico = repository.getTecnico(tecnicoId)

            tecnico?.let {
                uiState.update {
                    it.copy(
                        tecnicoId = tecnico.tecnicoId,
                        nombre = tecnico.nombre?: "",
                        sueldoHora = tecnico.sueldoHora,
                        tipoTecnico = tecnico.tipo

                    )
                }
            }
        }
    }

    fun saveTecnico(): Boolean {
        viewModelScope.launch {
            if(validar()){
                repository.saveTecnico(uiState.value.toEntity())
                uiState.value = TecnicoUIState()
            }
        }
        return validar()
    }

    fun newTecnico() {
        viewModelScope.launch {
            uiState.value = TecnicoUIState()
        }
    }

    fun deleteTecnico() {
        viewModelScope.launch {
            repository.deleteTecnico(uiState.value.toEntity())
        }
    }

    private fun validar(): Boolean {
        val nombreVacio = (uiState.value.nombre.isEmpty())
        val sueldoNoIntroducido = ((uiState.value.sueldoHora ?: 0.0) <= 0.0)
        val nombreConSimbolos = (uiState.value.nombre.contains(Regex("[^a-zA-Z ]+")) )
        val tipoNoIntroducido = (uiState.value.tipoTecnico == null) || (uiState.value.tipoTecnico == 0)
        val nombreRepetido = tecnico.value.any { it.nombre == uiState.value.nombre && it.tecnicoId != tecnicoId }

        val nombreError = when{
            nombreVacio -> "El nombre no puede estar vacio"
            nombreRepetido -> "El nombre ${uiState.value.nombre} ya existe"
            nombreConSimbolos -> "El nombre no puede contener simbolos"
            else -> null
        }

        val sueldoError = when{
            sueldoNoIntroducido -> "Debe de ingresar un sueldo"
            else -> null
        }

        val tipoTecnicoError = when{
            tipoNoIntroducido -> "Debe de seleccionar un tipo de tecnico"
            else -> null
        }

        uiState.update {
            it.copy( nombreError = nombreError, sueldoError = sueldoError, tipoTecnicoError = tipoTecnicoError)
        }

        return !nombreVacio && !sueldoNoIntroducido && !nombreConSimbolos && !tipoNoIntroducido && !nombreRepetido
    }
}

data class TecnicoUIState(
    val tecnicoId: Int? = null,
    var nombre: String = "",
    var nombreError: String? = null,
    var sueldoHora: Double? = null,
    var sueldoError: String? = null,
    var tipoTecnico: Int? = null,
    var tipoTecnicoError: String? = null,
)

fun TecnicoUIState.toEntity() = TecnicoEntity(
    tecnicoId = tecnicoId,
    nombre = nombre,
    sueldoHora = sueldoHora,
    tipo = tipoTecnico
)