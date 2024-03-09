package com.example.pratice_data_1.model

import com.example.pratice_data_1.db.entities.CostEntity
import com.example.pratice_data_1.db.entities.TravelEntity
import com.example.pratice_data_1.db.entities.UserEntity

data class TravelUiModel(
    val id:Int,
    val name:String,
    val users:List<UserEntity>,
    val costs:List<CostUiModel>
){
    fun toEntity(costs:List<Int>):TravelEntity{
        return TravelEntity(
            id = id,
            name = name,
            users = users.map { it.id ?: 0 },
            costs = costs
        )
    }
}
