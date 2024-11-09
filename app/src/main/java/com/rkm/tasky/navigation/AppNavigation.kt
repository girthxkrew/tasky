package com.rkm.tasky.navigation

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.rkm.tasky.R
import com.rkm.tasky.feature.agenda.screen.AgendaScreenRoot
import com.rkm.tasky.feature.agenda.viewmodel.AgendaViewModel
import com.rkm.tasky.feature.edit.screen.EditScreenRoot
import com.rkm.tasky.feature.edit.viewmodel.EditScreenViewModel
import com.rkm.tasky.feature.login.screen.LoginScreenRoot
import com.rkm.tasky.feature.login.viewmodel.LoginViewModel
import com.rkm.tasky.feature.registration.screen.RegistrationScreenRoot
import com.rkm.tasky.feature.registration.viewmodel.RegistrationViewModel
import com.rkm.tasky.ui.activity.AuthState
import kotlinx.serialization.Serializable

@Composable
fun AppNavigation(
    modifier: Modifier,
    navController: NavHostController = rememberNavController(),
    authState: AuthState
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = if (authState == AuthState.AuthenticatedUser) Destination.Home else Destination.Authentication
    ) {

        navigation<Destination.Home>(startDestination = Destination.Agenda) {
            composable<Destination.Agenda> {
                AgendaScreenRoot(
                    modifier = modifier,
                    viewModel = hiltViewModel<AgendaViewModel>()
                )
            }

            composable<Destination.Edit> {
                TODO("Add Logic to handle navigation")
            }
        }

        navigation<Destination.Authentication>(startDestination = Destination.Login) {
            composable<Destination.Login> {
                LoginScreenRoot(
                    onRegistrationClick = { navController.navigate(Destination.Registration) },
                    onLoginSuccess =
                    {
                        navController.navigate(Destination.Agenda) {
                            popUpTo(Destination.Authentication) {
                                inclusive = true
                            }
                        }
                    },
                    viewModel = hiltViewModel<LoginViewModel>(),
                    modifier = modifier
                )
            }

            composable<Destination.Registration> {
                RegistrationScreenRoot(
                    onNavigateBack = {
                        navController.navigate(Destination.Login) {
                            popUpTo(Destination.Login) {
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

sealed class Destination {
    @Serializable
    data object Authentication : Destination()

    @Serializable
    object Login : Destination()

    @Serializable
    object Registration : Destination()

    @Serializable
    object Home : Destination()

    @Serializable
    object Agenda : Destination()

    @Serializable
    data class Edit(val text: String, val action: EditActionType) : Destination()
}

@Serializable
sealed class EditActionType(@StringRes val id: Int) {
    data object Title : EditActionType(R.string.edit_screen_toolbar_title)
    data object Description : EditActionType(R.string.edit_screen_toolbar_description)
}
