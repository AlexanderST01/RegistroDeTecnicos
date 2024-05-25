package com.ucne.registrodetecnicos.presentation.Tecnico

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucne.registrodetecnicos.data.local.entities.TecnicoEntity
import com.ucne.registrodetecnicos.data.local.entities.TipoTecnicoEntity
import com.ucne.registrodetecnicos.presentation.components.FloatingButton
import com.ucne.registrodetecnicos.presentation.components.TopAppBar
import com.ucne.registrodetecnicos.ui.theme.RegistroDeTecnicosTheme


@Composable
fun TecnicoListScreen(
    viewModel: TecnicoViewModel,
    onVerTecnico: (TecnicoEntity) -> Unit,
    onAddTecnico: () -> Unit,
    openDrawer: () -> Unit,
) {
    val tecnicos by viewModel.tecnico.collectAsStateWithLifecycle()
    val tipo by viewModel.tipoTecnico.collectAsStateWithLifecycle()
    TecnicoListBody(
        tecnicos = tecnicos,
        tipo = tipo,
        onVerTecnico = onVerTecnico,
        onEliminarTecnico = { viewModel.deleteTecnico() },
        onAddTecnico = onAddTecnico,
        getTipoTecnicos = viewModel::getTipoTecnico,
        openDrawer = openDrawer
    )
}

@Composable
fun TecnicoListBody(
    tecnicos: List<TecnicoEntity>,
    onVerTecnico: (TecnicoEntity) -> Unit,
    openDrawer: () -> Unit,
    onEliminarTecnico: () -> Unit,
    onAddTecnico: () -> Unit,
    getTipoTecnicos: (Int?) -> String?,
    tipo: List<TipoTecnicoEntity>,
    ) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = "Lista de Técnicos",
                openDrawer = openDrawer
            )
        },
        floatingActionButton = {
            FloatingButton(onAddTecnico)
        }
    ) { innerPadding ->
        var showDeleteDialog by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(4.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(tecnicos) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onVerTecnico(item) }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = item.tecnicoId.toString(), modifier = Modifier.weight(0.10f))
                        Text(text = item.nombre.toString(), modifier = Modifier.weight(0.400f))
                        Text(text = item.sueldoHora.toString(), modifier = Modifier.weight(0.20f))
                        Text(text = getTipoTecnicos(item.tipo)?: "", modifier = Modifier.weight(0.40f))
                    }
                }
            }

        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = {
                    Text(text = "Eliminar Aporte")
                },
                text = {
                    Text("¿Estás seguro de que deseas eliminar este aporte?")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            onEliminarTecnico()
                            showDeleteDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Blue,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text(text = "Cancelar")
                    }
                }
            )
        }
    }
}

@Preview
@Composable
fun TecnicoListPreview() {
    val tecnico = listOf(
        TecnicoEntity(
            nombre = "Alexander",
            sueldoHora = 100.0
        )
    )
    RegistroDeTecnicosTheme() {
        TecnicoListBody(
            tecnicos = tecnico,
            onVerTecnico = {},
            openDrawer = {},
            onEliminarTecnico = {},
            onAddTecnico = {},
            getTipoTecnicos = {null},
            tipo = emptyList(),
        )
    }
}