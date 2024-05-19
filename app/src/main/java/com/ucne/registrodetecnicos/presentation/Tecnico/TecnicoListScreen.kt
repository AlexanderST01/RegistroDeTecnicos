package com.ucne.registrodetecnicos.presentation.Tecnico

import androidx.annotation.RestrictTo
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ucne.registrodetecnicos.R
import com.ucne.registrodetecnicos.Screen
import com.ucne.registrodetecnicos.data.local.entities.TecnicoEntity
import com.ucne.registrodetecnicos.presentation.components.FloatingButton
import com.ucne.registrodetecnicos.presentation.components.TopAppBar
import com.ucne.registrodetecnicos.ui.theme.RegistroDeTecnicosTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun TecnicoListScreen(
    viewModel: TecnicoViewModel,
    onVerTecnico: (TecnicoEntity) -> Unit,
    onAddTecnico: () -> Unit,
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val tecnicos by viewModel.tecnico.collectAsStateWithLifecycle()
    TecnicoListBody(
        tecnicos = tecnicos,
        onVerTecnico = onVerTecnico,
        onEliminarTecnico = { viewModel.deleteTecnico() },
        onAddTecnico = onAddTecnico,
        navController = navController
    )
}
@Composable
fun TecnicoListBody(
    tecnicos: List<TecnicoEntity>,
    onVerTecnico: (TecnicoEntity) -> Unit,
    onEliminarTecnico: () -> Unit,
    onAddTecnico: () -> Unit,
    navController: NavHostController
) {
    val scope = rememberCoroutineScope()
    var drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet( Modifier.requiredWidth(220.dp)) {
                Text("Lita de tipo de Técnicos", modifier = Modifier.padding(16.dp))
                Divider()
                NavigationDrawerItem(
                    label = { Text(text = "Lista de tipo de tecnicos") },
                    selected = false,
                    onClick = { navController.navigate(Screen.TipoTecnicoList) },
                    icon = {
                        Icon(painter =  painterResource(id = R.drawable.engineering)
                            , contentDescription = "Tecnicos")

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
                    title = "Lista de Técnicos",
                    onDrawerClicked = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingButton(onAddTecnico)
            }
        ) { innerPadding ->
                var tecnicoElimando by remember { mutableStateOf(TecnicoEntity()) }
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
                            Text(text = item.tipo.toString(), modifier = Modifier.weight(0.40f))
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
        TecnicoEntity(
            nombre = "Alexander",
            sueldoHora = 100.0
        )
    )
    RegistroDeTecnicosTheme() {
        TecnicoListBody(
            tecnicos = tecnico,
            onVerTecnico = {},
            onEliminarTecnico = {},
            onAddTecnico = {},
            navController = rememberNavController()
        )
    }
}