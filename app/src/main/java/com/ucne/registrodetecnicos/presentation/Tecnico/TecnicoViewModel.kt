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
                started = SharingStarted.Lazily,
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

    fun onTipoTecnicoChanged(tipoTecnico: String) {
        uiState.update {
            it.copy(tipoTecnico = tipoTecnico)
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
                        sueldoHora = tecnico.sueldoHora,
                        tipoTecnico = tecnico.tipo

                    )
                }
            }
        }
    }

    fun saveTecnico() {
        viewModelScope.launch {
            repository.saveTecnico(uiState.value.toEntity())
            uiState.value = TecnicoUIState()
        }
    }

    fun nombreNoRepetido(): Boolean{
        return !tecnico.value.any { it.nombre == uiState.value.nombre && it.tecnicoId != uiState.value.tecnicoId  }
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
    var nombreNoVacio: Boolean = false,
    var nombreNoSimbolos: Boolean = false,
    var sueldoHora: Double? = null,
    var sueldoNoIntroducido: Boolean = false,
    var tipoTecnico: String? = null,
    var tipoNoVacio: Boolean = false,
    var validos: Boolean = false
)

fun TecnicoUIState.toEntity() = TecnicoEntity(
    tecnicoId = tecnicoId,
    nombre = nombre,
    sueldoHora = sueldoHora,
    tipo = tipoTecnico
)