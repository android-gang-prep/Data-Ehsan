package com.example.pratice_data_1.model

import com.example.pratice_data_1.db.entities.User
import java.util.UUID

data class UiUser(val id:String=UUID.randomUUID().toString(),val name:String){
    fun toEntity():User{
        return User().apply {
            id = this@UiUser.id
            name = this@UiUser.name
        }
    }
}
