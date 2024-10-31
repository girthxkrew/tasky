package com.rkm.tasky.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rkm.tasky.feature.loginscreen.screen.LoginScreenRoot
import com.rkm.tasky.feature.loginscreen.viewmodel.LoginViewModel
import kotlinx.serialization.Serializable

@Serializable
object Login
@Serializable
object Registration
@Serializable
object Agenda


@Composable
fun AppNavigation(
    modifier: Modifier,
    navController: NavHostController = rememberNavController()
){
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Login,
    ) {
        composable<Login> {
            LoginScreenRoot(
                onRegistrationClick = { TODO("Add SignUp Screen") },
                onLoginSuccess = { TODO("Add agenda screen") },
                viewModel = hiltViewModel<LoginViewModel>(),
                modifier = modifier
            )
        }
    }
}
