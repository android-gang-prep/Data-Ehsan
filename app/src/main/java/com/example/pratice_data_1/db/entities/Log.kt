package com.example.pratice_data_1.db.entities

import com.example.pratice_data_1.model.UiLog
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID


class Log:RealmObject{
    @PrimaryKey
    var id:String = UUID.randomUUID().toString()
    var log:String = ""

    fun toUiModel():UiLog{
        return UiLog(id = id,log = log)
    }
}
