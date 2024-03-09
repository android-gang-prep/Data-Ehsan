package com.example.pratice_data_1.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pratice_data_1.db.entities.Log
import com.example.pratice_data_1.model.UiLog
import com.example.pratice_data_1.repositories.LogRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LogViewModel: ViewModel(),KoinComponent {
    private val _logs = MutableStateFlow(emptyList<UiLog>())
    val logs = _logs.asStateFlow()

    private val repository:LogRepository by inject()

    init {
        getLogs()
    }

    private fun getLogs(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getLogs().collect{logs->
                _logs.update { logs }
            }
        }
    }
    fun deleteLogs(){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteLogs()
        }
    }
}