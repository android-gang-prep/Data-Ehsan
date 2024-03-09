package com.example.pratice_data_1.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.pratice_data_1.db.entities.UserCostEntity


@Dao
interface UserCostDao {
    @Insert
    fun addUserCost(userCosts:List<UserCostEntity>):List<Long>
    @Query("SELECT * FROM user_cost WHERE id=:id")
    fun getUserCost(id:Int):UserCostEntity

    @Query("DELETE FROM user_cost WHERE id=:id")
    fun deleteById(id:Int)
}