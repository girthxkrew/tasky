package com.rkm.tasky.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.rkm.tasky.feature.agenda.screen.AgendaNavigationScreenEvents
import com.rkm.tasky.feature.agenda.screen.AgendaScreenRoot
import com.rkm.tasky.feature.agenda.viewmodel.AgendaViewModel
import com.rkm.tasky.feature.common.Mode
import com.rkm.tasky.feature.edit.screen.EditActionType
import com.rkm.tasky.feature.edit.screen.EditScreenRoot
import com.rkm.tasky.feature.edit.viewmodel.EditScreenViewModel
import com.rkm.tasky.feature.login.screen.LoginScreenRoot
import com.rkm.tasky.feature.login.viewmodel.LoginViewModel
import com.rkm.tasky.feature.registration.screen.RegistrationScreenRoot
import com.rkm.tasky.feature.registration.viewmodel.RegistrationViewModel
import com.rkm.tasky.feature.reminder.screen.ReminderScreenEvents
import com.rkm.tasky.feature.reminder.screen.ReminderScreenRoot
import com.rkm.tasky.feature.reminder.viewmodel.ReminderViewModel
import com.rkm.tasky.feature.task.screen.TaskScreenEvents
import com.rkm.tasky.feature.task.screen.TaskScreenRoot
import com.rkm.tasky.feature.task.viewmodel.TaskViewModel
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
                val events = AgendaNavigationScreenEvents(
                    onReminderClick = { id, mode, date ->
                        navController.navigate(Destination.Reminder(id = id, mode = mode, date = date))
                    },
                    onTaskClick = { id, mode, date ->
                        navController.navigate(Destination.Task(id = id, mode = mode, date = date))
                    }
                )
                AgendaScreenRoot(
                    modifier = modifier,
                    viewModel = hiltViewModel<AgendaViewModel>(),
                    events = events
                )
            }

            composable<Destination.Reminder> {

                val title = it.savedStateHandle.get<String>(EditActionType.TITLE.name)
                val desc = it.savedStateHandle.get<String>(EditActionType.DESCRIPTION.name)

                val events = ReminderScreenEvents(
                    onNavigateBack = {
                        navController.navigate(Destination.Agenda) {
                            popUpTo(Destination.Agenda) {
                                inclusive = true
                            }
                        }
                    },
                    onEditField = { text, action ->
                        navController
                            .navigate(route = Destination.Edit(text, action))
                    }
                )

                ReminderScreenRoot(
                    modifier = modifier,
                    viewModel = hiltViewModel<ReminderViewModel>(),
                    events = events,
                    title = title,
                    desc = desc
                )
            }

            composable<Destination.Task> {

                val title = it.savedStateHandle.get<String>(EditActionType.TITLE.name)
                val desc = it.savedStateHandle.get<String>(EditActionType.DESCRIPTION.name)

                val events = TaskScreenEvents(
                    onNavigateBack = {
                        navController.navigate(Destination.Agenda) {
                            popUpTo(Destination.Agenda) {
                                inclusive = true
                            }
                        }
                    },
                    onEditField = { text, action ->
                        navController
                            .navigate(route = Destination.Edit(text, action))
                    }
                )

                TaskScreenRoot(
                    modifier = modifier,
                    viewModel = hiltViewModel<TaskViewModel>(),
                    events = events,
                    title = title,
                    desc = desc
                )
            }

            composable<Destination.Edit> {
                EditScreenRoot(
                    modifier = modifier,
                    viewModel = hiltViewModel<EditScreenViewModel>(),
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onSavePressed = { text, action ->
                        navController.
                            previousBackStackEntry
                            ?.savedStateHandle
                            ?.set(action, text)
                        navController.popBackStack()
                    }
                )
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
    data object Login : Destination()

    @Serializable
    data object Registration : Destination()

    @Serializable
    data object Home : Destination()

    @Serializable
    data object Agenda : Destination()

    @Serializable
    data class Reminder(val id: String = "", val mode: String = Mode.CREATE.name, val date: String) : Destination()

    @Serializable
    data class Task(val id: String = "", val mode: String = Mode.CREATE.name, val date: String) : Destination()

    @Serializable
    data class Edit(val text: String, val action: String) : Destination()
}

