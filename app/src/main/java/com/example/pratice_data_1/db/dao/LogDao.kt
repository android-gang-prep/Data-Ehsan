package com.example.pratice_data_1.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.pratice_data_1.db.entities.LogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LogDao {

    @Insert
    fun addLog(log:LogEntity)

    @Query("SELECT * FROM logs")
    fun getLogs():Flow<List<LogEntity>>

    @Query("DELETE FROM logs")
    fun deleteLogs()
}