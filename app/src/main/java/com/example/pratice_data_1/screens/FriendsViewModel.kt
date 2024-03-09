package com.example.pratice_data_1.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pratice_data_1.db.AppDatabase
import com.example.pratice_data_1.db.addLog
import com.example.pratice_data_1.db.entities.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FriendsViewModel: ViewModel() {

    val userDao = AppDatabase.instance.userDao()

    private val _users = MutableStateFlow(emptyList<UserEntity>())
    val users = _users.asStateFlow()

    init {
        getFriends()
    }

    fun addFriend(friendName:String){
        viewModelScope.launch(Dispatchers.IO){
            userDao.addUsers(listOf(UserEntity(name = friendName)))
        }
        addLog("A friend added with $friendName name")
    }
    fun getFriends(){
        viewModelScope.launch(Dispatchers.IO){
            userDao.getUsers().collect{users->
                _users.update { users }
            }
        }
    }

}