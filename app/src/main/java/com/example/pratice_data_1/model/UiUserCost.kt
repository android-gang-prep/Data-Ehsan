package com.example.pratice_data_1.model

import com.example.pratice_data_1.db.entities.UserCost
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
