package com.example.pratice_data_1.utils

import com.example.pratice_data_1.db.entities.UserCost
import com.example.pratice_data_1.model.UiUserCost
import io.realm.kotlin.types.RealmObject

class PriceType{
    companion object{
        const val PRICE_TYPE_DOLLAR = "dollar"
        const val PRICE_TYPE_EURO = "euro"
        const val PRICE_TYPE_IRT = "irt"

        const val IRT_PER_DOLLAR = 60900
        const val IRT_PER_EURO = 66550
    }
}

fun UiUserCost.toIRT():Double{
    val doubleAmount = amount.toDoubleOrNull() ?: 0.0
    return when(priceType){
        PriceType.PRICE_TYPE_DOLLAR->{
            doubleAmount * PriceType.IRT_PER_DOLLAR
        }
        PriceType.PRICE_TYPE_EURO->{
            doubleAmount * PriceType.IRT_PER_EURO
        }
        else->{
            doubleAmount
        }
    }
}