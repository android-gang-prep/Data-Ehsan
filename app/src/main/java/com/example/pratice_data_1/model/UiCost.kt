package com.example.pratice_data_1.model

import com.example.pratice_data_1.db.entities.Cost
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.types.RealmObject
import java.util.UUID

data class UiCost(
    val id:String = UUID.randomUUID().toString(),
    val title:String,
    val userCosts:List<UiUserCost> = emptyList(),
    val payer:UiUser? = null,
    val payDate:String? = null
){
    fun toEntity():Cost{
        return Cost().apply {
            id = this@UiCost.id
            title = this@UiCost.title
            userCosts = this@UiCost.userCosts.map { it.toEntity() }.toRealmList()
            payer = this@UiCost.payer?.toEntity()
            payDate = this@UiCost.payDate
        }
    }
}

