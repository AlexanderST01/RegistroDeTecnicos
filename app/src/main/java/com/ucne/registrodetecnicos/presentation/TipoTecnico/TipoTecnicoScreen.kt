package com.ucne.registrodetecnicos.presentation.TipoTecnico


import android.widget.Toast
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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


var descripcionNoVacia by  mutableStateOf(false)
var sueldoNoIntroducido by  mutableStateOf(false)
var descripcionnNoSimbolos by  mutableStateOf(false)
var descripcionRepetida by mutableStateOf(false)
@Composable
fun TipoTecnicoScreen(
    viewModel: TipoTecnicoViewModel,
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val tipoTecnios by viewModel.tipoTecnico.collectAsStateWithLifecycle()
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
        tipoTecnios = tipoTecnios,

        onDescripcionChanged =  viewModel::onDescripcionChanged,
        navController = navController
    )
}
@Composable
private fun TipoTecnicoBody(
    uiState: TipoTecnicoUIState,
    onSaveTecnico: () -> Unit,
    tipoTecnios: List<TipoTecnicoEntity>,
    navController: NavHostController,
    onDeleteTecnico: () -> Unit = {},
    onDescripcionChanged: (String) -> Unit,
    onNewTecnico: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    var drawerState = rememberDrawerState(DrawerValue.Closed)
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet( Modifier.requiredWidth(220.dp)) {
                Text("Registro de Tipo de Técnicos", modifier = Modifier.padding(16.dp))
                Divider()
                NavigationDrawerItem(
                    label = { Text(text = "Listas de tipo de técnicos") },
                    selected = false,
                    onClick = { navController.navigate(Screen.TipoTecnicoList) },
                    icon = {
                        Icon(painter =  painterResource(id = R.drawable.engineering)
                            , contentDescription = "Tecnicos")

                    }
                )
            }
        },
        drawerState = drawerState
    ) {

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = "Tipo de técnicos",
                    onDrawerClicked = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            }
        )
        {
                innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(4.dp)
            ){
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
                            value = uiState.descripcion?: "",
                            onValueChange = onDescripcionChanged,
                            modifier = Modifier.fillMaxWidth(),
                            isError = false
                        )
                        if(descripcionNoVacia){
                            Text(text = "La descripción no puede estar vacia", color = MaterialTheme.colorScheme.error)
                        }
                        if(descripcionnNoSimbolos){
                            Text(text = "La descripción no puede contener simbolos o números", color = MaterialTheme.colorScheme.error)
                        }
                        if(descripcionRepetida){
                            Text(text = "La descripción  de \"${uiState.descripcion}\" ya existe", color = MaterialTheme.colorScheme.error)
                        }
                        Spacer(modifier = Modifier.padding(2.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            OutlinedButton(
                                onClick = {
                                    onNewTecnico()
                                    descripcionNoVacia = false
                                    sueldoNoIntroducido = false
                                    descripcionnNoSimbolos = false
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
                                    if (Validar(
                                            TipoTecnicoEntity(
                                                tipoTecnicoId = uiState.tipoTecnicoId,
                                                descripcion = uiState.descripcion,
                                            ),
                                        tipoTecnios = tipoTecnios
                                        )
                                    ) {
                                        onSaveTecnico()
                                        descripcionNoVacia = false
                                        sueldoNoIntroducido = false
                                        descripcionnNoSimbolos = false
                                        descripcionRepetida = false
                                        navController.navigate(Screen.TipoTecnicoList)
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
//                        ToastNotification(message = "Tecnico registrado")
                    }

                }
            }
        }
    }
}

@Composable
fun ToastNotification(message: String) {
    Toast.makeText(LocalContext.current, message, Toast.LENGTH_SHORT).show()
}
fun Validar(tecnico: TipoTecnicoEntity?, tipoTecnios: List<TipoTecnicoEntity>): Boolean {
    descripcionNoVacia = tecnico?.descripcion.isNullOrEmpty() || tecnico?.descripcion?.isBlank() ?: false
    descripcionnNoSimbolos = tecnico?.descripcion?.contains(Regex("[^a-zA-Z ]+")) ?: false
    descripcionRepetida = tipoTecnios.any { it.descripcion == tecnico?.descripcion && it.tipoTecnicoId != tecnico?.tipoTecnicoId }
    return !descripcionNoVacia  && !descripcionnNoSimbolos && !descripcionRepetida
}

@Preview
@Composable
private fun TipoTecnicoPreview() {
    RegistroDeTecnicosTheme() {
        TipoTecnicoBody(
            uiState = TipoTecnicoUIState(),
            onSaveTecnico = {},
            navController = rememberNavController(),
            onDescripcionChanged = {},
            onNewTecnico = {},
            onDeleteTecnico = {},
            tipoTecnios = emptyList()
        )
    }
}
