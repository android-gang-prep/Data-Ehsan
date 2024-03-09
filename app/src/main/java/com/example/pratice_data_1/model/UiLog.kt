package com.example.pratice_data_1.model

import com.example.pratice_data_1.db.entities.Log

data class UiLog(val id:String,val log:String){
    fun toEntity():Log{
        return Log().apply {
            id = this@UiLog.id
            log = this@UiLog.log
        }
    }
}
