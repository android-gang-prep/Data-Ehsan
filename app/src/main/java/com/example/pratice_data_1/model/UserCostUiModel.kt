package com.example.pratice_data_1.model

import androidx.room.PrimaryKey
import com.example.pratice_data_1.db.entities.UserEntity

data class UserCostUiModel(
    val id:Int = (0..Int.MAX_VALUE).random(),
    val user:UserEntity,
    val amount:String = "0.0",
    val priceType:String
){
    companion object{
        const val PRICE_TYPE_DOLLAR = "dollar"
        const val PRICE_TYPE_EURO = "euro"
        const val PRICE_TYPE_IRT = "irt"

        const val IRT_PER_DOLLAR = 60900
        const val IRT_PER_EURO = 66550
    }

    fun toIRT(): Double {
        val value = when (priceType) {
            PRICE_TYPE_IRT -> {
                amount.toDoubleOrNull() ?: 0.0
            }

            PRICE_TYPE_EURO -> {
                (amount.toDoubleOrNull()
                    ?: 0.0) * UserCostUiModel.IRT_PER_EURO
            }

            PRICE_TYPE_DOLLAR -> {
                (amount.toDoubleOrNull()
                    ?: 0.0) * UserCostUiModel.IRT_PER_DOLLAR
            }

            else -> 0.0
        }
        return value
    }
}
