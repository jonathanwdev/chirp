package com.plcoding.chirp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.plcoding.auth.presentation.navigation.AuthGraphRoutes
import com.plcoding.chat.presentation.navigation.ChatGraphRoutes
import com.plcoding.chirp.navigation.DeepLinkListener
import com.plcoding.chirp.navigation.NavigationRoot
import com.plcoding.core.designsystem.theme.ChirpTheme
import com.plcoding.core.presentation.util.ObserveAsEvent
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App(
    onAuthenticationChecked: () -> Unit = {},
    viewModel: MainViewModel = koinViewModel()
) {
    val navController = rememberNavController()
    DeepLinkListener(navController = navController)
    val mainState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(mainState.isCheckingAuth) {
        if(!mainState.isCheckingAuth) {
            onAuthenticationChecked()
        }
    }

    ObserveAsEvent(viewModel.events) { event ->
        when(event) {
            MainEvent.OnSessionExpired -> {
                navController.navigate(AuthGraphRoutes.Graph) {
                    popUpTo(AuthGraphRoutes.Graph) {
                        inclusive = false
                    }
                }
            }
        }
    }



    ChirpTheme {
        if(!mainState.isCheckingAuth) {
            NavigationRoot(
                navController = navController,
                startDestination = if(mainState.isLoggedIn) {
                    ChatGraphRoutes.Graph
                }else {
                    AuthGraphRoutes.Graph
                }
            )
        }
    }
}