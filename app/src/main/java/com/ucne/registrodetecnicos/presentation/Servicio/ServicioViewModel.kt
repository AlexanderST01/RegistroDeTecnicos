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
        return Validar()
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
        val descripcionVacia = uiState.value.descripcion.isNullOrEmpty() || uiState.value.descripcion?.isBlank() ?: false
        val totalVacio = (uiState.value.total ?: 0.0) <= 0.0
        val tecnicoVacio = uiState.value.tecnico == null || uiState.value.tecnico == 0
        val clienteVacio = uiState.value.cliente.isNullOrEmpty() || uiState.value.cliente?.isBlank() ?: false
        val clienteConSimbolos = (uiState.value.cliente?.contains(Regex("[^a-zA-Z ]+")) ?: false)
        if (descripcionVacia) {
            uiState.update {
                it.copy(descripcionError = "Debes de ingresar una descripcion")
            }
        }
        else{
            uiState.update {
                it.copy(descripcionError = null)
            }
        }
        if(totalVacio){
            uiState.update {
                it.copy(totalError = "Debes de ingresar un total")
            }
        }
        else{
            uiState.update {
                it.copy(totalError = null)
            }
        }
        if(tecnicoVacio){
            uiState.update {
                it.copy(tecnicoError = "Debes de elegir un tecnico")
            }
        }
        else{
            uiState.update {
                it.copy(tecnicoError = null)
            }
        }
        if(clienteVacio){
            uiState.update {
                it.copy(clienteError = "Debes de ingresar un cliente")
            }
        }
        else{
            uiState.update {
                it.copy(clienteError = null)
            }
        }
        if(clienteConSimbolos){
            uiState.update {
                it.copy(clienteError = "No se permiten simbolos")
            }
        }
        else{
            uiState.update {
                it.copy(clienteError = null)
            }
        }

        return !descripcionVacia && !totalVacio && !tecnicoVacio && !clienteVacio && !clienteConSimbolos
    }
}

data class ServicioUIState(
    val servicioId: Int? = null,
    val descripcion: String? = "",
    var descripcionError: String? = null,
    val fecha: String = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
    val total: Double? = null,
    var totalError: String? = null,
    val cliente: String? = null,
    var clienteError: String? = null,
    val tecnico: Int? = null,
    var tecnicoError: String? = null,
)

fun ServicioUIState.toEntity() = ServicioEntity(
    servicioId = servicioId,
    descripcion = descripcion,
    fecha = fecha,
    tecnicoId = tecnico,
    total = total,
    cliente = cliente
)