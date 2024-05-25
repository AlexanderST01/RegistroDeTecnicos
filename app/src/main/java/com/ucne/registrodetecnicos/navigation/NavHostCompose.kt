package com.ucne.registrodetecnicos.navigation

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.ucne.registrodetecnicos.data.repository.TecnicoRepository
import com.ucne.registrodetecnicos.data.repository.TipoTecnicoRepository
import com.ucne.registrodetecnicos.presentation.Tecnico.TecnicoListScreen
import com.ucne.registrodetecnicos.presentation.Tecnico.TecnicoScreen
import com.ucne.registrodetecnicos.presentation.Tecnico.TecnicoViewModel
import com.ucne.registrodetecnicos.presentation.Tecnico.TipoTecnicoViewModel
import com.ucne.registrodetecnicos.presentation.TipoTecnico.TipoTecnicoListScreen
import com.ucne.registrodetecnicos.presentation.TipoTecnico.TipoTecnicoScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NavHostCompose(
    navController: NavHostController,
    repository: TecnicoRepository,
    tipoRepository: TipoTecnicoRepository,
    scope: CoroutineScope,
    drawerState: DrawerState
) {
    NavHost(navController = navController, startDestination = Screen.TecnicoList) {
        composable<Screen.TecnicoList> {
            TecnicoListScreen(
                viewModel = viewModel {
                    TecnicoViewModel(
                        repository,
                        0,
                        tipoRepository
                    )
                },
                onVerTecnico = {
                    navController.navigate(Screen.Tecnico(it.tecnicoId ?: 0))
                },
                onAddTecnico = {
                    navController.navigate(Screen.Tecnico(0))
                },
                openDrawer = {
                    scope.launch {
                        drawerState.open()
                    }
                }
            )
        }
        composable<Screen.Tecnico> {
            val args = it.toRoute<Screen.Tecnico>()
            TecnicoScreen(
                viewModel = viewModel {
                    TecnicoViewModel(
                        repository,
                        args.tecnicoId,
                        tipoRepository
                    )
                },
                goToTecnicoList = { navController.navigate(Screen.TecnicoList) },
                navController = navController,
                openDrawer = {
                    scope.launch {
                        drawerState.open()
                    }
                }
            )
        }
        composable<Screen.TipoTecnico> {
            val args = it.toRoute<Screen.TipoTecnico>()
            TipoTecnicoScreen(
                viewModel = viewModel {
                    TipoTecnicoViewModel(
                        tipoRepository,
                        args.tipoId
                    )
                },
                goToTipoTecnicoList = { navController.navigate(Screen.TipoTecnicoList) },
                openDrawer = {
                    scope.launch {
                        drawerState.open()
                    }
                }
            )
        }
        composable<Screen.TipoTecnicoList> {
            TipoTecnicoListScreen(
                viewModel = viewModel { TipoTecnicoViewModel(tipoRepository, 0) },
                onVerTecnico = {
                    navController.navigate(
                        Screen.TipoTecnico(
                            it.tipoId ?: 0
                        )
                    )
                },
                onAddTecnico = {
                    navController.navigate(Screen.TipoTecnico(0))
                },
                openDrawer = {
                    scope.launch {
                        drawerState.open()
                    }
                }
            )
        }
    }
}