package com.example.pratice_data_1.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pratice_data_1.db.AppDatabase
import com.example.pratice_data_1.db.addLog
import com.example.pratice_data_1.db.entities.TravelEntity
import com.example.pratice_data_1.db.entities.UserEntity
import com.example.pratice_data_1.model.CostUiModel
import com.example.pratice_data_1.model.TravelUiModel
import com.example.pratice_data_1.model.UserCostUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TravelsViewModel: ViewModel() {

    private val _travels = MutableStateFlow(emptyList<TravelUiModel>())
    val travels = _travels.asStateFlow()

    private val _users = MutableStateFlow(emptyList<UserEntity>())
    val users = _users.asStateFlow()

    private val travelDao = AppDatabase.instance.travelDao()
    private val userDao = AppDatabase.instance.userDao()
    private val costDao = AppDatabase.instance.costDao()
    private val userCostDao = AppDatabase.instance.userCostDao()

    init {
        getTravels()
        getUsers()
    }

    private fun getUsers(){
        viewModelScope.launch(Dispatchers.IO){
            userDao.getUsers().collect{users->
                _users.update { users }
            }
        }
    }

    private fun getTravels() {
        viewModelScope.launch(Dispatchers.IO) {
            travelDao.getTravels().collect { travels ->
                val mappedTravels = mutableListOf<TravelUiModel>()
                travels.forEach {
                    val costs = it.costs.map {
                        costDao.getCost(it)
                    }
                    val userCosts = mutableListOf<CostUiModel>()
                    costs.forEach {
                        userCosts.add(
                            CostUiModel(
                                id = it.id ?: 0,
                                userCosts = it.userCosts.map {
                                    val userCost = userCostDao.getUserCost(it)
                                    UserCostUiModel(
                                        id = it,
                                        amount = userCost.amount.toString(),
                                        user = userDao.getUser(userCost.user),
                                        priceType = userCost.priceType
                                    )
                                },
                                title = it.title,
                                payer = if (it.payer == -1) null else userDao.getUser(it.payer),
                                payDate = it.payDate
                            )
                        )
                    }
                    val users = it.users.map {
                        userDao.getUser(it)
                    }

                    mappedTravels.add(
                        TravelUiModel(
                            id = it.id ?: 0,
                            users = users,
                            costs = userCosts,
                            name = it.name,
                        )
                    )
                }
                _travels.update { mappedTravels }
            }
        }
    }
    fun addTravel(travelName:String){
        viewModelScope.launch(Dispatchers.IO){
            travelDao.addTravel(TravelEntity(
                name = travelName,
                users = emptyList(),
                costs = emptyList(),
            ))
        }
        addLog("A Travel added with $travelName name")
    }


}