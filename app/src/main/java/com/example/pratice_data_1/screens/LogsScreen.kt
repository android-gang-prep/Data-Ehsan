package com.example.pratice_data_1.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pratice_data_1.viewModel.LogViewModel

@Composable
fun LogsScreen(viewModel: LogViewModel = viewModel()) {

    val logs by viewModel.logs.collectAsState()

    LazyColumn(modifier=Modifier.fillMaxSize()) {
        item {
            Button(onClick = viewModel::deleteLogs,modifier=Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
                Text(text = "Clear logs")
            }
        }
        item { Spacer(modifier = Modifier.height(12.dp)) }
        items(logs){
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = it.log)
            Spacer(modifier = Modifier.height(2.dp))
            Divider()
        }
    }
}