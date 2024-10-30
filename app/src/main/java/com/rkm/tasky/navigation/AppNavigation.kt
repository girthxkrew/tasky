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
                onNavigation = { TODO("Add SignUp Screen") },
                viewModel = hiltViewModel<LoginViewModel>(),
                modifier = modifier
            )
        }
    }
}
