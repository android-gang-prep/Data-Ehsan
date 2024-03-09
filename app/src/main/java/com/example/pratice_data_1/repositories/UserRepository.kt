package com.example.pratice_data_1.repositories

import com.example.pratice_data_1.db.entities.User
import com.example.pratice_data_1.model.UiUser
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepository(private val realm: Realm ) {
    suspend fun addUser(user:UiUser):UiUser{
        return realm.write {
            copyToRealm(user.toEntity())
        }.toUiModel()
    }
    fun getUsers():Flow<List<UiUser>>{
        return realm.query<User>().asFlow().map { it.list.map { it.toUiModel() } }
    }
}