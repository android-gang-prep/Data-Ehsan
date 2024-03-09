package com.example.pratice_data_1.db.entities

import com.example.pratice_data_1.model.UiUser
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID

class User:RealmObject{
    @PrimaryKey
    var id:String = UUID.randomUUID().toString()
    var name:String = ""

    fun toUiModel():UiUser{
        return UiUser(
            id = id,
            name = name
        )
    }
}
