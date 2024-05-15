package com.ucne.registrodetecnicos.presentation.Tecnico

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucne.registrodetecnicos.data.local.entities.TecnicoEntity
import com.ucne.registrodetecnicos.presentation.components.TopAppBar
import com.ucne.registrodetecnicos.ui.theme.RegistroDeTecnicosTheme


    var nombreVacio by  mutableStateOf(false)
    var sueldoNoIntroducido by  mutableStateOf(false)
    var personaNoSimbolos by  mutableStateOf(false)
    var nombreRepetido by mutableStateOf(false)
    @Composable
    fun TecnicoScreen(
        viewModel: TecnicoViewModel
    ) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        TecnicoBody(
            uiState = uiState,
            onSaveTicket = {
                viewModel.saveTecnico()
            },
            onNombreChanged =  viewModel::onNombreChanged,
            onSueldoHoraChanged = viewModel::onSueldoHoraChanged,

//            tecnicos = viewModel.tecnicos.collectAsState(initial = emptyList()).value
        )
    }
    @Composable
    private fun TecnicoBody(
        uiState: TecnicoUIState,
        onSaveTicket: () -> Unit,
//        tecnicos:List<TecnicoEntity>,
        onNombreChanged: (String) -> Unit,
        onSueldoHoraChanged: (Double) -> Unit
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = "Tecnicos"
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
                            value = uiState.nombre,
                            onValueChange = onNombreChanged,
                            modifier = Modifier.fillMaxWidth(),
                            isError = nombreVacio || personaNoSimbolos || nombreRepetido
                        )
                        if(nombreVacio){
                            Text(text = "El nombre no puede estar vacio", color = MaterialTheme.colorScheme.error)
                        }
                        if(personaNoSimbolos){
                            Text(text = "El nombre no puede contener simbolos o números", color = MaterialTheme.colorScheme.error)
                        }
                        if(nombreRepetido){
                            Text(text = "El nombre de \"$uiState.nombre\" ya existe", color = MaterialTheme.colorScheme.error)
                        }
                        Spacer(modifier = Modifier.padding(2.dp))
                        OutlinedTextField(
                            label = { Text(text = "Sueldo por hora") },
                            value = uiState.sueldoHora.toString().replace(Regex("[a-zA-Z ]+"), ""),
                            onValueChange = {onSueldoHoraChanged},
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            isError = sueldoNoIntroducido
                        )
                        if(sueldoNoIntroducido){
                            Text(text = "Debes de introducir un sueldo", color = MaterialTheme.colorScheme.error)
                        }

                        Spacer(modifier = Modifier.padding(2.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            OutlinedButton(
                                onClick = {
                                    onNombreChanged.invoke("")
                                    onSueldoHoraChanged.invoke(0.0)
                                    nombreVacio = false
                                    sueldoNoIntroducido = false
                                    personaNoSimbolos = false
                                    nombreRepetido = false
//                                    guardado = false
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
                                                sueldoHora = uiState.sueldoHora
                                            ),
                                        )
                                    ) {
                                        onSaveTicket()
                                        onNombreChanged.invoke("")
                                        onSueldoHoraChanged.invoke(0.0)
                                        nombreVacio = false
                                        sueldoNoIntroducido = false
                                        personaNoSimbolos = false
                                        nombreRepetido = false
//                                        guardado = true
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "save button"
                                )
                                Text(text = "Guardar")
                            }
                        }
                        ToastNotification(message = "Tecnico registrado")
                    }

                }
            }
        }
        var guardado by remember { mutableStateOf(false) }



    }

    @Composable
    fun ToastNotification(message: String) {
        Toast.makeText(LocalContext.current, message, Toast.LENGTH_SHORT).show()
    }
    fun Validar(tecnico: TecnicoEntity?): Boolean {
        nombreVacio = tecnico?.nombre.isNullOrEmpty() || tecnico?.nombre?.isBlank() ?: false
        sueldoNoIntroducido = (tecnico?.sueldoHora ?: 0.0) <= 0.0
        personaNoSimbolos = tecnico?.nombre?.contains(Regex("[^a-zA-Z ]+")) ?: false
        return !nombreVacio && !sueldoNoIntroducido  && !personaNoSimbolos
    }

    @Preview
    @Composable
    private fun TecnicoPreview() {
        RegistroDeTecnicosTheme() {
            TecnicoBody(
                onSaveTicket = {},
                onNombreChanged = {},
                onSueldoHoraChanged = {},
                uiState = TecnicoUIState()
            )
        }
    }
