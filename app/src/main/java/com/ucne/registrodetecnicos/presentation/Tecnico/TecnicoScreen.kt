package com.ucne.registrodetecnicos.presentation.Tecnico

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
import androidx.navigation.NavHostController
import com.ucne.registrodetecnicos.data.local.entities.TipoTecnicoEntity
import com.ucne.registrodetecnicos.presentation.components.Combobox
import com.ucne.registrodetecnicos.presentation.components.TopAppBar
import com.ucne.registrodetecnicos.ui.theme.RegistroDeTecnicosTheme

@Composable
fun TecnicoScreen(
    viewModel: TecnicoViewModel,
    navController: NavHostController,
    goToTecnicoList: () -> Unit,
    openDrawer: () -> Unit
) {
    val tipotTecnicos by viewModel.tipoTecnico.collectAsStateWithLifecycle()
    viewModel.tecnico.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TecnicoBody(
        uiState = uiState,
        onSaveTecnico = {
            viewModel.saveTecnico()
        },
        onDeleteTecnico = {
            viewModel.deleteTecnico()
        },
        goToTecnicoList = goToTecnicoList,
        onNewTecnico = {
            viewModel.newTecnico()
        },
        onTipoTecnioChanged = viewModel::onTipoTecnicoChanged,
        onNombreChanged = viewModel::onNombreChanged,
        onSueldoHoraChanged = viewModel::onSueldoHoraChanged,
        tiposTecnicos = tipotTecnicos,
        openDrawer = openDrawer
    )
}

@Composable
private fun TecnicoBody(
    uiState: TecnicoUIState,
    onSaveTecnico: () -> Boolean,
    openDrawer: () -> Unit,
    goToTecnicoList: () -> Unit,
    onDeleteTecnico: () -> Unit = {},
    onNombreChanged: (String) -> Unit,
    tiposTecnicos: List<TipoTecnicoEntity>,
    onSueldoHoraChanged: (String) -> Unit,
    onNewTecnico: () -> Unit,
    onTipoTecnioChanged: (Int) -> Unit,
) {
    var nombreVacio by remember { mutableStateOf(false) }
    var sueldoNoIntroducido by remember { mutableStateOf(false) }
    var nombreConSimbolos by remember { mutableStateOf(false) }
    var nombreRepetido by remember { mutableStateOf(false) }
    var tipoVacio by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = "Técnicos",
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
                        label = { Text(text = "Nombre") },
                        value = uiState.nombre ?: "",
                        onValueChange = onNombreChanged,
                        modifier = Modifier.fillMaxWidth(),
                        isError = nombreVacio || nombreConSimbolos || nombreRepetido
                    )
                    if (nombreVacio) {
                        Text(
                            text = "El nombre no puede estar vacio",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    if (nombreConSimbolos) {
                        Text(
                            text = "El nombre no puede contener simbolos o números",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    if (nombreRepetido) {
                        Text(
                            text = "El nombre de \"${uiState.nombre}\" ya existe",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    Spacer(modifier = Modifier.padding(2.dp))
                    OutlinedTextField(
                        label = { Text(text = "Sueldo por hora") },
                        value = uiState.sueldoHora.toString().replace("null", ""),
                        onValueChange = onSueldoHoraChanged,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        isError = sueldoNoIntroducido
                    )
                    if (sueldoNoIntroducido) {
                        Text(
                            text = "Debes de introducir un sueldo",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    var selectedItem by remember { mutableStateOf<TipoTecnicoEntity?>(null) }
                    Combobox(
                        items = tiposTecnicos,
                        label = "Tipo de tecnico",
                        selectedItemString = {
                            it?.let {
                                "${it.descripcion}"
                            } ?: ""
                        },
                        selectedItem = selectedItem,
                        onItemSelected = {
                            onTipoTecnioChanged(it?.tipoId ?: 0)
                            selectedItem = it
//                            uiState.tipoTecnico = it?.tipoId ?: 0
                        },
                        itemTemplate = { Text(text = it.descripcion ?: "") },
                        isErrored = tipoVacio
                    )
                    if (tipoVacio) {
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
                                nombreVacio = false
                                sueldoNoIntroducido = false
                                nombreConSimbolos = false
                                nombreRepetido = false
                                tipoVacio = false
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
                                    nombreVacio = uiState.nombreVacio
                                    sueldoNoIntroducido = uiState.sueldoNoIntroducido
                                    nombreConSimbolos = uiState.nombreConSimbolos
                                    nombreRepetido = uiState.nombreRepetido
                                    tipoVacio = uiState.tipoVacio
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
private fun TecnicoPreview() {
    RegistroDeTecnicosTheme() {
        TecnicoBody(
            uiState = TecnicoUIState(),
            onSaveTecnico = { true },
            onDeleteTecnico = {},
            onNombreChanged = {},
            tiposTecnicos = emptyList(),
            onSueldoHoraChanged = {},
            onNewTecnico = {},
            onTipoTecnioChanged = {},
            goToTecnicoList = {},
            openDrawer = {}
        )
    }
}
