package com.example.pratice_data_1.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.pratice_data_1.db.entities.TravelEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface TravelDao {
    @Insert
    fun addTravel(travel:TravelEntity)

    @Query("SELECT * FROM travels")
    fun getTravels():Flow<List<TravelEntity>>

    @Query("SELECT * FROM travels WHERE id=:id")
    fun getTravel(id:Int):Flow<TravelEntity>

    @Update
    fun updateTravel(travel: TravelEntity)
}