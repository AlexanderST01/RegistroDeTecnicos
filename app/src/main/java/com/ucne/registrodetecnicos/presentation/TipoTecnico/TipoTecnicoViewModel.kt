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


class TipoTecnicoViewModel(private val repository: TipoTecnicoRepository, private val tipoId: Int) : ViewModel() {

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
            val tipoTecnico = repository.getTiposTecnicos(tipoId)

            tipoTecnico?.let {
                uiState.update {
                    it.copy(
                        tipoId = tipoTecnico.tipoId,
                        descripcion = tipoTecnico.descripcion?: "",
                    )
                }
            }
        }
    }

    fun saveTipoTecnico(): Boolean {
        viewModelScope.launch {
            if (validar()){
                repository.saveTecnico(uiState.value.toEntity())
                uiState.value = TipoTecnicoUIState()
            }
        }
        return uiState.value.validado
    }

    fun validar(): Boolean{
        uiState.value.descripcionVacia = uiState.value.descripcion.isNullOrEmpty() && uiState.value.descripcion?.isBlank() ?: false
        uiState.value.descripcionRepetida = tipoTecnico.value.any { it.descripcion == uiState.value.descripcion && it.tipoId != tipoId }
        uiState.update {
            it.copy(validado = !uiState.value.descripcionVacia && !uiState.value.descripcionRepetida)
        }
        return uiState.value.validado
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
    val tipoId: Int? = null,
    var descripcion: String? = "",
    var descripcionVacia: Boolean = false,
    var descripcionRepetida: Boolean = true,
    var validado: Boolean = false
)

fun TipoTecnicoUIState.toEntity() = TipoTecnicoEntity(
    tipoId = tipoId,
    descripcion = descripcion,
)