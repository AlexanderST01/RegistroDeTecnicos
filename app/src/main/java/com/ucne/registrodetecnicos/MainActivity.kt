package com.ucne.registrodetecnicos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.ucne.registrodetecnicos.data.local.database.TecnicoDb
import com.ucne.registrodetecnicos.data.repository.ServicioRepository
import com.ucne.registrodetecnicos.data.repository.TecnicoRepository
import com.ucne.registrodetecnicos.data.repository.TipoTecnicoRepository
import com.ucne.registrodetecnicos.presentation.navigation.NavHostCompose
import com.ucne.registrodetecnicos.presentation.navigation.Screen
import com.ucne.registrodetecnicos.presentation.components.DrawerNavigation
import com.ucne.registrodetecnicos.ui.theme.RegistroDeTecnicosTheme
import kotlinx.coroutines.launch

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
        val servicioRepository = ServicioRepository(tecnicoDb.servicioDao())
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
                    navToServicioList = {navController.navigate(Screen.ServicioListScreen)},
                    closeDrawer = {
                        scope.launch {
                            drawerState.close()
                        }
                    },
                ) {
                    NavHostCompose(navController, repository, tipoRepository, scope, drawerState, servicioRepository)
                }
            }
        }
    }
}
