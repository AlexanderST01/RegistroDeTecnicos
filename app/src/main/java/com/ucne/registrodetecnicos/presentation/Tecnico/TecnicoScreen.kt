package com.ucne.registrodetecnicos.presentation.Tecnico

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucne.registrodetecnicos.data.local.entities.TecnicoEntity
import com.ucne.registrodetecnicos.ui.theme.RegistroDeTecnicosTheme


@Composable
fun TecnicoScreen(
    viewModel: TecnicoViewModel
) {
    val tickets by viewModel.tecnico.collectAsStateWithLifecycle()
    TecnicoBody(
        onSaveTicket = { tecnico ->
            viewModel.saveTicket(tecnico)
        }
    )
}

@Composable
private fun TecnicoBody(onSaveTicket: (TecnicoEntity) -> Unit) {
    var tecnicoId by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var sueldoHora by remember { mutableStateOf("")}

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
                value = nombre,
                onValueChange = { nombre = it },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                label = {Text(text = "Sueldo por hora")},
                value = sueldoHora.toString().replace(Regex("[a-zA-Z ]+"), ""),
                onValueChange = { sueldoHora = it.toDoubleOrNull().toString() },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()

            )

            Spacer(modifier = Modifier.padding(2.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(
                    onClick = {
                        tecnicoId = ""
                        nombre = ""
                        sueldoHora = ""
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
                        onSaveTicket(
                            TecnicoEntity(
                                tecnicoId = tecnicoId.toIntOrNull(),
                                nombre = nombre,
                                sueldoHora = sueldoHora.toDoubleOrNull()
                            )
                        )
                        tecnicoId = ""
                        nombre = ""
                        sueldoHora = ""
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "save button"
                    )
                    Text(text = "Guardar")
                }
            }
        }

    }

}


@Preview
@Composable
private fun TecnicoPreview() {
    RegistroDeTecnicosTheme() {
        TecnicoBody() {
        }
    }
}