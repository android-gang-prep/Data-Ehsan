package com.example.pratice_data_1.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pratice_data_1.LocalAppState
import com.example.pratice_data_1.Routes
import com.example.pratice_data_1.db.entities.CostEntity
import com.example.pratice_data_1.db.entities.TravelEntity
import com.example.pratice_data_1.db.entities.UserCostEntity
import com.example.pratice_data_1.db.entities.UserEntity

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {

    val appState = LocalAppState.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                appState.navController.navigate(Routes.Travels.route)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Travels")
        }
        Button(onClick = {
            appState.navController.navigate(Routes.Friends.route)
        }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
            Text(text = "Friends")
        }
        Button(
            onClick = {
                appState.navController.navigate(Routes.Logs.route)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Logs")
        }
    }

}