package com.ucne.registrodetecnicos.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job

@Composable
fun DrawerNavigation(
    drawerState: DrawerState,
    navToTecnicoList: () -> Unit,
    navToTipoTecnicoList: () -> Unit,
    closeDrawer: () -> Unit,
    content: @Composable () -> Unit,
) {
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(Modifier.requiredWidth(220.dp)) {
                Text("Lista de tipo de Tecnicos", modifier = Modifier.padding(16.dp))
                Divider()
                NavigationDrawerItem(
                    label = { Text(text = "Lista de Técnicos") },
                    selected = false,
                    onClick =  {
                        navToTecnicoList()
                        closeDrawer()
                               },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Tecnicos"
                        )
                    }
                )
                NavigationDrawerItem(
                    label = { Text(text = "Lista de Tipo Técnicos") },
                    selected = false,
                    onClick = {
                        navToTipoTecnicoList()
                        closeDrawer()
                              },
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
    )
    {
        content()
    }
}