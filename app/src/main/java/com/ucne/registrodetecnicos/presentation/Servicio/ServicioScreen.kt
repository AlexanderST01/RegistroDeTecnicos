package com.ucne.registrodetecnicos.presentation.Servicio

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucne.registrodetecnicos.data.local.entities.TecnicoEntity
import com.ucne.registrodetecnicos.presentation.components.Combobox
import com.ucne.registrodetecnicos.presentation.components.TopAppBar
import com.ucne.registrodetecnicos.ui.theme.RegistroDeTecnicosTheme
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.reflect.KFunction1


@Composable
fun ServicioScreen(
    viewModel: ServicioViewModel,
    goToServicioList: () -> Unit,
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
        goToTecnicoList = goToServicioList,
        onNewTecnico = {
            viewModel.newServicio()
        },
        onTecnioChanged = viewModel::onTecnicoChanged,
        onDescripcionChanged = viewModel::onDescripcionChanged,
        onTotalChanged = viewModel::onTotalChanged,
        onClienteChanged = viewModel::onClienteChanged,
        onFechaChanged = viewModel::onFechaChanged,
        tecnicos = tecnicos,
        openDrawer = openDrawer
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ServicioBody(
    uiState: ServicioUIState,
    onSaveTecnico: () -> Boolean,
    openDrawer: () -> Unit,
    goToTecnicoList: () -> Unit,
    onDeleteTecnico: () -> Unit = {},
    onDescripcionChanged: (String) -> Unit,
    onClienteChanged: (String) -> Unit,
    tecnicos: List<TecnicoEntity>,
    onTotalChanged: (String) -> Unit,
    onNewTecnico: () -> Unit,
    onTecnioChanged: (Int) -> Unit,
    onFechaChanged: (String) -> Unit,
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val unDia = 86400000

    val state = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis <= System.currentTimeMillis() - unDia
        }
    })

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
                        label = { Text(text = "Fecha") },
                        value = uiState.fecha.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")),
                        readOnly = true,
                        onValueChange = { },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    showDatePicker = true
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Date Picker"
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()

                            .clickable(enabled = true) {
                                showDatePicker = true
                            }
                    )
                    OutlinedTextField(
                            label = { Text(text = "Cliente") },
                    value = uiState.cliente ?: "",
                    onValueChange = onClienteChanged,
                    modifier = Modifier.fillMaxWidth(),
                    isError = uiState.clienteError != null
                    )
                    if (uiState.clienteError != null) {
                        Text(
                            text = uiState.clienteError ?: "",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    OutlinedTextField(
                        label = { Text(text = "Descripción") },
                        value = uiState.descripcion ?: "",
                        onValueChange = onDescripcionChanged,
                        modifier = Modifier.fillMaxWidth(),
                        isError = uiState.descripcionError != null
                    )
                    if (uiState.descripcionError != null) {
                        Text(
                            text = uiState.descripcionError ?: "",
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
                        isError = uiState.totalError != null
                    )
                    if (uiState.totalError != null) {
                        Text(
                            text = uiState.totalError ?: "",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    var selectedItem by remember { mutableStateOf<TecnicoEntity?>(null) }
                    Combobox(
                        items = tecnicos,
                        label = "Tecnicos",
                        selectedItemString = {
                            it?.let {
                                "${it.nombre}"
                            } ?: ""
                        },
                        selectedItem = selectedItem,
                        onItemSelected = {
                            onTecnioChanged(it?.tecnicoId ?: 0)
                            selectedItem = it
                        },
                        itemTemplate = { Text(text = it.nombre ?: "") },
                        isErrored = uiState.tecnicoError != null,
                        itemToId = {it.tecnicoId},
                        selectedItemId = uiState.tecnico
                    )
                    if (uiState.tecnicoError != null) {
                        Text(
                            text = uiState.tecnicoError ?: "",
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    Spacer(modifier = Modifier.padding(2.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        OutlinedButton(onClick = onNewTecnico) {
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
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                Button(
                    onClick = {
                        onFechaChanged( state.selectedDateMillis?.let {
                                Instant.ofEpochMilli(it).atZone(
                                    ZoneId.of("UTC")
                                ).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                            }.toString()
                        )
                        showDatePicker = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Aceptar")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDatePicker = false }) {
                    Text(text = "Cancelar")
                }
            },
        )
        {
            DatePicker(state)
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
            openDrawer = {},
            goToTecnicoList = {},
            onDeleteTecnico = {},
            onDescripcionChanged = {},
            onClienteChanged = {},
            tecnicos = emptyList(),
            onTotalChanged = {},
            onNewTecnico = {},
            onTecnioChanged = {},
            onFechaChanged = {}
        )
    }
}
