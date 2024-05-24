package com.ucne.registrodetecnicos.presentation.TipoTecnico


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ucne.registrodetecnicos.R
import com.ucne.registrodetecnicos.Screen
import com.ucne.registrodetecnicos.data.local.entities.TipoTecnicoEntity
import com.ucne.registrodetecnicos.presentation.Tecnico.TipoTecnicoUIState
import com.ucne.registrodetecnicos.presentation.Tecnico.TipoTecnicoViewModel
import com.ucne.registrodetecnicos.presentation.components.TopAppBar
import com.ucne.registrodetecnicos.ui.theme.RegistroDeTecnicosTheme
import kotlinx.coroutines.launch


@Composable
fun TipoTecnicoScreen(
    viewModel: TipoTecnicoViewModel,
    navController: NavHostController
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
        navController = navController
    )
}

@Composable
private fun TipoTecnicoBody(
    uiState: TipoTecnicoUIState,
    onSaveTecnico: () -> Boolean,

    navController: NavHostController,
    onDeleteTecnico: () -> Unit = {},
    onDescripcionChanged: (String) -> Unit,
    onNewTecnico: () -> Unit,
) {
    var descripcionVacia by remember { mutableStateOf(false) }
    var descripcionRepetida by remember { mutableStateOf(false) }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = "Tipo de técnicos",
                onDrawerClicked = {}
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
                        isError = descripcionVacia || descripcionRepetida,
                    )
                    if (descripcionVacia) {
                        Text(
                            text = "La descripción no puede estar vacia",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    if (descripcionRepetida) {
                        Text(
                            text = "La descripción  de \"${uiState.descripcion}\" ya existe",
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
                                descripcionVacia = false
                                descripcionRepetida = false
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
                                    navController.navigate(Screen.TipoTecnicoList)
                                } else {
                                    descripcionVacia = uiState.descripcionVacia
                                    descripcionRepetida = uiState.descripcionRepetida
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
                                navController.navigate(Screen.TipoTecnicoList)
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
            navController = rememberNavController(),
            onDescripcionChanged = {},
            onNewTecnico = {},
            onDeleteTecnico = {},
        )
    }
}
