package com.ucne.registrodetecnicos.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> Combobox(
    items: List<T>,
    selectedItem: T?,
    selectedItemString: (T?) -> String,
    onItemSelected: (T?) -> Unit,
    label: String = "",
    itemToId: (T) -> Int?,
    selectedItemId: Int?,
    itemTemplate: @Composable (T) -> Unit,
    isErrored: Boolean
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedItemId = items.find { itemToId(it) == selectedItemId }
    val textFieldValue = selectedItemId?.let { selectedItemString(it) } ?: ""

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = textFieldValue,
            onValueChange = { },
            readOnly = true,
            label = { Text(text = label)},
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            isError = isErrored,
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {itemTemplate(item)},
                    onClick = {
                        onItemSelected(item)
                        expanded = false
//                        textFieldValue = item.toString()
                    }
                )
            }
        }
    }
}
@Preview
@Composable
fun vista(

) {
    val items = listOf(
        Persona(nombre = "John", apellido = "Johnson"),
        Persona(nombre = "John", apellido = "Harrison"),
        Persona(nombre = "dd", apellido = "a" ),
        )

    var selectedItem by remember { mutableStateOf<Persona?>(null) }
    Combobox(
        items = items,
        label = "Prueba label",
        selectedItemString = { it?.let { persona ->
            "${persona.nombre} ${persona.apellido}"
        } ?: ""},
        selectedItem = selectedItem,
        onItemSelected = {
            selectedItem = it
        },
        itemTemplate = { Text(it.nombre) },
        isErrored = false,
        itemToId = { 1 },
        selectedItemId = null
    )
}
data class Persona(
    val nombre: String,
    val apellido: String
)
