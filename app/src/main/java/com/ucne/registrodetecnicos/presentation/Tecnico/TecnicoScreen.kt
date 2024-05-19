package com.ucne.registrodetecnicos.presentation.Tecnico

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ucne.registrodetecnicos.Screen
import com.ucne.registrodetecnicos.data.local.entities.TecnicoEntity
import com.ucne.registrodetecnicos.data.local.entities.TipoTecnicoEntity
import com.ucne.registrodetecnicos.presentation.components.Combobox
import com.ucne.registrodetecnicos.presentation.components.TopAppBar
import com.ucne.registrodetecnicos.ui.theme.RegistroDeTecnicosTheme
import kotlinx.coroutines.launch


var nombreNoVacio by  mutableStateOf(false)
    var sueldoNoIntroducido by  mutableStateOf(false)
    var personaNoSimbolos by  mutableStateOf(false)
    var nombreRepetido by mutableStateOf(false)
    var tipoTecnicoNoVacio by mutableStateOf(false)
    @Composable
    fun TecnicoScreen(
        viewModel: TecnicoViewModel,
        navController: NavHostController
    ) {
        val tipotTecnicos by viewModel.tipoTecnico.collectAsStateWithLifecycle()
        val tecnicos by viewModel.tecnico.collectAsStateWithLifecycle()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        TecnicoBody(
            uiState = uiState,
            tecnicos = tecnicos,
            onSaveTecnico = {
                viewModel.saveTecnico()
            },
            onDeleteTecnico = {
                viewModel.deleteTecnico()
            },
            onNewTecnico = {
                viewModel.newTecnico()
            },
            onTipoTecnioChanged = viewModel::onTipoTecnicoChanged,
            onNombreChanged =  viewModel::onNombreChanged,
            onSueldoHoraChanged = viewModel::onSueldoHoraChanged,
            tiposTecnicos = tipotTecnicos,
            navController = navController
        )
    }
    @Composable
    private fun TecnicoBody(
        uiState: TecnicoUIState,
        tecnicos: List<TecnicoEntity>,
        onSaveTecnico: () -> Unit,
        navController: NavHostController,
        onDeleteTecnico: () -> Unit = {},
        onNombreChanged: (String) -> Unit,
        tiposTecnicos: List<TipoTecnicoEntity>,
        onSueldoHoraChanged: (String) -> Unit,
        onNewTecnico: () -> Unit,
        onTipoTecnioChanged: (String) -> Unit,
    ) {
        val scope = rememberCoroutineScope()
        var drawerState = rememberDrawerState(DrawerValue.Closed)
        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet( Modifier.requiredWidth(220.dp)) {
                    Text("Registro de Tecnicos", modifier = Modifier.padding(16.dp))
                    Divider()
                    NavigationDrawerItem(
                        label = { Text(text = "Listas de tecnicos") },
                        selected = false,
                        onClick = { navController.navigate(Screen.TecnicoList) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Tecnicos"
                            )
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
                    title = "Técnicos",
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
                            value = uiState.nombre?: "",
                            onValueChange = onNombreChanged,
                            modifier = Modifier.fillMaxWidth(),
                            isError = nombreNoVacio || personaNoSimbolos || nombreRepetido
                        )
                        if(nombreNoVacio){
                            Text(text = "El nombre no puede estar vacio", color = MaterialTheme.colorScheme.error)
                        }
                        if(personaNoSimbolos){
                            Text(text = "El nombre no puede contener simbolos o números", color = MaterialTheme.colorScheme.error)
                        }
                        if(nombreRepetido){
                            Text(text = "El nombre de \"${uiState.nombre}\" ya existe", color = MaterialTheme.colorScheme.error)
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
                        if(sueldoNoIntroducido){
                            Text(text = "Debes de introducir un sueldo", color = MaterialTheme.colorScheme.error)
                        }
                        var selectedItem by remember { mutableStateOf<TipoTecnicoEntity?>(null) }
                        Combobox(
                            items = tiposTecnicos,
                            label = "Tipo de tecnico",
                            selectedItemString = { it?.let {
                                "${it.descripcion}"
                            } ?: ""},
                            selectedItem = selectedItem,
                            onItemSelected = {
                                onTipoTecnioChanged(it?.descripcion?:"")
                                selectedItem = it
                                uiState.tipoTecnico = it?.descripcion
                            },
                            itemTemplate = {Text(text = it.descripcion?:"")},
                            isErrored = tipoTecnicoNoVacio
                        )
                        if(tipoTecnicoNoVacio){
                            Text(text = "Debes de introducir un tipo de técnico", color = MaterialTheme.colorScheme.error)
                        }

                        Spacer(modifier = Modifier.padding(2.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            OutlinedButton(
                                onClick = {
                                    onNewTecnico()
                                    nombreNoVacio = false
                                    sueldoNoIntroducido = false
                                    personaNoSimbolos = false
                                    nombreRepetido = false
                                    tipoTecnicoNoVacio = false
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
                                            TecnicoEntity(
                                                tecnicoId = uiState.tecnicoId,
                                                nombre = uiState.nombre,
                                                sueldoHora = uiState.sueldoHora,
                                                tipo = uiState.tipoTecnico
                                            ),
                                        tecnicoList = tecnicos
                                        )
                                    ) {
                                        onSaveTecnico()
//                                        nombreNoVacio = false
//                                        sueldoNoIntroducido = false
//                                        personaNoSimbolos = false
//                                        nombreRepetido = false
//                                        tipoTecnicoNoVacio = false
                                        navController.navigate(Screen.TecnicoList)
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
                                    navController.navigate(Screen.TecnicoList)
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
    fun Validar(tecnico: TecnicoEntity?, tecnicoList: List<TecnicoEntity>): Boolean {
        nombreNoVacio = tecnico?.nombre.isNullOrEmpty() || tecnico?.nombre?.isBlank() ?: false
        sueldoNoIntroducido = (tecnico?.sueldoHora ?: 0.0) <= 0.0
        personaNoSimbolos = tecnico?.nombre?.contains(Regex("[^a-zA-Z ]+")) ?: false
        tipoTecnicoNoVacio = tecnico?.tipo.isNullOrEmpty() || tecnico?.tipo?.isBlank() ?: false
        nombreRepetido = tecnicoList.any { it.nombre?.replace(" ", "") == tecnico?.nombre?.replace(" ", "") && it.tecnicoId != tecnico?.tecnicoId }
        return !nombreNoVacio && !sueldoNoIntroducido  && !personaNoSimbolos && !tipoTecnicoNoVacio && !nombreRepetido
    }


    @Preview
    @Composable
    private fun TecnicoPreview() {
        RegistroDeTecnicosTheme() {
            TecnicoBody(
                uiState = TecnicoUIState(),
                onSaveTecnico = {},
                navController = rememberNavController(),
                onDeleteTecnico = {},
                onNombreChanged = {},
                tiposTecnicos = emptyList(),
                onSueldoHoraChanged = {},
                onNewTecnico = {},
                onTipoTecnioChanged = {},
                tecnicos =emptyList(),
//                onValidar = false

            )
        }
    }
