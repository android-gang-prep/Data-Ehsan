package com.example.pratice_data_1.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun FriendsScreen(viewModel: FriendsViewModel = viewModel()) {

    val friends by viewModel.users.collectAsState()

    val dialogOpen = remember {
        mutableStateOf(false)
    }

    if (dialogOpen.value) {

        val friendName = remember {
            mutableStateOf("")
        }

        Dialog(onDismissRequest = { dialogOpen.value = false }) {
            Column(modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(Color.White).padding(12.dp)) {
                Text(text = "Add Friend", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = friendName.value,
                    onValueChange = { friendName.value = it }, placeholder = {
                        Text(text = "Friend Name")
                    }, modifier = Modifier.fillMaxWidth()
                )

                Button(onClick = {
                    if (friendName.value.isNotBlank()) {
                        viewModel.addFriend(friendName.value)
                        dialogOpen.value = false
                    }
                }, shape = RoundedCornerShape(8.dp),modifier=Modifier.fillMaxWidth()) {
                    Text(text = "Add")
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { dialogOpen.value = true}, shape = RoundedCornerShape(8.dp)) {
            Text(text = "Add Friend")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Your Friends:",modifier=Modifier.fillMaxWidth(), fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            items(friends){
                Text(text = it.name)
            }
        }
    }
}