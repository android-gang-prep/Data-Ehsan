package com.example.pratice_data_1.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.pratice_data_1.db.converters.ListConverter


@Entity(tableName = "travels")
data class TravelEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int? = null,
    val name:String,
    val users:List<Int>,
    val costs:List<Int>,
)
