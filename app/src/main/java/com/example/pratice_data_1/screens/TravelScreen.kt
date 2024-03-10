package com.example.pratice_data_1.screens

import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pratice_data_1.LocalAppState
import com.example.pratice_data_1.R
import com.example.pratice_data_1.Routes
import com.example.pratice_data_1.db.entities.COST_TYPE_STOCK
import com.example.pratice_data_1.model.UiUser
import com.example.pratice_data_1.utils.toIRT
import com.example.pratice_data_1.viewModel.TravelViewModel
import kotlin.math.abs

@Composable
fun TravelScreen(
    travelId: String,
    viewModel: TravelViewModel = viewModel()
) {
    val users by viewModel.users.collectAsState()
    val travel by viewModel.travel.collectAsState()

    val appState = LocalAppState.current

    val context = LocalContext.current

    LaunchedEffect(travelId) {
        viewModel.getTravel(travelId)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        if (travel?.users?.isEmpty() == true){
                            Toast.makeText(context, "There is no friend in this travel", Toast.LENGTH_SHORT).show()
                        }else{
                            appState.navController.navigate(Routes.Debts.route + "/$travelId")
                        }
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f), shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Debts")
                }
                Button(
                    onClick = {
                        if (travel?.users?.isEmpty() == true){
                            Toast.makeText(context, "There is no friend in this travel", Toast.LENGTH_SHORT).show()
                        }else{
                            appState.navController.navigate(Routes.TravelCosts.route + "/$travelId")
                        }
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f), shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Costs")
                }
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
                                            println("ui id: ${travel.id}")
                                            viewModel.updateTravel(
                                                travel.copy (
                                                    users = (travel.users + user)
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
                            val instance = it
                            val value: Double = if (instance.costAmount != null){
                                val costAmount = instance.costAmount.toDoubleOrNull() ?: 0.0

                                costAmount.toIRT(instance.costPriceType)
                            }else{
                                instance.userCosts.sumOf { it.toIRT() }
                            }
                            value
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
                        Text(text = "In Travel:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))

                        val totalCosts = remember {
                            mutableStateOf(emptyList<TotalCost>())
                        }

                        LaunchedEffect(travel.costs,travel.debts,travel.users) {
                            totalCosts.value = emptyList()
                            travel.costs.forEach {
                                if (it.payer != null) {
                                    val payer = it.payer
                                    if (it.costAmount != null){
                                        it.userCosts.filter { cost-> cost.user?.id != it.payer.id }.forEach { userCost->
                                            if (userCost.user?.id in travel.users.map { it.id }){

                                                val value = if (it.costType == COST_TYPE_STOCK){
                                                    //stock
                                                    val sumOfStocks = it.userCosts.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
                                                    println("sum of stucs: $sumOfStocks")
                                                    ((it.costAmount.toDoubleOrNull() ?: 0.0) * (userCost.amount.toDoubleOrNull() ?: 0.0))/sumOfStocks
                                                }else{
                                                    //percent
                                                    ((it.costAmount.toDoubleOrNull() ?: 0.0) * (userCost.amount.toDoubleOrNull() ?: 0.0))/100
                                                }.toIRT(it.costPriceType)
                                                if (totalCosts.value.any { it.fromUser.id == userCost.user?.id && it.toUser.id == payer.id }){
                                                    val find = totalCosts.value.find { it.fromUser == userCost.user && it.toUser == payer }
                                                    val index = totalCosts.value.indexOf(find)
                                                    val list = totalCosts.value.toMutableList()
                                                    list[index] = list[index].copy(
                                                        value = list[index].value + value
                                                    )
                                                    totalCosts.value = list
                                                }else{
                                                    totalCosts.value += TotalCost(
                                                        fromUser = userCost.user!!,
                                                        toUser = payer,
                                                        value = value
                                                    )
                                                }

                                            }
                                        }
                                    }else{
                                        it.userCosts.filter { cost-> cost.user?.id != it.payer.id }.forEach {
                                            if (it.user?.id in travel.users.map { it.id }){
                                                if (totalCosts.value.any { any-> any.fromUser.id == it.user?.id && any.toUser.id == payer.id }){
                                                    val find = totalCosts.value.find { any-> any.fromUser.id == it.user?.id && any.toUser.id == payer.id }
                                                    val index = totalCosts.value.indexOf(find)
                                                    val list = totalCosts.value.toMutableList()
                                                    list[index] = list[index].copy(
                                                        value = list[index].value + it.toIRT()
                                                    )
                                                    totalCosts.value = list
                                                }else{
                                                    totalCosts.value += TotalCost(
                                                        fromUser = it.user!!,
                                                        toUser = payer,
                                                        value = it.toIRT()
                                                    )
                                                }
                                            }
                                        }
                                    }

                                }
                            }
                            travel.debts.forEach {
                                val conditionToPlus = { toUser:UiUser,fromUser:UiUser->
                                    toUser.id == it.debtFrom?.id && fromUser.id == it.debtTo?.id
                                }
                                if (totalCosts.value.any { conditionToPlus(it.toUser,it.fromUser)}){
                                    //plus to previous
                                    val list = totalCosts.value.toMutableList()
                                    val find = list.find { conditionToPlus(it.toUser,it.fromUser) }
                                    val index = list.indexOf(find)
                                    list[index] = list[index].copy(
                                        value = (list[index].value)+((it.debt.toDoubleOrNull() ?: 0.0).toIRT(it.debtType))
                                    )
                                    totalCosts.value = list
                                }else{
                                    //add
                                    totalCosts.value += TotalCost(
                                        toUser = it.debtFrom!!,
                                        fromUser = it.debtTo!!,
                                        value = (it.debt.toDoubleOrNull() ?: 0.0).toIRT(it.debtType)
                                    )
                                }
                            }
                        }

                        travel.costs.forEach {
                            if (it.payer != null) {
                                Text(text = "For ${it.title}:", fontSize = 14.sp, fontWeight = FontWeight.Bold,modifier=Modifier.padding(start = 8.dp))
                                val payer = it.payer
                                if (it.costAmount != null){
                                    it.userCosts.filter { cost-> cost.user?.id != it.payer.id }.forEach { userCost->
                                        if (userCost.user?.id in travel.users.map { it.id }){

                                            val value = if (it.costType == COST_TYPE_STOCK){
                                                //stock
                                                val sumOfStocks = it.userCosts.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
                                                ((it.costAmount.toDoubleOrNull() ?: 0.0) * (userCost.amount.toDoubleOrNull() ?: 0.0))/sumOfStocks
                                            }else{
                                                //percent
                                                ((it.costAmount.toDoubleOrNull() ?: 0.0) * (userCost.amount.toDoubleOrNull() ?: 0.0))/100
                                            }.toIRT(it.costPriceType)
                                            Text(text = "${userCost.user?.name} should pay $value irt to ${payer.name}",modifier=Modifier.padding(start = 16.dp))
                                        }
                                    }
                                }else{
                                    it.userCosts.filter { cost-> cost.user?.id != it.payer.id }.forEach {
                                        if (it.user?.id in travel.users.map { it.id }){
                                            Text(text = "${it.user?.name} should pay ${it.toIRT()} irt to ${payer.name}",modifier=Modifier.padding(start = 16.dp))
                                        }
                                    }
                                }

                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(16.dp))

                        val debts = (travel.debts.filter { it.debtFrom?.id in travel.users.map { it.id } && it.debtTo?.id in travel.users.map { it.id } })

                        if (debts.isNotEmpty()){
                            Text(text = "Debts:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            debts.forEach {
                                Text(text = "${it.debtFrom?.name} lent ${(it.debt.toDoubleOrNull() ?: 0.0).toIRT(it.debtType)} irt to ${it.debtTo?.name}")
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Divider()
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        if (totalCosts.value.isNotEmpty()){
                            Text(text = "In The End:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            totalCosts.value.forEach {
                                Text(text = "${it.fromUser.name} should pay ${it.value} irt to ${it.toUser.name}")
                            }
                        }
                    }

                }
            }
        }
    }
}

data class TotalCost(
    val fromUser:UiUser,
    val toUser:UiUser,
    val value:Double
)