package com.example.pratice_data_1.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pratice_data_1.db.entities.Cost
import com.example.pratice_data_1.db.entities.Travel
import com.example.pratice_data_1.db.entities.User
import com.example.pratice_data_1.db.entities.UserCost
import com.example.pratice_data_1.model.UiCost
import com.example.pratice_data_1.model.UiTravel
import com.example.pratice_data_1.model.UiUser
import com.example.pratice_data_1.model.UiUserCost
import com.example.pratice_data_1.repositories.CostRepository
import com.example.pratice_data_1.repositories.TravelRepository
import com.example.pratice_data_1.repositories.UserRepository
import com.example.pratice_data_1.repositories.addLog
import com.example.pratice_data_1.utils.PriceType
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.ext.toRealmList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TravelCostsViewModel : ViewModel(), KoinComponent {

    private val _users = MutableStateFlow(emptyList<UiUser>())
    val users = _users.asStateFlow()

    private val _travel = MutableStateFlow<UiTravel?>(null)
    val travel = _travel.asStateFlow()


    private val _costs = MutableStateFlow<List<UiCost>?>(null)
    val costs = _costs.asStateFlow()

    private val userRepo: UserRepository by inject()
    private val travelRepo: TravelRepository by inject()
    private val costRepo: CostRepository by inject()

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
        viewModelScope.launch(Dispatchers.IO) {
            travelRepo.updateTravel(newTravel)
            addLog(log)
        }
    }

    fun addCost(costTitle: String) {
        viewModelScope.launch {
            _costs.update {
                (it ?: emptyList()) + UiCost(title = costTitle)
            }
            addLog("a cost with $costTitle title added to ${_travel.value?.name} travel")
        }
    }

    fun deleteCost(cost: String) {
        viewModelScope.launch {
            addLog("${_costs.value?.find { it.id == cost }?.title} cost removed from ${_travel.value?.name} travel")
            _costs.update {
                it?.filter { it.id != cost }
            }
        }

    }

    fun addCostForUser(user: UiUser, cost: String) {
        viewModelScope.launch {
            _costs.update {
                val instance = it?.toMutableList()
                instance?.let { ins ->
                    val index = ins.indexOf(instance.find { it.id == cost })
                    index.let {
                        instance[index] = instance[index].copy(userCosts = instance[index].userCosts + UiUserCost(user = user, priceType = PriceType.PRICE_TYPE_IRT))
                    }
                }

                instance
            }
            addLog("a cost added for user with ${user.name} name for ${_costs.value?.find { it.id == cost }?.title} cost")
        }
    }

    fun setPayer(cost: String, payer: UiUser? = null, payDate: String? = null) {
        viewModelScope.launch {
            if (payDate == null) {
                addLog("user ${payer?.name} was set as ${_costs.value?.find { it.id == cost }?.title} cost payer in ${travel.value?.name} travel")
            } else {
                addLog("$payDate was set as pay date for ${_costs.value?.find { it.id == cost }?.title} cost in ${travel.value?.name} travel")
            }
            _costs.update {
                val instance = it?.toMutableList()
                instance?.let { ins ->
                    val index = ins.indexOf(instance.find { it.id == cost })
                    if (payer != null) {
                        instance[index] = instance[index].copy(payer = payer)
                    } else {
                        instance[index] = instance[index].copy(payDate = payDate)
                    }
                }
                instance
            }
        }
    }

    fun updateCostForUser(newCostForUser: UiUserCost, cost: String) {
        viewModelScope.launch {
            _costs.update {
                val instance = it?.toMutableList()
                instance?.let { ins ->
                    val index = ins.indexOf(instance.find { it.id == cost })
                    index.let {
                        val userCosts = instance[index].userCosts.toMutableList()
                        val userCostsIndex =
                            userCosts.indexOf(userCosts.find { it.id == newCostForUser.id })
                        userCosts[userCostsIndex] = newCostForUser
                        instance[index] =
                            instance[index].copy(userCosts = userCosts)
                    }
                }

                instance
            }
        }
    }

    fun deleteCostForUser(costForUser: UiUserCost, cost: String) {
        viewModelScope.launch {
            addLog("a cost removed for user with ${costForUser.user?.name} name for ${_costs.value?.find { it.id == cost }?.title} cost")
            _costs.update {
                val instance = it?.toMutableList()
                instance?.let { ins ->
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

    fun saveCosts() {
        _costs
            .value
            ?.filter { it.userCosts.isNotEmpty() && it.userCosts.all { it.amount.toDoubleOrNull() != null } }
            .also {
                val log = if (_costs.value == _travel.value?.costs) {
                    "${_travel.value?.name} travel costs saved with same values"
                } else {
                    "${_travel.value?.name} travel costs saved"
                }
                it?.let { costs ->
                    updateTravel(
                        _travel.value!!.copy(costs = costs) ,
                        log = log
                    )
                }
            }
    }

}