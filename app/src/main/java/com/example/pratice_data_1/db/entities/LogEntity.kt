package com.example.pratice_data_1.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "logs")
data class LogEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int? = null,
    val log:String
)
