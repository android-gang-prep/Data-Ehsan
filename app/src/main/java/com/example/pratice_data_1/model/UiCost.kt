package com.example.pratice_data_1.model

import com.example.pratice_data_1.db.entities.COST_TYPE_AMOUNT
import com.example.pratice_data_1.db.entities.Cost
import com.example.pratice_data_1.utils.PriceType
import io.realm.kotlin.ext.toRealmList
import java.util.UUID

data class UiCost(
    val id:String = UUID.randomUUID().toString(),
    val title:String,
    val userCosts:List<UiUserCost> = emptyList(),
    val payer:UiUser? = null,
    val payDate:String? = null,
    val costType:String = COST_TYPE_AMOUNT,
    val costAmount:String? = null,
    val costPriceType:String = PriceType.PRICE_TYPE_IRT,
){
    fun toEntity():Cost{
        return Cost().apply {
            id = this@UiCost.id
            title = this@UiCost.title
            userCosts = this@UiCost.userCosts.map { it.toEntity() }.toRealmList()
            payer = this@UiCost.payer?.toEntity()
            payDate = this@UiCost.payDate
            costType = this@UiCost.costType
            costAmount = this@UiCost.costAmount
            costPriceType = this@UiCost.costPriceType
        }
    }
}

