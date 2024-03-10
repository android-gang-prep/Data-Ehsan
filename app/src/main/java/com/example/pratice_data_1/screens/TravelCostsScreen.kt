package com.example.pratice_data_1.screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pratice_data_1.LocalAppState
import com.example.pratice_data_1.db.entities.COST_TYPE_AMOUNT
import com.example.pratice_data_1.db.entities.COST_TYPE_PERCENT
import com.example.pratice_data_1.db.entities.COST_TYPE_STOCK
import com.example.pratice_data_1.model.nextPriceType
import com.example.pratice_data_1.utils.PriceType
import com.example.pratice_data_1.utils.toIRT
import com.example.pratice_data_1.viewModel.TravelCostsViewModel
import java.text.SimpleDateFormat
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelCostsScreen(travelId: String, viewModel: TravelCostsViewModel = viewModel()) {

    val context = LocalContext.current

    LaunchedEffect(travelId) {
        viewModel.getTravel(travelId)
    }

    val appState = LocalAppState.current

    val costs by viewModel.costs.collectAsState()
    val travel by viewModel.travel.collectAsState()
    val users by viewModel.users.collectAsState()

    val selectedCost = remember {
        mutableStateOf("")
    }

    val costDialogOpen = remember {
        mutableStateOf(false)
    }
    val payerDialogOpen = remember {
        mutableStateOf(false)
    }
    if (costDialogOpen.value) {
        val costTitle = remember {
            mutableStateOf("")
        }
        val costTypeExpanded = remember {
            mutableStateOf(false)
        }
        val costType = remember {
            mutableStateOf(COST_TYPE_AMOUNT)
        }
        val costPriceType = remember {
            mutableStateOf(PriceType.PRICE_TYPE_IRT)
        }
        val costAmount = remember {
            mutableStateOf("")
        }
        Dialog(onDismissRequest = { costDialogOpen.value = false }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .padding(12.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Cost Title", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        ExposedDropdownMenuBox(
                            expanded = costTypeExpanded.value,
                            onExpandedChange = {
                                costTypeExpanded.value = it
                            }) {
                            Button(
                                modifier = Modifier
                                    .menuAnchor()
                                    .width(100.dp),
                                onClick = {},
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(text = costType.value)
                            }
                            ExposedDropdownMenu(
                                expanded = costTypeExpanded.value,
                                onDismissRequest = {
                                    costTypeExpanded.value = false
                                }) {
                                DropdownMenuItem(text = {
                                    Text(text = COST_TYPE_AMOUNT)
                                }, onClick = {
                                    costType.value = COST_TYPE_AMOUNT
                                    costTypeExpanded.value = false
                                })
                                DropdownMenuItem(
                                    text = {
                                        Text(text = COST_TYPE_PERCENT)
                                    },
                                    onClick = {
                                        costType.value = COST_TYPE_PERCENT
                                        costTypeExpanded.value = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = {
                                        Text(text = COST_TYPE_STOCK)
                                    },
                                    onClick = {
                                        costType.value = COST_TYPE_STOCK
                                        costTypeExpanded.value = false
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = costTitle.value,
                        placeholder = {
                            Text(text = "Title")
                        },
                        onValueChange = { costTitle.value = it }, modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    AnimatedVisibility(visible = costType.value != COST_TYPE_AMOUNT) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = costAmount.value,
                                placeholder = {
                                    Text(text = "Total Cost")
                                },
                                onValueChange = { costAmount.value = it }, modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Button(onClick = {
                                costPriceType.value = nextPriceType(costPriceType.value)
                            }, shape = RoundedCornerShape(8.dp)) {
                                Text(text = costPriceType.value)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Button(onClick = {
                        if (costTitle.value.isNotBlank()) {
                            if (costType.value != COST_TYPE_AMOUNT) {
                                if (costAmount.value.isNotBlank()) {
                                    viewModel.addCost(
                                        costTitle.value,
                                        costType = costType.value,
                                        costPriceType = costPriceType.value,
                                        costAmount = costAmount.value
                                    )
                                }
                            } else {
                                viewModel.addCost(
                                    costTitle.value,
                                    costType = costType.value,
                                    costPriceType = costPriceType.value,
                                    costAmount = null
                                )
                            }
                        }
                        costDialogOpen.value = false
                    }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
                        Text(text = "Add Cost")
                    }
                }
            }
        }
    }
    if (payerDialogOpen.value) {
        val payDate = remember {
            mutableStateOf("")
        }
        Dialog(onDismissRequest = { payerDialogOpen.value = false }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .padding(12.dp)
            ) {
                Column {
                    Text(
                        text = "Travel Participants:",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        items(travel?.users?.toList() ?: emptyList()) {
                            Button(onClick = {
                                viewModel.setPayer(
                                    cost = selectedCost.value,
                                    payer = it
                                )
                            }, shape = RoundedCornerShape(8.dp)) {
                                Text(text = it.name)
                                Spacer(modifier = Modifier.width(4.dp))
                                AnimatedVisibility(visible = costs!!.find { it.id == selectedCost.value }?.payer?.id == it.id) {
                                    Icon(
                                        imageVector = Icons.Rounded.Check,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "Pay date:")
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = payDate.value,
                        onValueChange = { payDate.value = it },
                        leadingIcon = {
                            Button(onClick = {
                                payDate.value =
                                    SimpleDateFormat("YYYY/MM/dd HH:mm").format(Calendar.getInstance().time)
                            }, shape = RoundedCornerShape(8.dp)) {
                                Text(text = "Now")
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Button(onClick = {
                        if (payDate.value.isNotBlank()) {
                            viewModel.setPayer(
                                cost = selectedCost.value,
                                payDate = payDate.value
                            )
                        }
                        payerDialogOpen.value = false
                    }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
                        Text(text = "Set")
                    }
                }
            }
        }

    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        costDialogOpen.value = true
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f), shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Add Cost")
                }
                Button(
                    onClick = {
                        viewModel.saveCosts(onToast = {
                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        }).also {
                            if (it){
                                Toast.makeText(context, "Costs saved.", Toast.LENGTH_SHORT).show()
                                appState.navController.popBackStack()
                            }
                        }
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f), shape = RoundedCornerShape(8.dp)

                ) {
                    Text(text = "Save Costs")
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Travel costs:",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn {
                itemsIndexed(costs ?: emptyList()) { index, cost ->
                    Column {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Cost ${cost.title}")
                            IconButton(onClick = {
                                viewModel.deleteCost(
                                    cost = cost.id
                                )
                            }) {
                                Icon(
                                    imageVector = Icons.Rounded.Clear,
                                    contentDescription = null,
                                )
                            }
                        }
                        if (cost.payer != null && cost.payDate != null) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "* ${cost.payer.name} was payed in ${cost.payDate}",
                                color = Color(
                                    0xFF1F884F
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        cost.userCosts.filter {
                            it.user?.id in (travel?.users?.map { it.id } ?: emptyList())
                        }
                            .forEach {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        OutlinedTextField(
                                            modifier = Modifier.weight(1f),
                                            value = it.amount,
                                            onValueChange = { value ->
                                                viewModel.updateCostForUser(
                                                    newCostForUser = it.copy(amount = value),
                                                    cost = cost.id
                                                )
                                            },
                                            placeholder = {
                                                Text(text = if (cost.costType == COST_TYPE_AMOUNT) "Amount" else if (cost.costType == COST_TYPE_PERCENT) "Percent" else "Stock")
                                            },
                                            keyboardOptions = KeyboardOptions(
                                                keyboardType = KeyboardType.Decimal
                                            )
                                        )
                                        if (cost.costType == COST_TYPE_AMOUNT){
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Button(
                                                modifier = Modifier
                                                    .width(100.dp),
                                                onClick = {
                                                    viewModel.updateCostForUser(
                                                        cost = cost.id,
                                                        newCostForUser = it.copy(
                                                            priceType = nextPriceType(it.priceType)
                                                        )
                                                    )
                                                },
                                                shape = RoundedCornerShape(8.dp)
                                            ) {
                                                Text(text = it.priceType)
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Button(
                                            modifier = Modifier
                                                .width(100.dp),
                                            onClick = {},
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text(text = it.user?.name.orEmpty())
                                        }
                                    }
                                    if (cost.costType != COST_TYPE_AMOUNT){
                                        val valueForUser = if (cost.costType == COST_TYPE_STOCK){
                                            //stock
                                            val sumOfStocks = cost.userCosts.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
                                            println("sum of stucs: $sumOfStocks")
                                            ((cost.costAmount?.toDoubleOrNull() ?: 0.0) * (it.amount.toDoubleOrNull() ?: 0.0))/sumOfStocks
                                        }else{
                                            //percent
                                            ((cost.costAmount?.toDoubleOrNull() ?: 0.0) * (it.amount.toDoubleOrNull() ?: 0.0))/100
                                        }.toIRT(cost.costPriceType)
                                        Text(text = "$valueForUser IRT")
                                    }

                                }
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                selectedCost.value = cost.id
                                payerDialogOpen.value = true
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(text = "Set payer")
                        }
                        Divider()
                    }
                }
            }
        }

    }
}