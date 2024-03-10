package com.example.pratice_data_1.model

import com.example.pratice_data_1.db.entities.Travel
import io.realm.kotlin.ext.toRealmList
import java.util.UUID

data class UiTravel(
    val id:String = UUID.randomUUID().toString(),
    val name:String,
    val users:List<UiUser> = emptyList(),
    val costs:List<UiCost> = emptyList(),
    val debts:List<UiDebt> = emptyList()
){
    fun toEntity():Travel{
        return Travel().apply {
            id = this@UiTravel.id
            name = this@UiTravel.name
            users = this@UiTravel.users.map { it.toEntity() }.toRealmList()
            costs = this@UiTravel.costs.map { it.toEntity() }.toRealmList()
            debts = this@UiTravel.debts.map { it.toEntity() }.toRealmList()
        }
    }
}
