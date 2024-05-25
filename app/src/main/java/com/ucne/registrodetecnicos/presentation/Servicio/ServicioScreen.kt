package com.ucne.registrodetecnicos.presentation.Servicio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucne.registrodetecnicos.data.local.entities.TecnicoEntity
import com.ucne.registrodetecnicos.presentation.components.Combobox
import com.ucne.registrodetecnicos.presentation.components.TopAppBar
import com.ucne.registrodetecnicos.ui.theme.RegistroDeTecnicosTheme

@Composable
fun ServicioScreen(
    viewModel: ServicioViewModel,
    goToTecnicoList: () -> Unit,
    openDrawer: () -> Unit
) {
    val tecnicos by viewModel.tecnicos.collectAsStateWithLifecycle()
    val Servicio by viewModel.servicio.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ServicioBody(
        uiState = uiState,
        onSaveTecnico = {
            viewModel.saveServicio()
        },
        onDeleteTecnico = {
            viewModel.deleteServicio()
        },
        goToTecnicoList = goToTecnicoList,
        onNewTecnico = {
            viewModel.newServicio()
        },
        onTecnioChanged = viewModel::onTecnicoChanged,
        onDescripcionChanged = viewModel::onDescripcionChanged,
        onTotalChanged = viewModel::onTotalChanged,
        tecnicos = tecnicos,
        openDrawer = openDrawer
    )
}

@Composable
private fun ServicioBody(
    uiState: ServicioUIState,
    onSaveTecnico: () -> Boolean,
    openDrawer: () -> Unit,
    goToTecnicoList: () -> Unit,
    onDeleteTecnico: () -> Unit = {},
    onDescripcionChanged: (String) -> Unit,
    tecnicos: List<TecnicoEntity>,
    onTotalChanged: (String) -> Unit,
    onNewTecnico: () -> Unit,
    onTecnioChanged: (Int) -> Unit,
) {
    var descripcionVacio by remember { mutableStateOf(false) }
    var sueldoNoIntroducido by remember { mutableStateOf(false) }
    var nombreConSimbolos by remember { mutableStateOf(false) }
    var nombreRepetido by remember { mutableStateOf(false) }
    var tecnicoVacio by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = "Servicio",
                openDrawer = openDrawer
            )
        }
    )
    { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(4.dp)
        ) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    OutlinedTextField(
                        label = { Text(text = "Descripción") },
                        value = uiState.descripcion ?: "",
                        onValueChange = onDescripcionChanged,
                        modifier = Modifier.fillMaxWidth(),
                        isError = descripcionVacio || nombreConSimbolos || nombreRepetido
                    )
                    if (descripcionVacio) {
                        Text(
                            text = "La descripción no puede estar vacio",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    if (nombreConSimbolos) {
                        Text(
                            text = "La descripción no puede contener simbolos o números",
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    Spacer(modifier = Modifier.padding(2.dp))
                    OutlinedTextField(
                        label = { Text(text = "Total") },
                        value = uiState.total.toString().replace("null", ""),
                        onValueChange = onTotalChanged,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        isError = sueldoNoIntroducido
                    )
                    if (sueldoNoIntroducido) {
                        Text(
                            text = "Debes de introducir un Total",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    var selectedItem by remember { mutableStateOf<TecnicoEntity?>(null) }
                    Combobox(
                        items = tecnicos,
                        label = "Tecnicos",
                        selectedItemString = {
                            it?.let {
                                "${it.tecnicoId}"
                            } ?: ""
                        },
                        selectedItem = selectedItem,
                        onItemSelected = {
                            onTecnioChanged(it?.tecnicoId ?: 0)
                            selectedItem = it
                            uiState.tecnico = it?.tecnicoId
                        },
                        itemTemplate = { Text(text = it.nombre ?: "") },
                        isErrored = tecnicoVacio
                    )
                    if (tecnicoVacio) {
                        Text(
                            text = "Debes de introducir un tipo de técnico",
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    Spacer(modifier = Modifier.padding(2.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        OutlinedButton(
                            onClick = {
                                onNewTecnico()
                                descripcionVacio = false
                                sueldoNoIntroducido = false
                                nombreConSimbolos = false
                                nombreRepetido = false
                                tecnicoVacio = false
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "new button"
                            )
                            Text(text = "Nuevo")
                        }
                        OutlinedButton(
                            onClick = {

                                if (onSaveTecnico()) {
                                    goToTecnicoList()
                                } else {
                                    descripcionVacio = uiState.descripcionVacia
                                    sueldoNoIntroducido = uiState.totalNoIntroducido
                                    tecnicoVacio = uiState.tecnicoVacio
                                }

                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "save button"
                            )
                            Text(text = "Guardar")
                        }
                        OutlinedButton(
                            onClick = {
                                onDeleteTecnico()
                                goToTecnicoList()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete button"
                            )
                            Text(text = "Borrar")
                        }
                    }
                }
            }
        }
    }

}

@Preview
@Composable
private fun ServicioPreview() {
    RegistroDeTecnicosTheme() {
        ServicioBody(
            uiState = ServicioUIState(),
            onSaveTecnico = { true },
            onDeleteTecnico = {},
            onDescripcionChanged = {},
            tecnicos = emptyList(),
            onTotalChanged = {},
            onNewTecnico = {},
            onTecnioChanged = {},
            goToTecnicoList = {},
            openDrawer = {}
        )
    }
}
