package com.ucne.registrodetecnicos.presentation.Servicio

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import com.ucne.registrodetecnicos.data.local.entities.ServicioEntity
import com.ucne.registrodetecnicos.data.local.entities.TecnicoEntity
import com.ucne.registrodetecnicos.presentation.components.FloatingButton
import com.ucne.registrodetecnicos.presentation.components.TopAppBar
import com.ucne.registrodetecnicos.ui.theme.RegistroDeTecnicosTheme


@Composable
fun ServicioListScreen(
    viewModel: ServicioViewModel,
    onVerServicio: (ServicioEntity) -> Unit,
    onAddServicio: () -> Unit,
    openDrawer: () -> Unit,
) {
    val servicios by viewModel.servicio.collectAsStateWithLifecycle()
    val tecnicos by viewModel.tecnicos.collectAsStateWithLifecycle()
    ServicioListBody(
        servicios = servicios,
        tecnicos = tecnicos,
        nombreTecnico = viewModel::onGetNombreTecnico,
        onVerServicio = onVerServicio,
        onEliminarServicio = { viewModel.deleteServicio() },
        onAddServicio = onAddServicio,
        openDrawer = openDrawer
    )
}

@Composable
fun ServicioListBody(
    servicios: List<ServicioEntity>,
    onVerServicio: (ServicioEntity) -> Unit,
    openDrawer: () -> Unit,
    onEliminarServicio: () -> Unit,
    onAddServicio: () -> Unit,
    nombreTecnico: (Int?) -> String,
    tecnicos: List<TecnicoEntity>,
    ) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = "Lista de Servicios",
                openDrawer = openDrawer
            )
        },
        floatingActionButton = {
            FloatingButton(onAddServicio)
        }
    ) { innerPadding ->
        var showDitailsDialog by remember { mutableStateOf(false) }
        var selectedItem by remember { mutableStateOf<ServicioEntity?>(null) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text ="Id", modifier = Modifier.weight(0.10f))
                Text(text = "Servicio", modifier = Modifier.weight(0.300f))
                Text(text = "Cliente", modifier = Modifier.weight(0.30f))
                Text(text = "Total", modifier = Modifier.weight(0.20f))
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(servicios) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedItem = item
                                showDitailsDialog = true
                            }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(text = item.servicioId.toString(), modifier = Modifier.weight(0.10f))
                        Text(text = item.descripcion?: "", modifier = Modifier.weight(0.300f))
                        Text(text = nombreTecnico(item.tecnicoId), modifier = Modifier.weight(0.30f))
                        Text(text = item.total.toString(), modifier = Modifier.weight(0.20f))
                    }
                }
            }

        }

        if (showDitailsDialog) {
            AlertDialog(
                onDismissRequest = { showDitailsDialog = false },
                title = {
                    Text(text = "Sevicio seleccionado")
                },
                text = {
                    Column{
                        Row{
                            Text("Fecha: ")
                            Text("${selectedItem?.fecha}")
                        }
                        Row{
                            Text("Cliente: ")
                            Text("${selectedItem?.cliente}")
                        }
                        Row{
                            Text("Tecnico: ")
                            Text(nombreTecnico(selectedItem?.tecnicoId))
                        }
                        Row{
                            Text("Descripción del servicio: ")
                            Text("${selectedItem?.descripcion}")
                        }
                        Row{
                            Text("Total: ")
                            Text(selectedItem?.total.toString())
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            onVerServicio(selectedItem?: ServicioEntity())
                            showDitailsDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Blue,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Editar")
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDitailsDialog = false }) {
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
    val servicios = listOf(
        ServicioEntity(
            servicioId = 1,
            descripcion = "Servicio 1",
            total = 100.0,
            tecnicoId = 1,
            cliente = "Cliente 1"
        )
    )
    val tecnicos = listOf(
        TecnicoEntity(
            tecnicoId = 1,
            nombre = "Ramon",
            sueldoHora = 99.9,
            tipo = 1
        )
    )

    RegistroDeTecnicosTheme() {
        ServicioListBody(
            servicios = servicios,
            onVerServicio = {},
            openDrawer = {},
            onEliminarServicio = {},
            onAddServicio = {},
            nombreTecnico = { "Romano" },
            tecnicos = tecnicos,
        )
    }
}