package com.ucne.registrodetecnicos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.room.Room
import com.ucne.registrodetecnicos.data.local.database.TecnicoDb
import com.ucne.registrodetecnicos.data.repository.TecnicoRepository
import com.ucne.registrodetecnicos.data.repository.TipoTecnicoRepository
import com.ucne.registrodetecnicos.presentation.Tecnico.TecnicoListScreen
import com.ucne.registrodetecnicos.presentation.Tecnico.TecnicoScreen
import com.ucne.registrodetecnicos.presentation.Tecnico.TecnicoViewModel
import com.ucne.registrodetecnicos.presentation.Tecnico.TipoTecnicoViewModel
import com.ucne.registrodetecnicos.presentation.TipoTecnico.TipoTecnicoListScreen
import com.ucne.registrodetecnicos.presentation.TipoTecnico.TipoTecnicoScreen
import com.ucne.registrodetecnicos.ui.theme.RegistroDeTecnicosTheme
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    private lateinit var tecnicoDb: TecnicoDb
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tecnicoDb = Room.databaseBuilder(
            this,
            TecnicoDb::class.java,
            "tecnico.db"
        )
            .fallbackToDestructiveMigration()
            .build()
        val repository = TecnicoRepository(tecnicoDb.tecnicoDao())
        val tipoRepository = TipoTecnicoRepository(tecnicoDb.tipoTecnicoDao())
        enableEdgeToEdge()
        setContent {
            RegistroDeTecnicosTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Screen.TecnicoList) {
                    composable<Screen.TecnicoList>{
                        TecnicoListScreen(
                            viewModel = viewModel{TecnicoViewModel(repository,0,tipoRepository)},
                            onVerTecnico = {
                                navController.navigate(Screen.Tecnico(it.tecnicoId ?: 0))
                            },
                            onAddTecnico = {
                                navController.navigate(Screen.Tecnico(0))
                            },
                            navController = navController
                            )
                    }
                    composable<Screen.Tecnico> {
                        val args = it.toRoute<Screen.Tecnico>()
                        TecnicoScreen(
                            viewModel = viewModel { TecnicoViewModel(repository,args.tecnicoId, tipoRepository) },
                            navController = navController)
                    }
                    composable<Screen.TipoTecnico> {
                        val args = it.toRoute<Screen.TipoTecnico>()
                        TipoTecnicoScreen(viewModel = viewModel{TipoTecnicoViewModel(tipoRepository,args.tipoTecnicoId)},
                            navController = navController )
                    }
                    composable<Screen.TipoTecnicoList> {
                        TipoTecnicoListScreen(
                            viewModel = viewModel{TipoTecnicoViewModel(tipoRepository,0)},
                            navController = navController,
                            onVerTecnico = {
                                navController.navigate(Screen.TipoTecnico(it.tipoTecnicoId ?: 0))
                            },
                            onAddTecnico = {
                                navController.navigate(Screen.TipoTecnico(0))
                            }
                        )
                    }

                }
            }
        }
    }
}


sealed class Screen{
    @Serializable
    object TecnicoList: Screen()

    @Serializable
    data class Tecnico(val tecnicoId: Int): Screen()

    @Serializable
    class TipoTecnico(val tipoTecnicoId: Int) : Screen()

    @Serializable
    object TipoTecnicoList: Screen()


}