package com.example.pratice_data_1.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pratice_data_1.LocalAppState
import com.example.pratice_data_1.model.UiDebt
import com.example.pratice_data_1.model.UiUser
import com.example.pratice_data_1.model.nextPriceType
import com.example.pratice_data_1.utils.PriceType
import com.example.pratice_data_1.utils.next
import com.example.pratice_data_1.viewModel.DebtViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebtsScreen(travelId: String, viewModel: DebtViewModel = viewModel()) {

    val travel by viewModel.travel.collectAsState()

    val context = LocalContext.current
    val appState = LocalAppState.current

    LaunchedEffect(Unit) {
        viewModel.getTravel(travelId)
    }

    if (travel == null) {
        Box(modifier = Modifier.fillMaxSize())
    } else {
        Scaffold(bottomBar = {
            Button(
                onClick = {
                    viewModel.saveDebts(onToast = {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    }, onSave = {
                        Toast.makeText(context, "Debts Saved.", Toast.LENGTH_SHORT).show()
                        appState.navController.popBackStack()
                    })
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Save debts")
            }
        }, topBar = {
            TopAppBar(title = {

            }, actions = {
                IconButton(onClick = viewModel::addDebt) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = null
                    )
                }
            })
        }) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                Text(
                    text = "${travel?.name} travel debts:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn() {
                    items(travel!!.debts) {
                        Column {
                            DebtItem(
                                debt = it,
                                travelUsers = travel?.users ?: emptyList(),
                                viewModel = viewModel
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Divider()
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun DebtItem(debt: UiDebt, travelUsers: List<UiUser>, viewModel: DebtViewModel) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(8.dp)) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Debt From")
                    Text(text = "Debt To")
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Button(
                        onClick = {
                            viewModel.updateDebt(
                                debt.copy(
                                    debtFrom = travelUsers.next(debt.debtFrom)
                                )
                            )
                        },
                        modifier = Modifier.width(80.dp),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        Text(text = debt.debtFrom?.name.orEmpty())
                    }
                    OutlinedTextField(
                        value = debt.debt,
                        onValueChange = {
                            viewModel.updateDebt(
                                debt.copy(
                                    debt = it
                                )
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        leadingIcon = {
                            Box(modifier = Modifier.padding(horizontal = 4.dp)) {
                                Button(
                                    onClick = {
                                        viewModel.updateDebt(
                                            debt.copy(
                                                debtType = nextPriceType(debt.debtType)
                                            )
                                        )
                                    },
                                    modifier = Modifier.width(80.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 4.dp)
                                ) {
                                    Text(text = debt.debtType)
                                }
                            }
                        },
                    )
                    Button(
                        onClick = {
                            viewModel.updateDebt(
                                debt.copy(
                                    debtTo = travelUsers.next(debt.debtTo)
                                )
                            )
                        },
                        modifier = Modifier.width(80.dp),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        Text(text = debt.debtTo?.name.orEmpty())
                    }

                }
                Spacer(modifier = Modifier.height(4.dp))
                Button(
                    onClick = {
                        viewModel.deleteDebt(debt)
                    }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF4545)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Delete")
                }
            }
        }
    }
}