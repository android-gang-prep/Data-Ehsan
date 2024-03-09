package com.example.pratice_data_1.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pratice_data_1.LocalAppState
import com.example.pratice_data_1.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelsScreen(viewModel:TravelsViewModel = viewModel()) {

    val travels by viewModel.travels.collectAsState()
    val users by viewModel.users.collectAsState()
    val appState = LocalAppState.current

    val dialogOpen = remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    if (dialogOpen.value){
        val travelName = remember {
            mutableStateOf("")
        }
        Dialog(onDismissRequest = { dialogOpen.value = false }) {
            Column(modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .padding(12.dp)) {
                Text(text = "Add Travel", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = travelName.value,
                    onValueChange = { travelName.value = it }, placeholder = {
                        Text(text = "Travel Name")
                    }, modifier = Modifier.fillMaxWidth()
                )

                Button(onClick = {
                    if (travelName.value.isNotBlank()) {
                        viewModel.addTravel(travelName.value)
                        dialogOpen.value = false
                    }
                }, shape = RoundedCornerShape(8.dp),modifier=Modifier.fillMaxWidth()) {
                    Text(text = "Add")
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter){
        Column(modifier=Modifier.fillMaxSize()) {
            Text(text = "Your Travels:",modifier=Modifier.fillMaxWidth(), fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            LazyColumn(modifier=Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(travels){
                    Card(onClick = {
                        appState.navController.navigate(Routes.Travel.route+"/${it.id}")
                    }) {
                        Column(modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp)){
                            Text(text = "Travel: ${it.name}")
                            Text(text = "Participants: ${it.users.count()} user")
                        }
                    }
                }
            }
        }
        Button(onClick = {
                         if (users.isEmpty()){
                             Toast.makeText(context, "First add friends to add new travel", Toast.LENGTH_SHORT).show()
                         }else{
                             dialogOpen.value = true
                         }
        },modifier=Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
            Text(text = "Add Travel")
        }
    }
}