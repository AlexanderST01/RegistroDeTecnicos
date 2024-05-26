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
            if(Validar()){
                repository.saveTecnico(uiState.value.toEntity())
                uiState.value = TecnicoUIState()
            }
        }
        return Validar()
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

    fun Validar(): Boolean {
        val nombrevacio = (uiState.value.nombre.isNullOrEmpty() || uiState.value.nombre?.isBlank() ?: false)
        val sueldoNoIntroducido = ((uiState.value.sueldoHora ?: 0.0) <= 0.0)
        val nombreConSimbolos = (uiState.value.nombre?.contains(Regex("[^a-zA-Z ]+")) ?: false)
        val tipoNoIntroducido = (uiState.value.tipoTecnico == null) || (uiState.value.tipoTecnico == 0)
        val nombreRepetido = tecnico.value.any { it.nombre == uiState.value.nombre && it.tecnicoId != tecnicoId }
        if(nombrevacio){
            uiState.update {
                it.copy( nombreError = "El nombre no puede estar vacio")
            }
        }
        else{
            uiState.update {
                it.copy( nombreError = null)
            }
        }
        if(sueldoNoIntroducido){
            uiState.update {
                it.copy( sueldoError = "Debe de ingresar un sueldo")
            }
        }
        else{
            uiState.update {
                it.copy( sueldoError = null)
            }
        }
        if(nombreConSimbolos){
            uiState.update {
                it.copy( nombreError = "El nombre no puede contener simbolos")
            }
        }
        else{
            uiState.update {
                it.copy( nombreError = null)
            }
        }
        if(tipoNoIntroducido){
            uiState.update {
                it.copy( tipoTecnicoError = "Debe de seleccionar un tipo de tecnico")
            }
        }
        else{
            uiState.update {
                it.copy( tipoTecnicoError = null)
            }
        }
        if(nombreRepetido){
            uiState.update {
                it.copy( nombreError = "El nombre \"${it.nombre}\" ya existe")
            }
        }
        else{
            uiState.update {
                it.copy( nombreError = null)
            }
        }
        return !nombrevacio && !sueldoNoIntroducido && !nombreConSimbolos && !tipoNoIntroducido && !nombreRepetido
    }
}

data class TecnicoUIState(
    val tecnicoId: Int? = null,
    var nombre: String? = "",
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