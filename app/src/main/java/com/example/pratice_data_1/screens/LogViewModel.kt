package com.example.pratice_data_1.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pratice_data_1.db.AppDatabase
import com.example.pratice_data_1.db.entities.LogEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LogViewModel: ViewModel() {
    private val _logs = MutableStateFlow(emptyList<LogEntity>())
    val logs = _logs.asStateFlow()

    val dao = AppDatabase.instance.logDao()

    init {
        getLogs()
    }

    fun getLogs(){
        viewModelScope.launch(Dispatchers.IO) {
            dao.getLogs().collect{logs->
                _logs.update { logs }
            }
        }
    }
    fun deleteLogs(){
        viewModelScope.launch(Dispatchers.IO){
            dao.deleteLogs()
        }
    }
}