package com.example.pratice_data_1.model

import com.example.pratice_data_1.db.entities.UserCost
import com.example.pratice_data_1.utils.PriceType
import java.util.UUID

data class UiUserCost(
    val id:String = UUID.randomUUID().toString(),
    val user:UiUser?,
    val amount:String = "0.0",
    val priceType:String
){
    fun toEntity():UserCost{
        return UserCost().apply {
            id = this@UiUserCost.id
            user = this@UiUserCost.user?.toEntity()
            amount = this@UiUserCost.amount
            priceType = this@UiUserCost.priceType
        }
    }
}

fun nextPriceType(current:String):String{
    return when(current){
        PriceType.PRICE_TYPE_IRT->{
            PriceType.PRICE_TYPE_DOLLAR
        }
        PriceType.PRICE_TYPE_DOLLAR->{
            PriceType.PRICE_TYPE_EURO
        }
        PriceType.PRICE_TYPE_EURO->{
            PriceType.PRICE_TYPE_IRT
        }
        else->{
            PriceType.PRICE_TYPE_IRT}
    }
}
