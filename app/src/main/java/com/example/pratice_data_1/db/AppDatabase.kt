package com.example.pratice_data_1.db

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pratice_data_1.db.converters.ListConverter
import com.example.pratice_data_1.db.dao.CostDao
import com.example.pratice_data_1.db.dao.LogDao
import com.example.pratice_data_1.db.dao.TravelDao
import com.example.pratice_data_1.db.dao.UserCostDao
import com.example.pratice_data_1.db.dao.UserDao
import com.example.pratice_data_1.db.entities.CostEntity
import com.example.pratice_data_1.db.entities.LogEntity
import com.example.pratice_data_1.db.entities.TravelEntity
import com.example.pratice_data_1.db.entities.UserCostEntity
import com.example.pratice_data_1.db.entities.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(entities = [TravelEntity::class,CostEntity::class,UserCostEntity::class,UserEntity::class,LogEntity::class], version = 2)
@TypeConverters(value = [ListConverter::class])
abstract class AppDatabase: RoomDatabase() {

    companion object{
        lateinit var instance:AppDatabase

        fun setup(context: Context){
            instance = Room.databaseBuilder(
                context = context,
                klass = AppDatabase::class.java,
                name = "app_db"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    abstract fun travelDao():TravelDao
    abstract fun userDao():UserDao
    abstract fun costDao():CostDao
    abstract fun userCostDao():UserCostDao
    abstract fun logDao():LogDao
}
fun ViewModel.addLog(log:String){
    val dao = AppDatabase.instance.logDao()
    viewModelScope.launch(Dispatchers.IO) {
        dao.addLog(LogEntity(log = log))
    }
}