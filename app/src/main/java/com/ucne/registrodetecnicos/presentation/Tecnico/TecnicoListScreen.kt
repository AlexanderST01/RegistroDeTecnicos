package com.ucne.registrodetecnicos.presentation.Tecnico

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucne.registrodetecnicos.data.local.entities.TecnicoEntity
import com.ucne.registrodetecnicos.ui.theme.RegistroDeTecnicosTheme


@Composable
fun TecnicoListScreen(
    viewModel: TecnicoViewModel,
    onVerTicket: (TecnicoEntity) -> Unit
) {
    val tecnicos by viewModel.tecnico.collectAsStateWithLifecycle()
    TecnicoBody(
        tecnicos = tecnicos,
        onVerTecnico = onVerTicket
    )
}
@Composable
fun TecnicoBody(
    tecnicos: List<TecnicoEntity>,
    onVerTecnico: (TecnicoEntity) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
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
                        .padding(16.dp)
                ) {
                    Text(text = item.tecnicoId.toString(), modifier = Modifier.weight(0.10f))
                    Text(text = item.nombre.toString(), modifier = Modifier.weight(0.400f))
                    Text(text = item.sueldoHora.toString(), modifier = Modifier.weight(0.40f))
                }
            }
        }
    }
}

@Preview
@Composable
private fun TecnicoListPreview() {
    val tecnico = listOf(
        TecnicoEntity(
            nombre = "Alexander",
            sueldoHora = 100.0
        )
    )
    RegistroDeTecnicosTheme() {
        TecnicoBody(tecnicos = tecnico) {

        }
    }
}