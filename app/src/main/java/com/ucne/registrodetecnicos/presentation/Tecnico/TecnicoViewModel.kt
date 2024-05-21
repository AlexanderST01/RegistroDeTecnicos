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

    fun saveTecnico(): Boolean {

        viewModelScope.launch {
            if(Validar()){
                repository.saveTecnico(uiState.value.toEntity())
                uiState.value = TecnicoUIState()
            }
        }
        return uiState.value.validos
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
        uiState.value.nombreVacio = (uiState.value.nombre.isNullOrEmpty() || uiState.value.nombre?.isBlank() ?: false)
        uiState.value.sueldoNoIntroducido = ((uiState.value.sueldoHora ?: 0.0) <= 0.0)
        uiState.value.nombreConSimbolos = (uiState.value.nombre?.contains(Regex("[^a-zA-Z ]+")) ?: false)
        uiState.value.tipoVacio = (uiState.value.tipoTecnico.isNullOrEmpty() || uiState.value.tipoTecnico?.isBlank() ?: false)
        uiState.value.nombreRepetido = tecnico.value.any { it.nombre == uiState.value.nombre && it.tecnicoId != tecnicoId }
        uiState.update {
            it.copy( validos =  !uiState.value.nombreVacio &&
                                !uiState.value.sueldoNoIntroducido &&
                                !uiState.value.nombreConSimbolos &&
                                !uiState.value.tipoVacio &&
                                !uiState.value.nombreRepetido
            )
        }
        return uiState.value.validos
    }
}

data class TecnicoUIState(
    val tecnicoId: Int? = null,
    var nombre: String? = "",
    var nombreRepetido: Boolean = true,
    var nombreVacio: Boolean = false,
    var nombreConSimbolos: Boolean = false,
    var sueldoHora: Double? = null,
    var sueldoNoIntroducido: Boolean = false,
    var tipoTecnico: String? = null,
    var tipoVacio: Boolean = false,
    var validos: Boolean = false
)

fun TecnicoUIState.toEntity() = TecnicoEntity(
    tecnicoId = tecnicoId,
    nombre = nombre,
    sueldoHora = sueldoHora,
    tipo = tipoTecnico
)