package com.ucne.registrodetecnicos.presentation.TipoTecnico


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ucne.registrodetecnicos.Screen
import com.ucne.registrodetecnicos.data.local.entities.TipoTecnicoEntity
import com.ucne.registrodetecnicos.presentation.Tecnico.TipoTecnicoViewModel
import com.ucne.registrodetecnicos.presentation.components.FloatingButton
import com.ucne.registrodetecnicos.presentation.components.TopAppBar
import com.ucne.registrodetecnicos.ui.theme.RegistroDeTecnicosTheme
import kotlinx.coroutines.launch


@Composable
fun TipoTecnicoListScreen(
    viewModel: TipoTecnicoViewModel,
    onVerTecnico: (TipoTecnicoEntity) -> Unit,
    onAddTecnico: () -> Unit,
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val tecnicos by viewModel.tipoTecnico.collectAsStateWithLifecycle()
    TipoTecnicoListBody(
        tipoTecnico = tecnicos,
        onVerTipoTecnico = onVerTecnico,
        onDeleteTipoTecnico = { viewModel.deleteTipoTecnico()},
        onAddTipoTecnico = onAddTecnico,
        navController = navController
    )
}
@Composable
fun TipoTecnicoListBody(
    tipoTecnico: List<TipoTecnicoEntity>,
    onVerTipoTecnico: (TipoTecnicoEntity) -> Unit,
    onDeleteTipoTecnico: () -> Unit,
    onAddTipoTecnico: () -> Unit,
    navController: NavHostController
) {
    val scope = rememberCoroutineScope()
    var drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet( Modifier.requiredWidth(220.dp)) {
                Text("Lista de tipo de Tecnicos", modifier = Modifier.padding(16.dp))
                Divider()
                NavigationDrawerItem(
                    label = { Text(text = "Lista de Técnicos") },
                    selected = false,
                    onClick = { navController.navigate(Screen.TecnicoList) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Tecnicos"
                        )
                    }
                )
                // ...other drawer items
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = "Lista de tipo de técnicos",
                    onDrawerClicked = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingButton(onAddTipoTecnico)
            }
        ) { innerPadding ->
                var tipoTecnicoElimando by remember { mutableStateOf(TipoTecnicoEntity()) }
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
                    items(tipoTecnico) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onVerTipoTecnico(item) }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = item.tipoTecnicoId.toString(), modifier = Modifier.weight(0.10f))
                            Text(text = item.descripcion.toString(), modifier = Modifier.weight(0.400f))
                        }
                    }
                }

            }

            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    title = {
                        Text(text = "Eliminar tipo de tecnico")
                    },
                    text = {
                        Text("¿Estás seguro de que deseas eliminar este tipo de tecnico?")
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                onDeleteTipoTecnico()
                                showDeleteDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Blue, // Cambia el color de fondo del bot贸n a azul
                                contentColor = Color.White // Cambia el color del contenido del bot贸n (texto e icono) a blanco
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
}
@Preview
@Composable
fun TecnicoListPreview() {
    val tecnico = listOf(
        TipoTecnicoEntity(
            descripcion = "Alexander",
        ),
        TipoTecnicoEntity(
            descripcion = "Alexander",
        )
    )
    RegistroDeTecnicosTheme() {
        TipoTecnicoListBody(
            tipoTecnico = tecnico,
            onVerTipoTecnico = {},
            onDeleteTipoTecnico = {},
            onAddTipoTecnico = {},
            navController = rememberNavController()
        )
    }
}