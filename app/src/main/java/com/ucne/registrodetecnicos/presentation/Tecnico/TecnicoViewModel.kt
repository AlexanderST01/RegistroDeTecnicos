package com.ucne.registrodetecnicos.presentation.Tecnico

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucne.registrodetecnicos.data.local.entities.TecnicoEntity
import com.ucne.registrodetecnicos.data.repository.TecnicoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class TecnicoViewModel(private val repository: TecnicoRepository, private val tecnicoId: Int) : ViewModel() {

    var uiState = MutableStateFlow(TecnicoUIState())
        private set

        val tecnico = repository.getTecnicos()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun onNombreChanged(nombre: String) {
        uiState.update {
            it.copy(nombre = nombre)
        }
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
                        sueldoHora = tecnico.sueldoHora
                    )
                }
            }
        }
    }

    fun saveTecnico() {
        viewModelScope.launch {
            if(tecnico.value.any { it.nombre == uiState.value.nombre && it.tecnicoId != tecnicoId}){
                repository.saveTecnico(uiState.value.toEntity())
                uiState.value = TecnicoUIState()
            }
        }
    }

    fun nombreNoRepetido(): Boolean{
        var nombreRepetido  = tecnico.value.any { it.nombre == uiState.value.nombre && it.tecnicoId != tecnicoId  }
        return nombreRepetido
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


}

data class TecnicoUIState(
    val tecnicoId: Int? = null,
    var nombre: String? = "",
    var nombreError: String? = null,
    var sueldoHora: Double? = null,
    var sueldoHoraError: String? = null,
)

fun TecnicoUIState.toEntity() = TecnicoEntity(
    tecnicoId = tecnicoId,
    nombre = nombre,
    sueldoHora = sueldoHora,
)