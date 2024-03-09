package com.example.pratice_data_1.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pratice_data_1.db.entities.User
import com.example.pratice_data_1.model.UiUser
import com.example.pratice_data_1.repositories.UserRepository
import com.example.pratice_data_1.repositories.addLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FriendsViewModel: ViewModel(),KoinComponent {

    private val userRepo by inject<UserRepository>()

    private val _users = MutableStateFlow(emptyList<UiUser>())
    val users = _users.asStateFlow()

    init {
        getFriends()
    }

    fun addFriend(friendName:String){
        viewModelScope.launch(Dispatchers.IO){
            userRepo.addUser(UiUser(name = friendName))
            addLog("A friend added with $friendName name")
        }
    }
    fun getFriends(){
        viewModelScope.launch(Dispatchers.IO){
            userRepo.getUsers().collect{users->
                _users.update { users }
            }
        }
    }

}