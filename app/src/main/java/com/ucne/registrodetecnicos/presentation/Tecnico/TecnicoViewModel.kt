package com.ucne.registrodetecnicos.presentation.Tecnico

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucne.registrodetecnicos.data.local.entities.TecnicoEntity
import com.ucne.registrodetecnicos.data.repository.TecnicoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
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
    fun onSueldoHoraChanged(sueldoHora: Double) {
        uiState.update {
            it.copy(sueldoHora = sueldoHora)
        }
    }

    init {
        viewModelScope.launch {
            val tecnico = repository.getTecnico(tecnicoId)

            tecnico?.let {
                uiState.update {
                    it.copy(
                        tecnicoId = tecnico.tecnicoId?: 0,
                        nombre = tecnico.nombre,
                        sueldoHora = tecnico.sueldoHora?: 0.0
                    )
                }
            }
        }
    }

    fun saveTecnico() {
        viewModelScope.launch {
            repository.saveTecnico(uiState.value.toEntity())
        }
    }

    fun deleteTecnico(tecnico: TecnicoEntity) {
        viewModelScope.launch {
            repository.deleteTecnico(tecnico)
        }
    }


}

data class TecnicoUIState(
    val tecnicoId: Int = 0,
    var nombre: String = "",
    var nombreError: String? = null,
    var sueldoHora: Double = 0.0,
    var sueldoHoraError: String? = null,
)

fun TecnicoUIState.toEntity() = TecnicoEntity(
    tecnicoId = tecnicoId,
    nombre = nombre,
    sueldoHora = sueldoHora,
)