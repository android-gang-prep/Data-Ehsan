package com.example.pratice_data_1.db.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pratice_data_1.model.UserCostUiModel


@Entity(tableName = "user_cost")
data class UserCostEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int? = null,
    val user:Int,
    val amount:Double,
    val priceType:String
)
