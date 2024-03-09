package com.example.pratice_data_1.model

import com.example.pratice_data_1.db.entities.UserEntity

data class CostUiModel(
    val id:Int = (0..Int.MAX_VALUE).random(),
    val title:String,
    val userCosts:List<UserCostUiModel>,
    val payer:UserEntity?,
    val payDate:String?
)
