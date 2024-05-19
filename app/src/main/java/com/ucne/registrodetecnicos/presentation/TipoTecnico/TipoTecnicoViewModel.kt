package com.ucne.registrodetecnicos.presentation.Tecnico

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucne.registrodetecnicos.data.local.entities.TipoTecnicoEntity
import com.ucne.registrodetecnicos.data.repository.TipoTecnicoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class TipoTecnicoViewModel(private val repository: TipoTecnicoRepository, private val tipoTecnicoId: Int) : ViewModel() {

    var uiState = MutableStateFlow(TipoTecnicoUIState())
        private set

    val tipoTecnico = repository.getTiposTecnicos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun onDescripcionChanged(descripcion: String) {
        uiState.update {
            it.copy(descripcion = descripcion)
        }
    }

    init {
        viewModelScope.launch {
            val tipoTecnico = repository.getTiposTecnicos(tipoTecnicoId)

            tipoTecnico?.let {
                uiState.update {
                    it.copy(
                        tipoTecnicoId = tipoTecnico.tipoTecnicoId,
                        descripcion = tipoTecnico.descripcion?: "",
                    )
                }
            }
        }
    }

    fun saveTipoTecnico() {
        viewModelScope.launch {
                repository.saveTecnico(uiState.value.toEntity())
                uiState.value = TipoTecnicoUIState()
        }
    }

    fun newTipoTecnico() {
        viewModelScope.launch {
            uiState.value = TipoTecnicoUIState()
        }
    }

    fun deleteTipoTecnico() {
        viewModelScope.launch {
            repository.deleteTecnico(uiState.value.toEntity())
        }
    }
}

data class TipoTecnicoUIState(
    val tipoTecnicoId: Int? = null,
    var descripcion: String? = "",
    var descripcionError: String? = null,
)

fun TipoTecnicoUIState.toEntity() = TipoTecnicoEntity(
    tipoTecnicoId = tipoTecnicoId,
    descripcion = descripcion,
)