package com.example.pratice_data_1.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pratice_data_1.db.AppDatabase
import com.example.pratice_data_1.db.addLog
import com.example.pratice_data_1.db.entities.CostEntity
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

class TravelViewModel: ViewModel() {

    private val _users = MutableStateFlow(emptyList<UserEntity>())
    val users = _users.asStateFlow()

    private val _travel = MutableStateFlow<TravelUiModel?>(null)
    val travel = _travel.asStateFlow()


    private val _costs = MutableStateFlow<List<CostUiModel>?>(null)
    val costs = _costs.asStateFlow()

    private val travelDao = AppDatabase.instance.travelDao()
    private val userDao = AppDatabase.instance.userDao()
    private val costDao = AppDatabase.instance.costDao()
    private val userCostDao = AppDatabase.instance.userCostDao()

    init {
        getUsers()
    }

    fun getUsers(){
        viewModelScope.launch(Dispatchers.IO){
            userDao.getUsers().collect{users->
                _users.update { users }
            }
        }
    }
    fun getTravel(travelId:Int){
        viewModelScope.launch(Dispatchers.IO) {
            travelDao.getTravel(travelId).collect { travel ->
                travel.also {
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

                   _travel.update { previousState->
                       TravelUiModel(
                           id = it.id ?: 0,
                           users = users,
                           costs = userCosts,
                           name = it.name,
                       )
                   }
                    if (_costs.value == null){
                        _costs.update { userCosts }
                    }
                }
            }
        }

    }
    fun updateTravel(newTravel: TravelUiModel,log:String){
        viewModelScope.launch(Dispatchers.IO){
            _travel.value?.costs?.forEach {
                it.userCosts.forEach {
                    userCostDao.deleteById(it.id)
                }
                costDao.deleteById(it.id)
            }

            val costs = mutableListOf<Int>()
            newTravel.costs.forEach {
                val userCosts = mutableListOf<Int>()
                it.userCosts.forEach {
                    userCostDao.addUserCost(listOf(UserCostEntity(
                        user = it.user.id ?: 0,
                        amount = it.amount.toDoubleOrNull() ?: 0.0,
                        priceType = it.priceType
                    ))).also {
                        it.firstOrNull()?.let {
                            userCosts.add(it.toInt())
                        }
                    }
                }
                costDao.addCosts(listOf(CostEntity(
                    userCosts = userCosts,
                    title = it.title,
                    payer = if (it.payer == null) -1 else it.payer.id!!,
                    payDate = it.payDate
                ))).also {
                    it.firstOrNull()?.let {
                        costs.add(it.toInt())
                    }
                }
            }
            travelDao.updateTravel(newTravel.toEntity(costs = costs))
            addLog(log)
        }
    }
}