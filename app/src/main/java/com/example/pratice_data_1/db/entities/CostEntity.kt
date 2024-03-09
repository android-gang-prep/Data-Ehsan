package com.example.pratice_data_1.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.TypeConverters
import com.example.pratice_data_1.db.converters.ListConverter


@Entity(tableName = "costs")
data class CostEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int? = null,
    val title:String,
    val userCosts:List<Int>,
    val payer:Int,
    val payDate:String?
)
