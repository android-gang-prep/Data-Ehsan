package com.example.pratice_data_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.pratice_data_1.screens.FriendsScreen
import com.example.pratice_data_1.screens.HomeScreen
import com.example.pratice_data_1.screens.LogsScreen
import com.example.pratice_data_1.screens.TravelCostsScreen
import com.example.pratice_data_1.screens.TravelScreen
import com.example.pratice_data_1.screens.TravelsScreen
import com.example.pratice_data_1.ui.theme.PraticeData1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            PraticeData1Theme(false) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val appState = LocalAppState.current

                    Scaffold(modifier=Modifier.padding(16.dp)){
                        NavHost(
                            modifier=Modifier.padding(it),
                            navController = appState.navController,
                            startDestination = Routes.Home.route,
                            enterTransition = {
                                slideIntoContainer(
                                    AnimatedContentTransitionScope.SlideDirection.Left,
                                    animationSpec = tween(500)
                                )
                            },
                            exitTransition = {
                                slideOutOfContainer(
                                    AnimatedContentTransitionScope.SlideDirection.Left,
                                    animationSpec = tween(500)
                                )
                            },
                            popExitTransition = {
                                slideOutOfContainer(
                                    AnimatedContentTransitionScope.SlideDirection.Right,
                                    tween(500)
                                )
                            },
                            popEnterTransition = {
                                slideIntoContainer(
                                    AnimatedContentTransitionScope.SlideDirection.Right,
                                    tween(500)
                                )
                            }
                        ){
                            composable(Routes.Home.route){
                                HomeScreen()
                            }
                            composable(Routes.Travels.route){
                                TravelsScreen()
                            }
                            composable(Routes.Travel.route+"/{id}", arguments = listOf(
                                navArgument("id"){
                                    type = NavType.StringType
                                }
                            )){
                                TravelScreen(travelId = it.arguments?.getString("id") ?: "0")
                            }
                            composable(Routes.TravelCosts.route+"/{id}", arguments = listOf(
                                navArgument("id"){
                                    type = NavType.StringType
                                }
                            )){
                                TravelCostsScreen(travelId = it.arguments?.getString("id") ?: "0")
                            }
                            composable(Routes.Friends.route){
                                FriendsScreen()
                            }
                            composable(Routes.Logs.route){
                                LogsScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}
