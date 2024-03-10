package com.example.pratice_data_1.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pratice_data_1.db.entities.Debt
import com.example.pratice_data_1.model.UiDebt
import com.example.pratice_data_1.model.UiTravel
import com.example.pratice_data_1.repositories.TravelRepository
import com.example.pratice_data_1.repositories.addLog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DebtViewModel: ViewModel(),KoinComponent {

    private val _travel = MutableStateFlow<UiTravel?>(null)
    val travel = _travel.asStateFlow()

    private val travelRepo:TravelRepository by inject()

    fun getTravel(travelId:String){
        viewModelScope.launch {
            travelRepo.getTravel(travelId).collect{result->
                _travel.update { result }
            }
        }
    }

    fun addDebt(){
        _travel.update {
            var instance = it
            instance?.let {
                val debts = it.debts.toMutableList()
                debts.add(UiDebt(
                    debtFrom = travel.value?.users?.first(),
                    debtTo = travel.value?.users?.last(),
                ))
                instance = instance?.copy(debts = debts)
            }
            instance
        }
        viewModelScope.launch {
            addLog("a debt added to ${travel.value?.name} travel")
        }
    }
    fun deleteDebt(debt: UiDebt){
        _travel.update {
            var instance = it
            instance?.let {
                val debts = it.debts.toMutableList()
                val index = debts.indexOf(debts.find { it.id == debt.id })
                debts.removeAt(index)
                instance = instance?.copy(debts = debts)
            }
            instance
        }
        viewModelScope.launch {
            addLog("a debt removed from ${travel.value?.name} travel")
        }
    }
    fun updateDebt(newDebt: UiDebt){
        _travel.update {
            var instance = it
            instance?.let {
                val debts = it.debts.toMutableList()
                val index = debts.indexOf(debts.find { it.id == newDebt.id })
                debts[index] = newDebt
                instance = instance?.copy(debts = debts)
            }
            instance
        }
    }

    fun saveDebts(onToast:(String)->Unit,onSave:()->Unit){
        viewModelScope.launch {
            _travel.value?.let {
                val debts = it.debts
                var haveError = false
                debts.forEach {
                    if (it.debtFrom?.id == it.debtTo?.id){
                        onToast("You cant borrow from yourself :|")
                        haveError = true
                    }
                    if ((it.debt.toDoubleOrNull() ?: 0.0) == 0.0){
                        onToast("You cannot borrow 0")
                        haveError =true
                    }
                }
                if (!haveError){
                    println(it.debts)
                    travelRepo.updateTravel(it)
                    onSave()
                }
            }
        }
        viewModelScope.launch {
            addLog("${travel.value?.name} travel debts saved")
        }
    }
}