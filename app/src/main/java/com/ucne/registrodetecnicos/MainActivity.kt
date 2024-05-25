package com.ucne.registrodetecnicos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.room.Room
import com.ucne.registrodetecnicos.data.local.database.TecnicoDb
import com.ucne.registrodetecnicos.data.repository.TecnicoRepository
import com.ucne.registrodetecnicos.data.repository.TipoTecnicoRepository
import com.ucne.registrodetecnicos.navigation.NavHostCompose
import com.ucne.registrodetecnicos.navigation.Screen
import com.ucne.registrodetecnicos.presentation.Tecnico.TecnicoListScreen
import com.ucne.registrodetecnicos.presentation.Tecnico.TecnicoScreen
import com.ucne.registrodetecnicos.presentation.Tecnico.TecnicoViewModel
import com.ucne.registrodetecnicos.presentation.Tecnico.TipoTecnicoViewModel
import com.ucne.registrodetecnicos.presentation.TipoTecnico.TipoTecnicoListScreen
import com.ucne.registrodetecnicos.presentation.TipoTecnico.TipoTecnicoScreen
import com.ucne.registrodetecnicos.presentation.components.DrawerNavigation
import com.ucne.registrodetecnicos.ui.theme.RegistroDeTecnicosTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
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
                val scope = rememberCoroutineScope()
                var drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                DrawerNavigation(
                    drawerState = drawerState,
                    navToTecnicoList = { navController.navigate(Screen.TecnicoList) },
                    navToTipoTecnicoList = { navController.navigate(Screen.TipoTecnicoList) },
                    closeDrawer = {
                        scope.launch {
                            drawerState.close()
                        }
                    },
                ) {
                    NavHostCompose(navController, repository, tipoRepository, scope, drawerState)
                }
            }
        }
    }
}
