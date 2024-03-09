package com.example.pratice_data_1.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pratice_data_1.db.AppDatabase
import com.example.pratice_data_1.db.entities.CostEntity
import com.example.pratice_data_1.db.entities.TravelEntity
import com.example.pratice_data_1.db.entities.UserCostEntity
import com.example.pratice_data_1.db.entities.UserEntity
import com.example.pratice_data_1.model.CostUiModel
import com.example.pratice_data_1.model.TravelUiModel
import com.example.pratice_data_1.model.UserCostUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.cos

class HomeViewModel : ViewModel() {

    private val _users = MutableStateFlow(emptyList<Long>())
    val users = _users.asStateFlow()

    private val _travels = MutableStateFlow(emptyList<TravelUiModel>())
    val travels = _travels.asStateFlow()

    private val travelDao = AppDatabase.instance.travelDao()
    private val userDao = AppDatabase.instance.userDao()
    private val costDao = AppDatabase.instance.costDao()
    private val userCostDao = AppDatabase.instance.userCostDao()


    fun addTravel(travel: TravelEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            travelDao.addTravel(travel)
        }
    }

    fun addUsers(users: List<UserEntity>) {
        viewModelScope.launch(Dispatchers.IO) {
            userDao.addUsers(users).also { result ->
                _users.update { result }
            }
        }
    }

    fun addCosts(costs: List<CostEntity>, onAdd: (List<Long>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            costDao.addCosts(costs).also(onAdd)
        }
    }

    fun addUserCosts(userCosts: List<UserCostEntity>, onAdd: (List<Long>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            userCostDao.addUserCost(userCosts).also(onAdd)
        }
    }

}