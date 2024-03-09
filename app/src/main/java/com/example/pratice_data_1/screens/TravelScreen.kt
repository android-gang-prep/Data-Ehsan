package com.example.pratice_data_1.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pratice_data_1.LocalAppState
import com.example.pratice_data_1.R
import com.example.pratice_data_1.Routes
import com.example.pratice_data_1.model.UserCostUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelScreen(
    travelId: Int,
    viewModel: TravelViewModel = viewModel()
) {
    val users by viewModel.users.collectAsState()
    val travel by viewModel.travel.collectAsState()

    val appState = LocalAppState.current

    LaunchedEffect(travelId) {
        viewModel.getTravel(travelId)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Button(
                onClick = {
                    appState.navController.navigate(Routes.TravelCosts.route + "/$travelId")
                }, modifier = Modifier
                    .fillMaxWidth(), shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Costs")
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it), contentAlignment = Alignment.Center
        ) {
            if (travel == null) {
                CircularProgressIndicator(color = Color(0xff313131))
            }
            travel?.let { travel ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Column(modifier = Modifier.fillMaxHeight()) {
                        Text(
                            text = "Travel Participants: (Click to add or remove)",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        if (users.isEmpty()) {
                            Text(text = "You have no friends")
                        } else {
                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(users) { user ->
                                    val userIsTravelPart = user.id in travel.users.map { it.id }
                                    Button(onClick = {
                                        if (userIsTravelPart) {
                                            // remove user from travel part
                                            viewModel.updateTravel(travel.copy(
                                                users = travel.users.filter { it.id != user.id }
                                            ),
                                                log = "user with name ${user.name} removed from ${travel.name} travel")
                                        } else {
                                            // add user to travel part
                                            viewModel.updateTravel(
                                                travel.copy(
                                                    users = travel.users + user
                                                ),
                                                log = "user with name ${user.name} added to ${travel.name} travel"
                                            )
                                        }
                                    }, shape = RoundedCornerShape(8.dp)) {
                                        Text(text = user.name)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Icon(
                                            painter = painterResource(id = if (userIsTravelPart) R.drawable.ic_remove else R.drawable.ic_add),
                                            tint = Color.White,
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        val totalCost = travel.costs.map {
                            it.userCosts.map {
                                it.toIRT()
                            }.sum()
                        }.sum()
                        val costText = buildAnnotatedString {
                            append("Total Cost Per IRT: ")
                            withStyle(SpanStyle(color = Color(0xFF1AB654))) {
                                append(totalCost.toString())
                            }
                            append(" IRT")
                        }
                        Text(text = costText)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "In Totaly:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        travel.costs.forEach {
                            if (it.payer != null) {
                                Text(text = "For ${it.title}:", fontSize = 14.sp, fontWeight = FontWeight.Bold,modifier=Modifier.padding(start = 8.dp))
                                val payer = it.payer
                                it.userCosts.filter { cost-> cost.user.id != it.payer.id }.forEach {
                                    if (it.user.id in travel.users.map { it.id }){
                                        Text(text = "${it.user.name} should pay ${it.toIRT()} irt to ${payer.name}",modifier=Modifier.padding(start = 16.dp))
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
    }
}