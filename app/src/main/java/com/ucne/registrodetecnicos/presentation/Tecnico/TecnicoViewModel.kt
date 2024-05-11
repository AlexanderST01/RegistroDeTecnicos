package com.ucne.registrodetecnicos.presentation.Tecnico

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucne.registrodetecnicos.data.local.entities.TecnicoEntity
import com.ucne.registrodetecnicos.data.repository.TecnicoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class TecnicoViewModel(private val repository: TecnicoRepository) : ViewModel() {

    val tecnico = repository.getTickets()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun saveTecnico(tecnico: TecnicoEntity) {
        viewModelScope.launch {
            repository.saveTecnico(tecnico)
        }
    }

    fun deleteTecnico(tecnico: TecnicoEntity) {
        viewModelScope.launch {
            repository.deleteTecnico(tecnico)
        }
    }




    companion object {
        fun provideFactory(
            repository: TecnicoRepository
        ): AbstractSavedStateViewModelFactory =
            object : AbstractSavedStateViewModelFactory() {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    key: String,
                    modelClass: Class<T>,
                    handle: SavedStateHandle
                ): T {
                    return TecnicoViewModel(repository) as T
                }
            }
    }
}