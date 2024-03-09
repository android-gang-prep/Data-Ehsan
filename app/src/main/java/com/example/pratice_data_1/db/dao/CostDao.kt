package com.example.pratice_data_1.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.pratice_data_1.db.entities.CostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CostDao {
    @Insert
    fun addCosts(costs:List<CostEntity>):List<Long>

    @Query("SELECT * FROM costs WHERE id=:id")
    fun getCost(id:Int):CostEntity

    @Query("DELETE FROM costs WHERE id=:id")
    fun deleteById(id:Int)
}