package com.ucne.registrodetecnicos.presentation.TipoTecnico


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucne.registrodetecnicos.presentation.Tecnico.TipoTecnicoUIState
import com.ucne.registrodetecnicos.presentation.Tecnico.TipoTecnicoViewModel
import com.ucne.registrodetecnicos.presentation.components.TopAppBar
import com.ucne.registrodetecnicos.ui.theme.RegistroDeTecnicosTheme


@Composable
fun TipoTecnicoScreen(
    viewModel: TipoTecnicoViewModel,
    goToTipoTecnicoList: () -> Unit,
    openDrawer: () -> Unit,

    ) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    viewModel.tipoTecnico.collectAsStateWithLifecycle()
    TipoTecnicoBody(
        uiState = uiState,
        onSaveTecnico = {
            viewModel.saveTipoTecnico()
        },
        onDeleteTecnico = {
            viewModel.deleteTipoTecnico()
        },
        onNewTecnico = {
            viewModel.newTipoTecnico()
        },
        onDescripcionChanged = viewModel::onDescripcionChanged,
        goToTipoTecnicoList = goToTipoTecnicoList,
        openDrawer = openDrawer
    )
}

@Composable
private fun TipoTecnicoBody(
    uiState: TipoTecnicoUIState,
    onSaveTecnico: () -> Boolean,
    openDrawer: () -> Unit,
    onDeleteTecnico: () -> Unit = {},
    onDescripcionChanged: (String) -> Unit,
    onNewTecnico: () -> Unit,
    goToTipoTecnicoList: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = "Tipo de técnicos",
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
                        value = uiState.descripcion ?: "",
                        onValueChange = onDescripcionChanged,
                        modifier = Modifier.fillMaxWidth(),
                        isError = uiState.descripcionError != null,
                    )
                    if (uiState.descripcionError != null) {
                        Text(
                            text = uiState.descripcionError?: "",
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
                                if (onSaveTecnico()){
                                    goToTipoTecnicoList()
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
                                goToTipoTecnicoList()
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
private fun TipoTecnicoPreview() {
    RegistroDeTecnicosTheme() {
        TipoTecnicoBody(
            uiState = TipoTecnicoUIState(),
            onSaveTecnico = { true },
            onDescripcionChanged = {},
            onNewTecnico = {},
            onDeleteTecnico = {},
            goToTipoTecnicoList = {},
            openDrawer = {}
        )
    }
}
