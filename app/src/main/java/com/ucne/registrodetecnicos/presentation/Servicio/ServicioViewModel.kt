package com.ucne.registrodetecnicos.presentation.Servicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucne.registrodetecnicos.data.local.entities.ServicioEntity
import com.ucne.registrodetecnicos.data.repository.ServicioRepository
import com.ucne.registrodetecnicos.data.repository.TecnicoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class ServicioViewModel(
    private val servicioRepository: ServicioRepository,
    private val servicioId: Int,
    private val repository: TecnicoRepository
) : ViewModel() {

    var uiState = MutableStateFlow(ServicioUIState())
        private set

    val servicio = servicioRepository.getServicios()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val tecnicos = repository.getTecnicos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    fun onDescripcionChanged(descripcion: String) {
        uiState.update {
            it.copy(descripcion = descripcion)
        }
    }
    fun onTecnicoChanged(tecnico: Int) {
        uiState.update {
            it.copy(tecnico = tecnico)
        }
    }
    fun onFechaChanged(fecha: String) {
        uiState.update {
            it.copy(fecha = fecha)
        }
    }
    fun onClienteChanged(cliente: String) {
        uiState.update {
            it.copy(cliente = cliente)
        }
    }
    fun onTotalChanged(total: String) {
        val letras = Regex("[a-zA-Z ]+")
        val numeros= total.replace(letras, "").toDouble()
        uiState.update {
            it.copy(total = numeros)
        }
    }
    init {
        viewModelScope.launch {
            val servicio = servicioRepository.getServicio(servicioId)

            servicio?.let {
                uiState.update {
                    it.copy(
                        servicioId = servicio.servicioId,
                        descripcion = servicio.descripcion?: "",
                        total = servicio.total,
                        tecnico = servicio.tecnicoId,
                        fecha = servicio.fecha?:"",

                    )
                }
            }
        }
    }

    fun saveServicio(): Boolean {

        viewModelScope.launch {
            if(Validar()){
                servicioRepository.saveServicio(uiState.value.toEntity())
                uiState.value = ServicioUIState()
            }
        }
        return uiState.value.validos
    }

    fun newServicio() {
        viewModelScope.launch {
            uiState.value = ServicioUIState()
        }
    }

    fun deleteServicio() {
        viewModelScope.launch {
            servicioRepository.deleteServicio(uiState.value.toEntity())
        }
    }

    fun onGetNombreTecnico(id: Int?): String {
        val tecnico = tecnicos.value.find { it.tecnicoId == id }
        return tecnico?.nombre ?: ""
    }

    fun Validar(): Boolean {
        uiState.value.descripcionVacia = (uiState.value.descripcion.isNullOrEmpty() || uiState.value.descripcion?.isBlank() ?: false)
        uiState.value.totalNoIntroducido = ((uiState.value.total ?: 0.0) <= 0.0)
        uiState.value.tecnicoVacio =  uiState.value.tecnico == null || uiState.value.tecnico == 0
        uiState.update {
            it.copy( validos =
                    !uiState.value.descripcionVacia &&
                    !uiState.value.totalNoIntroducido &&
                    !uiState.value.tecnicoVacio
            )
        }
        return uiState.value.validos
    }
}

data class ServicioUIState(
    val servicioId: Int? = null,
    var descripcion: String? = "",
    var fecha: String = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
    var descripcionVacia: Boolean = false,
    var total: Double? = null,
    var cliente: String? = null,
    var totalNoIntroducido: Boolean = false,
    var tecnico: Int? = null,
    var tecnicoVacio: Boolean = false,
    var validos: Boolean = false
)

fun ServicioUIState.toEntity() = ServicioEntity(
    servicioId = servicioId,
    descripcion = descripcion,
    fecha = fecha,
    tecnicoId = tecnico,
    total = total,
    cliente = cliente
)