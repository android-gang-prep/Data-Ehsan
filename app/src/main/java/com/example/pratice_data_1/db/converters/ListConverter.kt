package com.example.pratice_data_1.db.converters

import androidx.room.TypeConverter


class ListConverter {

    @TypeConverter
    fun fromString(value:String):List<Int>{
        return if (value == "null") emptyList() else value.split("-").map { it.toInt() }
    }

    @TypeConverter
    fun toString(value:List<Int>):String{
        return if (value.isEmpty()) "null" else value.joinToString("-")
    }
}