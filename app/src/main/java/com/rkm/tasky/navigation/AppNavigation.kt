package com.rkm.tasky.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.rkm.tasky.feature.agenda.screen.AgendaScreenRoot
import com.rkm.tasky.feature.login.screen.LoginScreenRoot
import com.rkm.tasky.feature.login.viewmodel.LoginViewModel
import com.rkm.tasky.feature.registration.screen.RegistrationScreenRoot
import com.rkm.tasky.feature.registration.viewmodel.RegistrationViewModel
import com.rkm.tasky.ui.activity.AuthState
import kotlinx.serialization.Serializable

@Serializable
object Authentication

@Serializable
object Login

@Serializable
object Registration

@Serializable
object Home

@Serializable
object Agenda


@Composable
fun AppNavigation(
    modifier: Modifier,
    navController: NavHostController = rememberNavController(),
    authState: AuthState
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = if (authState == AuthState.AuthenticatedUser) Home else Authentication
    ) {

        navigation<Home>(startDestination = Agenda) {
            composable<Agenda> {
                AgendaScreenRoot(modifier = modifier)
            }
        }

        navigation<Authentication>(startDestination = Login) {
            composable<Login> {
                LoginScreenRoot(
                    onRegistrationClick = { navController.navigate(Registration) },
                    onLoginSuccess =
                    {
                        navController.navigate(Agenda) {
                            popUpTo(Authentication) {
                                inclusive = true
                            }
                        }
                    },
                    viewModel = hiltViewModel<LoginViewModel>(),
                    modifier = modifier
                )
            }

            composable<Registration> {
                RegistrationScreenRoot(
                    onNavigateBack = {
                        navController.navigate(Login) {
                            popUpTo(Login) {
                                inclusive = true
                            }
                        }
                    },
                    viewModel = hiltViewModel<RegistrationViewModel>(),
                    modifier = modifier
                )
            }
        }
    }
}
