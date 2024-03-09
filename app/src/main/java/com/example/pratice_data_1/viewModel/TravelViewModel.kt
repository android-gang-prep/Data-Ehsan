package com.example.pratice_data_1.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pratice_data_1.db.entities.Cost
import com.example.pratice_data_1.db.entities.Travel
import com.example.pratice_data_1.db.entities.User
import com.example.pratice_data_1.model.UiCost
import com.example.pratice_data_1.model.UiTravel
import com.example.pratice_data_1.model.UiUser
import com.example.pratice_data_1.repositories.TravelRepository
import com.example.pratice_data_1.repositories.UserRepository
import com.example.pratice_data_1.repositories.addLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TravelViewModel : ViewModel(), KoinComponent {

    private val _users = MutableStateFlow(emptyList<UiUser>())
    val users = _users.asStateFlow()

    private val _travel = MutableStateFlow<UiTravel?>(null)
    val travel = _travel.asStateFlow()


    private val _costs = MutableStateFlow<List<UiCost>?>(null)
    val costs = _costs.asStateFlow()


    private val userRepo: UserRepository by inject()
    private val travelRepo: TravelRepository by inject()

    init {
        getUsers()
    }

    fun getUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            userRepo.getUsers().collect { users ->
                _users.update { users }
            }
        }
    }

    fun getTravel(travelId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            travelRepo.getTravel(travelId).collect {
                it?.also { travel ->
                    _travel.update { travel }
                    if (_costs.value == null) {
                        _costs.update { travel.costs }
                    }
                }
            }
        }
    }
    fun updateTravel(newTravel: UiTravel, log: String) {
        println("view model id: $newTravel")
        viewModelScope.launch(Dispatchers.IO) {
            travelRepo.updateTravel(newTravel)
            addLog(log)
        }
    }
}