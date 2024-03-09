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

class TravelCostsViewModel: ViewModel() {

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
                    payer = it.payer?.id ?: -1,
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
    fun addCost(costTitle:String){
        viewModelScope.launch {
            _costs.update {
                (it ?: emptyList())+ CostUiModel(userCosts = emptyList(),title = costTitle, payer = null, payDate = null)
            }
        }
        addLog("a cost with $costTitle title added to ${_travel.value?.name} travel")
    }
    fun deleteCost(cost: Int){
        addLog("${_costs.value?.find { it.id == cost }?.title} cost removed from ${_travel.value?.name} travel")
        viewModelScope.launch {
            _costs.update {
                it?.filter { it.id != cost }
            }
        }

    }
    fun addCostForUser(user:UserEntity,cost:Int){
        viewModelScope.launch {
            _costs.update {
                val instance = it?.toMutableList()
                instance?.let { ins->
                    val index = ins.indexOf(instance.find { it.id == cost })
                    index.let {
                        instance[index] = instance[index].copy(
                            userCosts = instance[index].userCosts+UserCostUiModel(
                                user = user,
                                priceType = UserCostUiModel.PRICE_TYPE_IRT
                            )
                        )
                    }
                }

                instance
            }
        }
        addLog("a cost added for user with ${user.name} name for ${_costs.value?.find { it.id == cost }?.title} cost")
    }

    fun setPayer(cost:Int,payer:UserEntity? = null,payDate:String? = null){
        if (payDate == null){
            addLog("user ${payer?.name} was set as ${_costs.value?.find { it.id == cost }?.title} cost payer in ${travel.value?.name} travel")
        }else{
            addLog("$payDate was set as pay date for ${_costs.value?.find { it.id == cost }?.title} cost in ${travel.value?.name} travel")
        }
        viewModelScope.launch {
            _costs.update {
                val instance = it?.toMutableList()
                instance?.let { ins->
                    val index = ins.indexOf(instance.find { it.id == cost })
                    if (payer != null){
                        instance[index] = instance[index].copy(payer = payer)
                    }else{
                        instance[index] = instance[index].copy(payDate = payDate)
                    }
                }
                instance
            }
        }
    }

    fun updateCostForUser(newCostForUser:UserCostUiModel,cost: Int){
        viewModelScope.launch {
            _costs.update {
                val instance = it?.toMutableList()
                instance?.let { ins->
                    val index = ins.indexOf(instance.find { it.id == cost })
                    index.let {
                        val userCosts = instance[index].userCosts.toMutableList()
                        val userCostsIndex = userCosts.indexOf(userCosts.find { it.id == newCostForUser.id })
                        userCosts[userCostsIndex] = newCostForUser
                        instance[index] = instance[index].copy(userCosts = userCosts)
                    }
                }

                instance
            }
        }
    }
    fun deleteCostForUser(costForUser:UserCostUiModel,cost: Int){
        addLog("a cost removed for user with ${costForUser.user.name} name for ${_costs.value?.find { it.id == cost }?.title} cost")
        viewModelScope.launch {
            _costs.update {
                val instance = it?.toMutableList()
                instance?.let { ins->
                    val index = ins.indexOf(instance.find { it.id == cost })
                    index.let {
                        val userCosts = instance[index].userCosts.toMutableList()
                        instance[index] = instance[index].copy(userCosts = userCosts.filter { it.id != costForUser.id })
                    }
                }

                instance
            }
        }
    }

    fun saveCosts(){
        _costs
            .value
            ?.filter { it.userCosts.isNotEmpty() && it.userCosts.all { it.amount.toDoubleOrNull() != null }}
            .also {
                val log = if (_costs.value == _travel.value?.costs){
                    "${_travel.value?.name} travel costs saved with same values"
                }else{
                    "${_travel.value?.name} travel costs saved"
                }
                it?.let {costs->
                    updateTravel(_travel.value!!.copy(
                        costs = costs
                    ),
                        log = log)
                }
            }
    }

}