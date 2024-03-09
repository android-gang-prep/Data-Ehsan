package com.example.pratice_data_1.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pratice_data_1.db.entities.Travel
import com.example.pratice_data_1.db.entities.User
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

class TravelsViewModel: ViewModel(),KoinComponent{

    private val _travels = MutableStateFlow(emptyList<UiTravel>())
    val travels = _travels.asStateFlow()

    private val _users = MutableStateFlow(emptyList<UiUser>())
    val users = _users.asStateFlow()

    private val userRepo:UserRepository by inject()
    private val travelRepo:TravelRepository by inject()

    init {
        getTravels()
        getUsers()
    }

    private fun getUsers(){
        viewModelScope.launch(Dispatchers.IO){
            userRepo.getUsers().collect{users->
                _users.update { users }
            }
        }
    }

    private fun getTravels() {
        viewModelScope.launch(Dispatchers.IO) {
            travelRepo.getTravels().collect { travels ->
                _travels.update { travels }
            }
        }
    }
    fun addTravel(travelName:String){
        viewModelScope.launch(Dispatchers.IO){
            travelRepo.addTravel(UiTravel(name = travelName))
            addLog("A Travel added with $travelName name")
        }
    }


}