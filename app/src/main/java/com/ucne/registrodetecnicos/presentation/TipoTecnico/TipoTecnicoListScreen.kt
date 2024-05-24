package com.ucne.registrodetecnicos.presentation.TipoTecnico


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
import com.ucne.registrodetecnicos.data.local.entities.TipoTecnicoEntity
import com.ucne.registrodetecnicos.presentation.Tecnico.TipoTecnicoViewModel
import com.ucne.registrodetecnicos.presentation.components.FloatingButton
import com.ucne.registrodetecnicos.presentation.components.TopAppBar
import com.ucne.registrodetecnicos.ui.theme.RegistroDeTecnicosTheme
import kotlinx.coroutines.Job


@Composable
fun TipoTecnicoListScreen(
    viewModel: TipoTecnicoViewModel,
    onVerTecnico: (TipoTecnicoEntity) -> Unit,
    onAddTecnico: () -> Unit,
    openDrawer: () -> Unit,
) {
    val tecnicos by viewModel.tipoTecnico.collectAsStateWithLifecycle()
    TipoTecnicoListBody(
        tipoTecnico = tecnicos,
        onVerTipoTecnico = onVerTecnico,
        onDeleteTipoTecnico = { viewModel.deleteTipoTecnico() },
        onAddTipoTecnico = onAddTecnico,
        openDrawer = openDrawer
    )
}

@Composable
fun TipoTecnicoListBody(
    tipoTecnico: List<TipoTecnicoEntity>,
    openDrawer: () -> Unit,
    onVerTipoTecnico: (TipoTecnicoEntity) -> Unit,
    onDeleteTipoTecnico: () -> Unit,
    onAddTipoTecnico: () -> Unit,
) {
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(
            title = "Lista de tipo de técnicos",
            openDrawer = openDrawer
        )
    }, floatingActionButton = {
        FloatingButton(onAddTipoTecnico)
    }) { innerPadding ->
        var showDeleteDialog by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(4.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(tipoTecnico) { item ->
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onVerTipoTecnico(item) }
                        .padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = item.tipoId.toString(), modifier = Modifier.weight(0.10f)
                        )
                        Text(text = item.descripcion.toString(), modifier = Modifier.weight(0.400f))
                    }
                }
            }

        }

        if (showDeleteDialog) {
            AlertDialog(onDismissRequest = { showDeleteDialog = false }, title = {
                Text(text = "Eliminar tipo de tecnico")
            }, text = {
                Text("¿Estás seguro de que deseas eliminar este tipo de tecnico?")
            }, confirmButton = {
                Button(
                    onClick = {
                        onDeleteTipoTecnico()
                        showDeleteDialog = false
                    }, colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "OK")
                }
            }, dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(text = "Cancelar")
                }
            })
        }
    }
}


@Preview
@Composable
fun TecnicoListPreview() {
    val tecnico = listOf(
        TipoTecnicoEntity(
            descripcion = "Alexander",
        ), TipoTecnicoEntity(
            descripcion = "Alexander",
        )
    )
    RegistroDeTecnicosTheme() {
        TipoTecnicoListBody(
            tipoTecnico = tecnico,
            onVerTipoTecnico = {},
            onDeleteTipoTecnico = {},
            onAddTipoTecnico = {},
            openDrawer = {}
        )
    }
}