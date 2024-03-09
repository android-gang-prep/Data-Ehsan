package com.example.pratice_data_1.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.pratice_data_1.db.entities.UserEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface UserDao {
    @Insert
    fun addUsers(users:List<UserEntity>):List<Long>
    @Query("SELECT * FROM users WHERE id=:id")
    fun getUser(id:Int):UserEntity

    @Query("SELECT * FROM users")
    fun getUsers():Flow<List<UserEntity>>
}